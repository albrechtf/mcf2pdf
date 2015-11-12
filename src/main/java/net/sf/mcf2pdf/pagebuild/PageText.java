/*******************************************************************************
 * ${licenseText}
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mcf2pdf.mcfelements.McfText;
import net.sf.mcf2pdf.mcfelements.util.ImageUtil;
import net.sf.mcf2pdf.pagebuild.FormattedTextParagraph.Alignment;


/**
 * TODO comment
 */
public class PageText implements PageDrawable {

	private static final Pattern PATTERN_HTML_TEXT_PARA = Pattern.compile("<p\\s([^>]*)>(([^<]|<[^/]|</[^p]|</p[^>])*)</p>");
	private static final Pattern PATTERN_PARA_ALIGN = Pattern.compile("(?:\\s|^)align=\"([^\"]+)\"");
	private static final Pattern PATTERN_PARA_STYLE = Pattern.compile("(?:\\s|^)style=\"([^\"]+)\"");
	private static final Pattern PATTERN_HTML_TEXT_SPAN = Pattern.compile("<span\\s+style=\"([^\"]+)\"[^>]*>([^<]+)</span>");

	private McfText text;

	private List<FormattedTextParagraph> paras;

	public PageText(McfText text) {
		this.text = text;
		parseText();
	}

	@Override
	public float getLeftMM() {
		return text.getArea().getLeft() / 10.0f;
	}

	@Override
	public float getTopMM() {
		return text.getArea().getTop() / 10.0f;
	}

	@Override
	public int getZPosition() {
		return text.getArea().getZPosition();
	}

	private void parseText() {
		// parse text out of content
		String htmlText = text.getHtmlContent();

		paras = new Vector<FormattedTextParagraph>();

		Matcher mp = PATTERN_HTML_TEXT_PARA.matcher(htmlText);
		int curStart = 0;
		while (mp.find(curStart)) {
			FormattedTextParagraph para = new FormattedTextParagraph();
			String paraAttrs = mp.group(1);
			Matcher malign = PATTERN_PARA_ALIGN.matcher(paraAttrs);
			if (malign.find()) {
				String align = malign.group(1);
				if ("center".equals(align))
					para.setAlignment(Alignment.CENTER);
				else if ("right".equals(align))
					para.setAlignment(Alignment.RIGHT);
				else if ("justify".equals(align))
					para.setAlignment(Alignment.JUSTIFY);
			}

			// extract font family and size for possibly empty paras, add empty span
			Matcher mstyle = PATTERN_PARA_STYLE.matcher(paraAttrs);
			if (mstyle.find()) {
				para.addText(createFormattedText("", mstyle.group(1)));
			}

			String paraContent = mp.group(2);
			Matcher ms = PATTERN_HTML_TEXT_SPAN.matcher(paraContent);
			int curSpanStart = 0;
			while (ms.find(curSpanStart)) {
				para.addText(createFormattedText(ms.group(2), ms.group(1)));
				curSpanStart = ms.end();
			}
			curStart = mp.end();

			paras.add(para);
		}
	}

	@Override
	public boolean isVectorGraphic() {
		return true;
	}

	@Override
	public void renderAsSvgElement(Writer writer, PageRenderContext context) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public BufferedImage renderAsBitmap(PageRenderContext context,
			Point drawOffsetPixels) throws IOException {
		context.getLog().debug("Rendering text");
		int width = context.toPixel(text.getArea().getWidth() / 10.0f);
		int height = context.toPixel(text.getArea().getHeight() / 10.0f);

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = img.createGraphics();
		int curY = context.toPixel(text.getVerticalIndentMargin() / 10.0f);
		int x = context.toPixel(text.getIndentMargin() / 10.0f);

		// background color?
		if (text.getArea().getBackgroundColor() != null) {
			graphics.setColor(text.getArea().getBackgroundColor());
			graphics.fillRect(0, 0, width, height);
		}

		Rectangle rc = new Rectangle(x, 0, width - x, height);

		for (FormattedTextParagraph para : paras) {
			curY = drawParagraph(para, graphics, rc, curY, context);
			if (curY > height)
				break;
		}

		graphics.dispose();

		if (text.getArea().getRotation() != 0) {
			return ImageUtil.rotateImage(img,
					(float)Math.toRadians(text.getArea().getRotation()), drawOffsetPixels);
		}
		return img;
	}

