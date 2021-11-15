package com.SarahDev.game.controller.playersControllers;

import java.awt.Point;
import java.util.List;

import com.SarahDev.game.controller.GameController;
import com.SarahDev.game.controller.moveManagers.ComputerMoveManager;
import com.SarahDev.game.controller.utils.Players;
import com.SarahDev.game.model.Player;
import com.SarahDev.game.view.BoardView;

public class ComputerController extends PlayerController {
	protected ComputerMoveManager computerMoveManager;

	public ComputerController(GameController controller, BoardView boardView, int playerIndex, List<Player> players,
			int rounds, boolean nextIsAutomatic) {
		super(controller, boardView, playerIndex, players, rounds, nextIsAutomatic);
		computerMoveManager = new ComputerMoveManager(players.get(playerIndex), players);
	}

	@Override
	public void displayNewTurn() {
		List<Point> way = computerMoveManager.plotWay();
		_players.get(_playerIndex).move(way.get(0), way.get(way.size() - 1));
		_controller.setPlayers(_players);
		_controller.setNextIsPossible(true);
		_boardView.displayMove(_playerIndex, false, Players.colorPegsMap(_players), way, Players.results(_players),
				_rounds, true, _nextIsAutomatic);
	}

	@Override
	public void treatSelection(Point clicked) {
	}

	@Override
	public void showHelp() {
	}

	@Override
	public void removeSelected() {
	}

}
