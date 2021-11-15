package com.SarahDev.game.controller.moveManagers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.SarahDev.game.controller.utils.Distance;
import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;

public class MoveGraph extends MoveManager {
	private Map<Point, List<Point>> map;

	public MoveGraph(Player player, List<Player> players) {
		super(player, players);
	}

	protected Set<Point> depthFirstTraversal(Point start, List<Point> allMarbles) {
		map = new HashMap<>();
		map.put(start, new ArrayList<>());
		Set<Point> visited = new HashSet<>();
		Stack<Point> stack = new Stack<>();
		stack.push(start);
		while (!stack.isEmpty()) {
			Point node = stack.pop();
			addLandingNodesToMap(node, start, allMarbles);
			for (Point adjNode : map.get(node)) {
				if (!visited.contains(adjNode) && !stack.contains(adjNode))
					stack.push(adjNode);
			}
			visited.add(node);
		}
		return visited;
	}

	protected List<Point> getLinkedNodesBetween(Point start, Point end) {
		map = new HashMap<Point, List<Point>>();
		map.put(start, new ArrayList<Point>());
		List<Point> way = new ArrayList<>();
		if (Distance.between(start, end) != 1) {
			Set<Point> visited = new HashSet<Point>();
			Stack<Point> stack = new Stack<Point>();
			stack.push(start);
			way.add(start);
			while (!stack.isEmpty()) {
				Point node = stack.pop();
				addLandingNodesToMap(node, start, GameBoard.getAllMarbles(_players));
				for (Point adjNode : map.get(node)) {
					if (!visited.contains(adjNode) && !stack.contains(adjNode))
						stack.push(adjNode);
				}
				if (!visited.contains(node)) {
					visited.add(node);
					if (!way.contains(node))
						way.add(node);
					if (node.equals(end)) {
						sortOutWay(way);
						return way;
					}
				}
			}
		} else {
			way.add(start);
			way.add(end);
		}
		return way;
	}

	private List<Point> sortOutWay(List<Point> way) {
		Point cursor = way.get(way.size() - 1);
		while (!cursor.equals(way.get(0))) {
			Point beforeCursor = way.get(way.indexOf(cursor) - 1);
			if (!map.get(cursor).contains(beforeCursor)) {
				way.remove(beforeCursor);
			} else
				cursor = beforeCursor;
		}
		if (way.size() > 2) {
			tidyUpWay(way, 0);
		}
		return way;
	}

	private List<Point> tidyUpWay(List<Point> way, int index) {
		if (index < way.size() - 2) {
			for (int i = way.size() - 1; i > index + 1; i--) {
				if (map.get(way.get(i)).contains(way.get(index))) {
					for (int j = i - 1; j > index; j--)
						way.remove(way.get(j));
					return tidyUpWay(way, index);
				}
			}
			return tidyUpWay(way, ++index);
		}
		return way;
	}

	private void addLandingNodesToMap(Point start, Point root, List<Point> allMarbles) {
		getEndsListAfterOnePivotMove(start, root, allMarbles).forEach(adjacent -> {
			map.putIfAbsent(adjacent, new ArrayList<Point>());
			addValueToKey(start, adjacent);
			addValueToKey(adjacent, start);
		});
	}

	private void addValueToKey(Point node1, Point node2) {
		List<Point> tempList = map.get(node1);
		if (tempList != null)
			tempList.remove(node2);
		else
			tempList = new ArrayList<Point>();
		tempList.add(node2);
		map.put(node1, tempList);
	}
}
