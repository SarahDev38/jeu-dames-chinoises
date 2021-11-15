package com.SarahDev.game.controller.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;

public class Position {

	public static boolean placeIsFree(int x, int y, List<Point> allMarbles) {
		return (!allMarbles.contains(new Point(x, y)));
	}

	public static boolean areAligned(Point node1, Point node2) {
		return areAligned(node1.x, node1.y, node2.x, node2.y);
	}

	public static boolean areAligned(int x1, int y1, int x2, int y2) {
		return ((x1 == x2) || (y1 == y2) || ((x1 - x2) == (y2 - y1)));
	}

	/*
	 * utilisé pour les gros doigts : si on ne vise pas bien un node en bordure du
	 * jeu évite de recommencer le trajet
	 */
	public static boolean isOnBorder(Point touchpoint) {
		if (Math.abs(touchpoint.x) == 5)
			return true;
		if (Math.abs(touchpoint.y) == 5)
			return true;
		if (Math.abs(touchpoint.x + touchpoint.y) == 5)
			return true;
		return false;
	}

	public static boolean isBlockingEnd(Player player, List<Player> players, Point start, Point end) {
		if (GameBoard.getColor(end) != GameBoard.getOppositeColor(player.getColor()))
			return false;

		List<Point> oppositeNodes = GameBoard.getNodesOfColor(GameBoard.getOppositeColor(player.getColor()));
		List<Point> newMarbles = new ArrayList<>(player.getMarbles());
		newMarbles.set(newMarbles.indexOf(start), end);
		if (countRemainingInTop3(GameBoard.getOppositePlayer(player, players), oppositeNodes) > 0) {
			switch (countRemainingInTop2(GameBoard.getOppositePlayer(player, players), oppositeNodes)) {
			case 0:
				if (!newMarbles.contains(oppositeNodes.get(5)) && !newMarbles.contains(oppositeNodes.get(8)))
					return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
				if (newMarbles.contains(oppositeNodes.get(5)) && newMarbles.contains(oppositeNodes.get(8)))
					return !oneIsFree(oppositeNodes, newMarbles, 2, 7);
				if (newMarbles.contains(oppositeNodes.get(5)))
					return !oneIsFree(oppositeNodes, newMarbles, 1, 4, 6, 7);
				return !oneIsFree(oppositeNodes, newMarbles, 0, 2, 3, 4);
			case 1:
				if (!newMarbles.contains(oppositeNodes.get(5)) && !newMarbles.contains(oppositeNodes.get(8)))
					return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
				if (newMarbles.contains(oppositeNodes.get(5)))
					return !oneIsFree(oppositeNodes, newMarbles, 1, 4, 6, 7);
				return (!oneIsFree(oppositeNodes, newMarbles, 0, 2, 3, 4));
			case 2:
				return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
			}
		}
		return false;
	}

	private static Boolean oneIsFree(List<Point> oppositeNodes, List<Point> marbles, Integer... indexes) {
		for (Integer i : indexes)
			if (!marbles.contains(oppositeNodes.get(i)))
				return true;
		return false;
	}

	private static int countRemainingInTop3(Player oppositePlayer, List<Point> oppositeNodes) {
		int n = 0;
		for (Point marble : oppositePlayer.getMarbles())
			if (marble.equals(oppositeNodes.get(5)) || marble.equals(oppositeNodes.get(8))
					|| marble.equals(oppositeNodes.get(9)))
				n++;
		return n;
	}

	private static int countRemainingInTop2(Player oppositePlayer, List<Point> oppositeNodes) {
		int n = 0;
		for (Point marble : oppositePlayer.getMarbles())
			if (marble.equals(oppositeNodes.get(5)) || marble.equals(oppositeNodes.get(8)))
				n++;
		return n;
	}

}
