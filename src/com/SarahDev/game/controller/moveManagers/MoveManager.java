package com.SarahDev.game.controller.moveManagers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.SarahDev.game.controller.utils.Distance;
import com.SarahDev.game.controller.utils.PivotMove;
import com.SarahDev.game.controller.utils.Position;
import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;

public abstract class MoveManager {
	protected List<Player> _players;
	protected Player _player;
	protected List<Point> _marbles;
	protected final int _color;
	protected final int _oppositeColor;
	protected final List<Point> _oppositeNodes;

	public MoveManager(Player player, List<Player> players) {
		this._players = players;
		this._player = player;
		this._marbles = player.getMarbles();
		this._color = _player.getColor();
		this._oppositeColor = GameBoard.getOppositeColor(_color);
		this._oppositeNodes = GameBoard.getNodesOfColor(_oppositeColor);
	}

	/*
	 * retourne la liste de tous les trous existants et disponibles où on peut se
	 * déplacer par saut d'une case en partant de start
	 */
	protected List<Point> getAdjacentsList(Point start) {
		List<Point> nodes = new ArrayList<>();
		for (int i = -1; i < 2; i++)
			for (int j = -1; j < 2; j++) {
				Point adjacent = new Point(start.x + i, start.y + j);
				if ((j != i) && !GameBoard.getAllMarbles(_players).contains(adjacent)
						&& GameBoard._nodes.contains(adjacent))
					nodes.add(adjacent);
			}
		return nodes;
	}

	/*
	 * retourne la liste de tous les trous existants et disponibles où on peut se
	 * déplacer par saut pivot en partant de start, sachant que le tout premier
	 * point de départ est root (différent de start si plusieurs sauts consécutifs)
	 */
	protected List<Point> getEndsListAfterOnePivotMove(Point start, Point root, List<Point> allMarbles) {
		List<Point> endsList = new ArrayList<>();
		for (Point pivot : allMarbles)
			if (PivotMove.isPossibleAround(pivot, root, start, allMarbles))
				endsList.add(PivotMove.getEnd(start, pivot));
		return endsList;
	}

	protected boolean isStartPossibleEndForOther(Point start, Point end, MoveGraph graph) {
		List<Point> newAllMarblesList = new ArrayList<>(GameBoard.getAllMarbles(_players));
		newAllMarblesList.set(newAllMarblesList.indexOf(start), end);
		for (Point marble : _marbles) {
			if (!marble.equals(start) && !GameBoard.isArrived(marble, _color)
					&& graph.depthFirstTraversal(marble, newAllMarblesList).contains(start)
					&& (getBenefit(marble, start) > 0))
				return true;
		}
		return false;
	}

	protected boolean isEndAlignedWithOther(Point start, Point end) {
		for (Point marble : _marbles) {
			if (!marble.equals(start) && !GameBoard.isArrived(marble, _color) && Position.areAligned(marble, end)
					&& (getBenefit(marble, end) > 0))
				return true;
		}
		return false;
	}

	protected boolean isPivotMovePossibleAfter(Point start, Point end) {
		List<Point> newAllMarblesList = new ArrayList<>(GameBoard.getAllMarbles(_players));
		newAllMarblesList.set(newAllMarblesList.indexOf(start), end);
		for (Point pivot : newAllMarblesList) {
			if (PivotMove.isPossibleAround(pivot, end, end, newAllMarblesList)) {
				Point pivotEnd = PivotMove.getEnd(end, pivot);
				if (!GameBoard.isInColoredArea(pivotEnd) || GameBoard.isArrived(pivotEnd, _color))
					return true;
			}
		}
		return false;
	}

	protected int getBenefit(Point start, Point end) {
		return (distanceToFreeOppositeNode(start) - distanceToFreeOppositeNode(end));
	}

	protected int distanceToFreeOppositeNode(Point node) {
		if (GameBoard.isArrived(node, _color))
			return 0;
		int d = 16;
		for (Point opposite : _oppositeNodes) {
			if (!_marbles.contains(opposite)
					&& !_players.get((_players.indexOf(_player) + 3) % 6).getMarbles().contains(opposite)) {
				d = Math.min(d, Distance.between(opposite, node));
			}
		}
		return d;
	}
}