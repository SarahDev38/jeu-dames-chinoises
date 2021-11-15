package com.SarahDev.game.controller.moveManagers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.SarahDev.game.controller.utils.Distance;
import com.SarahDev.game.controller.utils.ListUtils;
import com.SarahDev.game.controller.utils.PivotMove;
import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;

public class HumanMoveManager extends MoveManager {
	private enum State {
		NewTurn, SelectMarble, PivotMove, NodeMove
	}

	private State _state;
	private List<Point> _way;

	public HumanMoveManager(Player player, List<Player> players) {
		super(player, players);
		_state = State.NewTurn;
		_way = new ArrayList<>();
	}

	public List<Point> changeSelected(Point clicked) {
		Point last = null;
		if (_way.size() > 0) {
			last = _way.get(_way.size() - 1);
			if (!clicked.equals(last))
				_player.move(last, _way.get(0));
		}
		if (!clicked.equals(last)) {
			_way.clear();
			_way.add(clicked);
			_state = State.SelectMarble;
		}
		return _way;
	}

	public List<Point> moveSelected(Point clicked) {
		if (_state == State.SelectMarble || _state == State.PivotMove) {
			if (PivotMove.isPossible(_way.get(0), _way.get(_way.size() - 1), clicked,
					GameBoard.getAllMarbles(_players))) {
				_player.move(_way.get(_way.size() - 1), clicked);
				_way.add(clicked);
				_state = State.PivotMove;
			} else if (_state == State.SelectMarble && (Distance.between(_way.get(_way.size() - 1), clicked) == 1
					&& !GameBoard.getAllMarbles(_players).contains(clicked))) {
				_player.move(_way.get(_way.size() - 1), clicked);
				_way.add(clicked);
				_state = State.NodeMove;
			}
		}
		if (_state == State.NodeMove || _state == State.PivotMove) {
			if (_way.size() > 0 && _way.get(0).equals(clicked)) {
				_player.move(_way.get(_way.size() - 1), clicked);
				_way.clear();
				_way.add(clicked);
				_state = State.SelectMarble;
			}
		}
		_way = ListUtils.tidy(_way);
		return _way;
	}

	public boolean isPossibleEndOfTurn() {
		if (_way != null && _way.size() > 0)
			return (!(_way.get(_way.size() - 1).equals(_way.get(0)))
					&& GameBoard.isParkPossible(_way.get(_way.size() - 1), _color));
		return false;
	}

	public List<Point> findPossiblesForSelected() {
		if ((_way != null) && (_way.size() != 0)) {
			Point selected = _way.get(0);
			List<Point> formerWay = new ArrayList<>(_way);
			State formerState = _state;
			initSelected();

			MoveGraph graph = new MoveGraph(_player, _players);
			Set<Point> possibles = graph.depthFirstTraversal(selected, GameBoard.getAllMarbles(_players));
			possibles.remove(selected);
			List<Point> possiblesCopy = new ArrayList<>(possibles);
			for (Point point : possiblesCopy) {
				if (!GameBoard.isParkPossible(point, _color))
					possibles.remove(point);
			}
			_way = formerWay;
			_state = formerState;
			if (_way.size() > 0)
				_player.move(_way.get(0), _way.get(_way.size() - 1));
			return new ArrayList<>(possibles);
		}
		return null;
	}

	public List<Point> findPossibles() {
		initSelected();
		MoveGraph graph = new MoveGraph(_player, _players);
		Set<Point> possibles = new HashSet<>();
		Set<Point> tempPossibles;
		for (Point marble : _marbles) {
			if (!GameBoard.isArrived(marble, _color)) {
				tempPossibles = graph.depthFirstTraversal(marble, GameBoard.getAllMarbles(_players));
				tempPossibles.remove(marble);
				possibles.addAll(tempPossibles);
			}
		}
		tempPossibles = new HashSet<>(possibles);
		for (Point point : tempPossibles) {
			if (!GameBoard.isParkPossible(point, _color))
				possibles.remove(point);
		}
		return new ArrayList<>(possibles);
	}

	public void initSelected() {
		if (_way.size() > 0)
			_player.move(_way.get(_way.size() - 1), _way.get(0));
		_way.clear();
		_state = State.NewTurn;
	}
}