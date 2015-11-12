/*******************************************************************************
 * ${licenseText}     
 *******************************************************************************/
package net.sf.mcf2pdf.pagebuild;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.*;
import java.util.List;

public class FormattedTextParagraph {
	
	public static enum Alignment {
		LEFT, CENTER, RIGHT, JUSTIFY
	}
	
	private Alignment alignment = Alignment.LEFT;
	
	private List<FormattedText> texts = new Vector<FormattedText>();
	
	public FormattedTextParagraph() {
	}
	
	public void addText(FormattedText text) {
		// if we contain an empty start text, remove that now!
		if (texts.size() == 1 && texts.get(0).getText().length() == 0)
			texts.remove(0);
		texts.add(text);
	}
	
	public List<FormattedText> getTexts() {
		return Collections.unmodifiableList(texts);
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public Alignment getAlignment() {
		return alignment;
	}

	public AttributedCharacterIterator getCharacterIterator(PageRenderContext context) {
		// build whole string
		StringBuilder sb = new StringBuilder();
		for (FormattedText text : texts) {
			sb.append(text.getText());
		}
		AttributedString string = new AttributedString(sb.toString());
		
		// apply formats
		int start = 0;
		for (FormattedText text : texts) {
			Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
			
			// use font created by text (could be a loaded font!)
			Font font = createFont(text, context);
			
			// default attributes (could also be applied to the whole string)
			map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
			
			map.put(TextAttribute.WEIGHT, text.isBold() ? 
					TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
			map.put(TextAttribute.POSTURE, text.isItalic() ? 
					TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
				map.put(TextAttribute.UNDERLINE, text.isUnderline() ? 
						TextAttribute.UNDERLINE_ON : Integer.valueOf(-1));
			
			map.put(TextAttribute.FOREGROUND, text.getTextColor());
			
			float fontSizeInch = text.getFontSize() / 72.0f;
			
			map.put(TextAttribute.SIZE, fontSizeInch * context.getTargetDpi());
			font = font.deriveFont(map);
			map.put(TextAttribute.FONT, font);
						
			string.addAttributes(map, start, start + text.getText().length());
			start += text.getText().length();
		}
		
		return string.getIterator();
	}

	public boolean isEmpty() {
		if (texts.isEmpty())
			return true;
		
		for (FormattedText t : texts) {
			if (t.getText().length() > 0)
				return false;
		}
		
		return true;
	}

	public int getEmptyHeight(Graphics2D graphics, PageRenderContext context) {
		if (texts.isEmpty())
			return 0;
		
		FormattedText ft = texts.get(0);
		float fontSizeInch = ft.getFontSize() / 72.0f;
		Font font = createFont(ft, context).deriveFont(fontSizeInch * context.getTargetDpi());
		
		FontMetrics fm = graphics.getFontMetrics(font);
		return fm.getHeight();
	}
	
	private Font createFont(FormattedText text, PageRenderContext context) {
		Font font = null;
		for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
			if (f.getFamily().equals(text.getFontFamily())) {
				// we just assume that first match to family is best match
				font = f;
				break;
			}
		}
		
		if (font == null) {
			font = context.getFont(text.getFontFamily());
			if (font == null)
				return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0];
		}
		
		return font;
	}
	
	


}
