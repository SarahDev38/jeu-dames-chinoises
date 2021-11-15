package com.SarahDev.game.controller.moveManagers;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.SarahDev.game.controller.utils.Distance;
import com.SarahDev.game.controller.utils.Position;
import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;

public class ComputerMoveManager extends MoveManager {
	private final Map<Point, Point> _map;
	private final MoveGraph _graph;
	private Point _start;
	private Point _end;

	public ComputerMoveManager(Player player, List<Player> players) {
		super(player, players);
		_map = new HashMap<>();
		_graph = new MoveGraph(_player, _players);
	}

	public List<Point> plotWay() {
		System.out.println();
		System.out.println(_player.getColor());
		System.out.println();
		addAndChooseStarts();
		System.out.println();
		chooseEndPoint();
		if (GameBoard.isFirstRound(_player) && Distance.between(GameBoard.getTop(_color), _end) < 6)
			randomChoose();
		return _graph.getLinkedNodesBetween(_start, _end);
	}

	private void randomChoose() {
		List<Integer> firstsStarts = Arrays.asList(0, 1, 2, 3, 4, 6, 7);
		int randomIndex = firstsStarts.get((int) (Math.random() * firstsStarts.size()));
		_start = _marbles.get(randomIndex);
		Set<Point> visited = collectEnds(_start);
		randomIndex = (int) (Math.random() * visited.size());
		Point[] visitedArray = visited.toArray(new Point[0]);
		_end = visitedArray[randomIndex];
	}

	private void addAndChooseStarts() {
		for (Point start : _marbles) {
			for (Point end : collectEnds(start))
				if (!_map.containsKey(end) || checkForChange(start, end, _map.get(end), end))
					_map.put(end, start);
		}
	}

	private Set<Point> collectEnds(Point start) {
		Set<Point> visited = _graph.depthFirstTraversal(start, GameBoard.getAllMarbles(_players));
		visited.remove(start);
		visited.addAll(getAdjacentsList(start));
		Set<Point> visitedCopy = new HashSet<>(visited);
		for (Point end : visitedCopy) {
			if (!GameBoard.isParkPossible(end, _color))
				visited.remove(end);
			else if (Position.isBlockingEnd(_player, _players, start, end))
				visited.remove(end);
		}
		return visited;
	}

	private void chooseEndPoint() {
		_end = null;
		for (Point end : _map.keySet()) {
			Point start = _map.get(end);
			if ((_end == null) || checkForChange(start, end, _start, _end)) {
				_start = start;
				_end = end;
			}
		}
	}

	private boolean checkForChange(Point start, Point end, Point oldStart, Point oldEnd) {
		if (getBenefit(start, end) > 0) {
			if (_player.getTempResult() > 7) {
				if (distanceToFreeOppositeNode(start) > distanceToFreeOppositeNode(oldStart)
						&& (getBenefit(start, end) + 2 > getBenefit(oldStart, oldEnd))) 
					return true;
				}
			if (getBenefit(start, end) > getBenefit(oldStart, oldEnd)) 
				return true;
			if (getBenefit(start, end) == getBenefit(oldStart, oldEnd)) {
				if (start.equals(oldStart) && Distance.between(GameBoard.getTop(_oppositeColor), end) < Distance
						.between(GameBoard.getTop(_oppositeColor), oldEnd)) 
					return true;
				else if (start.equals(oldStart) && Distance.between(GameBoard.getTop(_oppositeColor), end) > Distance
						.between(GameBoard.getTop(_oppositeColor), oldEnd)) 
					return false;
				if (isStartPossibleEndForOther(start, end, _graph)
						&& !isStartPossibleEndForOther(oldStart, oldEnd, _graph)) 
					return true;
				if (isPivotMovePossibleAfter(start, end) && !isPivotMovePossibleAfter(oldStart, oldEnd)) 
					return true;
				if ((Distance.between(end, GameBoard.getTop(_oppositeColor)) > 4) && isEndAlignedWithOther(start, end) && !isEndAlignedWithOther(oldStart, oldEnd)) 
					return true;
				if (GameBoard.isInStartArea(start, _color) && !GameBoard.isInStartArea(oldStart, _color)) 
					return true;
				if (GameBoard.isInStartArea(start, _color) && GameBoard.isInStartArea(oldStart, _color)
						&& (distanceToFreeOppositeNode(start) > distanceToFreeOppositeNode(oldStart))) 
					return true;
				else if (GameBoard.isInStartArea(start, _color) && GameBoard.isInStartArea(oldStart, _color)
						&& (distanceToFreeOppositeNode(start) < distanceToFreeOppositeNode(oldStart)))
					return false;
				if (!GameBoard.isArrived(start, _color) && GameBoard.isArrived(oldStart, _color)) 
					return true;
				if (distanceToFreeOppositeNode(start) > distanceToFreeOppositeNode(oldStart)) 
					return true;
				else if (distanceToFreeOppositeNode(start) < distanceToFreeOppositeNode(oldStart)) 
					return false;
				if (distanceToFreeOppositeNode(end) < distanceToFreeOppositeNode(oldEnd)) 
					return true;
			}
		} else if ((getBenefit(start, end) == 0) && (getBenefit(oldStart, oldEnd) <= 0)) {
			if (getBenefit(oldStart, oldEnd) < 0) 
				return true;
			if (isStartPossibleEndForOther(start, end, _graph)
					&& !isStartPossibleEndForOther(oldStart, oldEnd, _graph)) 
				return true;
			if (isPivotMovePossibleAfter(start, end) && !isPivotMovePossibleAfter(oldStart, oldEnd)) 
				return true;
			if (isEndAlignedWithOther(start, end) && !isEndAlignedWithOther(oldStart, oldEnd)) 
				return true;
			if (GameBoard.isInTipArea(end, _color) && !GameBoard.isInTipArea(oldEnd, _color)) 
				return true;
		}
		return false;
	}
}
