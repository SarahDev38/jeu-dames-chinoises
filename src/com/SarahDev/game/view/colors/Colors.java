package com.SarahDev.game.view.colors;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Colors {

	public static Color color(int index) {
		if (index < 6)
			return Team.values()[index].getColor();
		if (index == 6)
			return Color.gray;
		return null;
	}

	public static List<Color> colors(List<Integer> color) {
		List<Color> colors = new ArrayList<Color>();
		for (Integer index : color) {
			colors.add(color(index));
		}
		return colors;
	}

	public static int getColorIndex(Color color) {
		for (Team team : Team.values()) {
			if (team.getColor().equals(color))
				return team.ordinal();
		}
		return 0;
	}

	public static Color fontColor(Color color) {
		if (color == Team.BLACK.color || color == Color.blue)
			return Color.white;
		return Color.black;
	}
	
	public static Color bright(Color color) {
		return Team.values()[getColorIndex(color)].getBrightColor();
	}

	public static Map<Color, List<Point>> colorMap(Map<Integer, List<Point>> integerMap) {
		Map<Color, List<Point>> colorMap = new HashMap<Color, List<Point>>();
		for (Map.Entry<Integer, List<Point>> mapEntry : integerMap.entrySet()) {
			colorMap.put(Colors.color(mapEntry.getKey()), mapEntry.getValue());
		}
		return colorMap;
	}

}
