package com.SarahDev.game.controller.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.SarahDev.game.model.Player;

public class Players {

	public static Player oppositePlayer(List<Player> players, Player player) {
		return players.get((players.indexOf(player) + 3) % 6);
	}

	public static List<Integer> results(List<Player> players) {
		List<Integer> results = new ArrayList<Integer>();
		players.forEach(player -> results.add(player.getTempResult()));
		return results;
	}

	public static List<Point> pegs(List<Player> players) {
		List<Point> pegs = new ArrayList<Point>();
		for (Player player : players)
			for (Point peg : player.getMarbles())
				pegs.add(peg);
		return pegs;
	}

	public static Map<Integer, List<Point>> colorPegsMap(List<Player> players) {
		Map<Integer, List<Point>> playersPegs = new HashMap<Integer, List<Point>>();
		players.forEach(player -> playersPegs.put(player.getColor(), player.getMarbles()));
		return playersPegs;
	}

	public static LinkedHashMap<Integer, String> colorNamesMap(List<Player> players) {
		if (players != null) {
			LinkedHashMap<Integer, String> playersMap = new LinkedHashMap<Integer, String>();
			players.forEach(player -> playersMap.put(player.getColor(), player.getName()));
			return playersMap;
		}
		return null;
	}
}
