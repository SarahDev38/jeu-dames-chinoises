package com.SarahDev.game.controller.playersControllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.SarahDev.game.controller.GameController;
import com.SarahDev.game.controller.utils.Players;
import com.SarahDev.game.model.Player;
import com.SarahDev.game.view.BoardView;

public abstract class PlayerController {
	protected GameController _controller;
	protected BoardView _boardView;
	protected List<Player> _players;
	protected int _rounds;
	protected int _playerIndex;
	protected boolean _nextIsAutomatic;

	public PlayerController(GameController controller, BoardView boardView, int playerIndex, List<Player> players,
			int rounds, boolean nextIsAutomatic) {
		this._controller = controller;
		this._boardView = boardView;
		this._playerIndex = playerIndex;
		this._players = players;
		this._rounds = rounds;
		this._nextIsAutomatic = nextIsAutomatic;
	}

	public abstract void displayNewTurn();
	
	public abstract void treatSelection(Point clicked);

	public abstract void showHelp();

	public abstract void removeSelected();

	public void displayWinner(BoardView boardView, Map<String, Integer> newScoresMap) {
		boardView.displayWinner(Players.colorPegsMap(_players), getNames(), Players.results(_players), newScoresMap);
	}
	
	public void displayFirstWinner(BoardView boardView, String scoresText) {
		boardView.displayFirstWinner(Players.colorPegsMap(_players), getNames(), Players.results(_players), scoresText);
	}

	public List<String> getNames() {
		List<String> playersName = new ArrayList<String>();
		_players.forEach(player -> playersName.add(player.getName()));
		return playersName;
	}
}
