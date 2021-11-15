package com.SarahDev.game.view.colors;

import java.awt.Color;

public enum Team {
	BLACK(new Color(33,33,33), new Color(97,97,97)), YELLOW(new Color(255, 193, 7), new Color(255, 236, 179)), GREEN(new Color(139,195,74), new Color(220, 237, 200)), WHITE(new Color(250, 250, 250), new Color(230, 230, 230)),
	BLUE(new Color(3, 169, 244), new Color(179, 229, 252)), RED(new Color(244, 67, 54), new Color(255, 205, 210));

	protected Color color;
	protected Color brightColor;

	private Team(Color color, Color brightColor) {
		this.color = color;
		this.brightColor = brightColor;
	}

	public Color getColor() {
		return color;
	}

	public Color getBrightColor() {
		return brightColor;
	}
}
