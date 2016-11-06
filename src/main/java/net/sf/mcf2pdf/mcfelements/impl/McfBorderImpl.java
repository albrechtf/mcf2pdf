package net.sf.mcf2pdf.mcfelements.impl;

import java.awt.Color;

import net.sf.mcf2pdf.mcfelements.McfBorder;

public class McfBorderImpl implements McfBorder {

	private Color color;

	private float offset;

	private float width;

	private boolean enabled;

	@Override
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	@Override
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
