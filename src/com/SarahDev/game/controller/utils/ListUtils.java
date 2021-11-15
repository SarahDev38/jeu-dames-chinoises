package com.SarahDev.game.controller.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ListUtils {

	public static List<Point> tidy(List<Point> way) {
		List<Point> tidy = new ArrayList<>();
		int regress = way.size();
		if (way.size() > 2) {
			for (int i = 0; i < way.size() - 2; i++)
				if (way.get(i).equals(way.get(way.size() - 1))) {
					regress = i + 1;
					break;
				}
		}
		for (int j = 0; j < regress; j++)
			tidy.add(way.get(j));
		return tidy;
	}

}