	private int drawParagraph(FormattedTextParagraph para, Graphics2D graphics,
			Rectangle rc, int curY, PageRenderContext context) {
		// if empty paragraph
		if (para.isEmpty())
			return curY + para.getEmptyHeight(graphics, context);

		AttributedCharacterIterator paragraph = para.getCharacterIterator(context);
		int paragraphStart = paragraph.getBeginIndex();
		int paragraphEnd = paragraph.getEndIndex();
		FontRenderContext frc = graphics.getFontRenderContext();
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

		float breakWidth = rc.width;
		float drawPosY = curY;

		lineMeasurer.setPosition(paragraphStart);

		while (lineMeasurer.getPosition() < paragraphEnd
				&& drawPosY <= rc.y + rc.height) {
			TextLayout layout = lineMeasurer.nextLayout(breakWidth);
			// for justified style
			TextLayout justifiedLayout = layout.getJustifiedLayout(breakWidth);

			float drawPosX;

			switch (para.getAlignment()) {
			case CENTER:
				drawPosX = (rc.width - layout.getAdvance()) / 2.0f + rc.x;
				break;
			case RIGHT:
				drawPosX = !layout.isLeftToRight() ? 0 : breakWidth
						- layout.getAdvance();
				drawPosX += rc.x;
				break;
			case JUSTIFY:
				if (lineMeasurer.getPosition() < paragraphEnd)
					layout = justifiedLayout;
				// fallthrough
			default:
				drawPosX = layout.isLeftToRight() ? 0 : breakWidth
						- layout.getAdvance();
				drawPosX += rc.x;
			}

			drawPosY += layout.getAscent();
			layout.draw(graphics, drawPosX, drawPosY);
			drawPosY += layout.getDescent() + layout.getLeading();
		}

		return (int)drawPosY;
	}


	private FormattedText createFormattedText(String text, String css) {
		// parse attributes out of css
		String[] avPairs = css.split(";");

		boolean bold = false;
		boolean italic = false;
		boolean underline = false;
		float fontSize = 12.0f;
		String fontFamily = "Arial";
		Color textColor = Color.black;

		for (String avp : avPairs) {
			avp = avp.trim();
			if (!avp.contains(":"))
				continue;
			String[] av = avp.split(":");
			if (av.length != 2)
				continue;
			String a = av[0].trim();
			String v = av[1].trim();

			try {
				if ("font-family".equalsIgnoreCase(a))
					fontFamily = v.replace("'", "");
				if ("font-size".equalsIgnoreCase(a) && v.matches("[0-9]+pt"))
					fontSize = Float.valueOf(v.substring(0, v.indexOf("pt"))).floatValue();
				if ("font-weight".equalsIgnoreCase(a))
					bold = Integer.valueOf(v).intValue() > 400;
				if ("text-decoration".equalsIgnoreCase(a))
					underline = "underline".equals(v);
				if ("color".equalsIgnoreCase(a))
					textColor = Color.decode(v);
				if ("font-style".equalsIgnoreCase(a))
					italic = "italic".equals(v);
			}
			catch (Exception e) {
				// ignore invalid attributes
			}
		}

		// escape HTML entities in text, as they seem to be "double-encoded"
		// TODO this should be replaced by some utility function
		text = text.replace("&amp;", "&");
		text = text.replace("&quot;", "\"");
		text = text.replace("&lt;", "<");
		text = text.replace("&gt;", ">");
		return new FormattedText(text, bold, italic, underline, textColor, fontFamily, fontSize);
	}



}
