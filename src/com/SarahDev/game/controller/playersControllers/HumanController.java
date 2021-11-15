package com.SarahDev.game.controller.playersControllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.SarahDev.game.controller.GameController;
import com.SarahDev.game.controller.moveManagers.HumanMoveManager;
import com.SarahDev.game.controller.utils.Players;
import com.SarahDev.game.model.Player;
import com.SarahDev.game.view.BoardView;

public class HumanController extends PlayerController {
	private final HumanMoveManager _humanMoveManager;
	private List<Point> _way = new ArrayList<>();

	public HumanController(GameController controller, BoardView boardView, int playerIndex, List<Player> players,
			int rounds, boolean nextIsAutomatic) {
		super(controller, boardView, playerIndex, players, rounds, nextIsAutomatic);
		_humanMoveManager = new HumanMoveManager(players.get(playerIndex), players);
	}

	@Override
	public void displayNewTurn() {
		_humanMoveManager.initSelected();
		_boardView.displayMove(_playerIndex, true, Players.colorPegsMap(_players), null, Players.results(_players),
				_rounds, false, false);
		_controller.setNextIsPossible(false);
	}

	@Override
	public void treatSelection(Point clicked) {
		if (_players.get(_playerIndex).getMarbles().contains(clicked)) {
			_way = _humanMoveManager.changeSelected(clicked);
		} else
			_way = _humanMoveManager.moveSelected(clicked);
		_boardView.displayMove(_playerIndex, true, Players.colorPegsMap(_players), _way, Players.results(_players),
				_rounds, _humanMoveManager.isPossibleEndOfTurn(), false);
		_controller.setNextIsPossible(_humanMoveManager.isPossibleEndOfTurn());
	}

	@Override
	public void showHelp() {
		_humanMoveManager.initSelected();
		_way.clear();
		_boardView.displayMove(_playerIndex, true, Players.colorPegsMap(_players), null, Players.results(_players),
				_rounds, false, false);
		_boardView.showHelpForPlayer(_humanMoveManager.findPossibles());
	}

	@Override
	public void removeSelected() {
		_humanMoveManager.initSelected();
		_way.clear();
	}
}
