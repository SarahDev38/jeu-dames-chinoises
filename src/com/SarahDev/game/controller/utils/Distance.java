package com.SarahDev.game.controller.utils;

import java.awt.Point;

public class Distance {

	public static Integer between(Point node1, Point node2) {
		int a = node1.x - node2.x;
		int b = node1.y - node2.y;
		if (Math.signum(a) == Math.signum(b))
			return (Math.abs(a) + Math.abs(b));
		else if (Math.abs(a) >= Math.abs(b))
			return (Math.abs(a + b) + Math.abs(b));
		else
			return (Math.abs(a + b) + Math.abs(a));
	}
	/*
	 * public static int getBenefit(Point start, Point end, Player player,
	 * List<Point> playersPegs) { return (distanceFromOpposite(start, player,
	 * playersPegs) - distanceFromOpposite(end, player, playersPegs)); }
	 * 
	 * private static int distanceFromOpposite(Point node, Player player,
	 * List<Point> playersPegs) { if (GameBoard.isInOpposite(node, player)) return
	 * 0; int d = 16; for (Point opposite : GameBoard.oppositeNodes(player)) { if
	 * (!playersPegs.contains(opposite)) { d = Math.min(d, between(opposite, node));
	 * } } return d; }
	 */
}
