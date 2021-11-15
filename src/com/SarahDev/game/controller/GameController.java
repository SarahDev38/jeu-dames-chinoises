package com.SarahDev.game.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.SarahDev.game.controller.playersControllers.ComputerController;
import com.SarahDev.game.controller.playersControllers.HumanController;
import com.SarahDev.game.controller.playersControllers.PlayerController;
import com.SarahDev.game.controller.utils.Position;
import com.SarahDev.game.model.GameBoard;
import com.SarahDev.game.model.Player;
import com.SarahDev.game.model.Type;
import com.SarahDev.game.view.ViewType;

public class GameController extends Controller {
	private PlayerController _playerController;
	@SuppressWarnings("unused")
	private GameBoard gameBoard;
	private Boolean _nextIsPossible = true;
	private final List<Player> _winners = new ArrayList<>();
	private boolean nextIsAutomatic = true;

	public GameController(ViewType views, ControllerManager controllerManager) {
		super(views, controllerManager);
	}

	public void display() {
		startView.exit();
		scoreView.exit();
		boardView.exit();
		boardView.display(getPlayersMap());
		gameBoard = new GameBoard(_data.getPlayers());
		boardView.setGameParams(GameBoard._colors, _data.getNames(), GameBoard._nodesMap);
		if (!_data.isInProgressGame() || _data.getRounds() == 1)
			initGameParams();
		_data.setOpenGame(true);
		startGame();
	}

	public void initGameParams() {
		_data.setRounds(1);
		_data.setCurrentIndex(0);
		_data.setInProgressGame(true);
		_data.getPlayers().forEach(player -> player.initMarblesPlace());
	}

	public void startGame() {
		_playerController = getPlayerController();
		while (_playerController == null) {
			setNextPlayer();
		}
		_playerController.displayNewTurn();
	}

	public void treatSelection(Point clicked) {
		if (GameBoard._nodes.contains(clicked))
			_playerController.treatSelection(clicked);
		else if (_data.isCurrentHuman() && !Position.isOnBorder(clicked)) {
			_playerController.removeSelected();
			_playerController.displayNewTurn();
		}
	}

	public void treatPlayerChange() {
		saveData();
		if (_data.getResult(_data.getCurrentIndex()) == 10) {
			if (!_data.isContinueGame()) {
				if (_winners.size() == 0) {
					_winners.add(_data.getCurrent());
					setGameScore(_data.getCurrentIndex());
					displayFirstWinner();
				}
			} else {
				if (!_winners.contains(_data.getCurrent()))
					_winners.add(_data.getCurrent());
				if (_winners.size() >= _playersCount - 1) {
					_data.setInProgressGame(false);
					_data.setOpenGame(false);
					_data.setContinueGame(false);
					displayWinner();
				} else {
					setNextPlayer();
					_playerController.displayNewTurn();
				}
			}
		} else if (_nextIsPossible) {
			setNextPlayer();
			_data.setInProgressGame(true);
			_playerController.displayNewTurn();
			saveData();
		} else {
			_playerController.displayNewTurn();
		}
	}

	protected void setNextPlayer() {
		final int former = _data.getCurrentIndex();
		int next = getNextIndex(_data.getCurrentIndex());
		_data.setCurrentIndex(next);
		if (next < former)
			_data.setRounds(_data.getRounds() + 1);
		_playerController = getPlayerController();
	}

	protected int getNextIndex(int former) {
		int newIndex = (former + 1) % 6;
		while ((_data.getType(newIndex).equals(Type.NoOne) || _data.getPlayer(newIndex).getTempResult() == 10)
				&& (_winners.size() < _playersCount)) {
			newIndex = getNextIndex(newIndex);
		}
		return newIndex;
	}

	private PlayerController getPlayerController() {
		switch (_data.getPlayer(_data.getCurrentIndex()).getType()) {
		case Computer:
			return new ComputerController(this, boardView, _data.getCurrentIndex(), _data.getPlayers(),
					_data.getRounds(), nextIsAutomatic);
		case Human:
			return new HumanController(this, boardView, _data.getCurrentIndex(), _data.getPlayers(), _data.getRounds(),
					nextIsAutomatic);
		default:
			return null;
		}
	}

	public void showHelp() {
		_playerController.showHelp();
	}

	private void displayFirstWinner() {
		saveScore();
		String score = "Scores : \n\n";
		String winner = "";
		String others = "";
		int i = 1;
		for (Player player : _data.getPlayers()) {
			Integer points = _data.getGameScore().get(player.getColor());
			if (player.getType().equals(Type.Human)) {
				if (points > 0)
					winner += "joueur " + (i++) + " " + player.getName() + " : " + points + " pts \n";
				else
					others += "joueur " + (i++) + " " + player.getName() + " : " + points + " pts \n";
			} else if (player.getType().equals(Type.Computer)) {
				if (points > 0)
					winner += " joueur auto " + (i++) + " : " + points + " pts \n";
				else
					others += " joueur auto " + (i++) + " : " + points + " pts \n";
			}
		}
		score += winner;
		score += others;
		score += "\n Continuer la partie ?";
		_playerController.displayFirstWinner(boardView, score);
	}

	public void continueGame(boolean continueG) {
		if (continueG) {
			_data.setContinueGame(true);
			setNextPlayer();
			saveData();
			display();
		} else {
			displayWinner();
		}
	}

	private void displayWinner() {
		_playerController.displayWinner(boardView, _data.getScores());
	}

	public PlayerController getPlayerState() {
		return _playerController;
	}

	public void setNextIsPossible(Boolean nextIsPossible) {
		this._nextIsPossible = nextIsPossible;
	}

	public void setPlayers(List<Player> players) {
		_data.setPlayers(players);
	}

	public void setGameScore(int winnerIndex) {
		_data.resetGameScore();
		int winnerScore = 0;
		for (int i = 0; i < 6; i++) {
			if ((i != winnerIndex) && !_data.getType(i).equals(Type.NoOne)) {
				winnerScore += _data.getPlayer(i).gameScore();
				_data.getGameScore().put(_data.getColor(i), -_data.getPlayer(i).gameScore());
			}
		}
		_data.getGameScore().put(_data.getColor(winnerIndex), winnerScore);
	}

	private void saveScore() {
		if (_data.getGameScore() != null) {
			for (int colorIndex : _data.getGameScore().keySet()) {
				String name = _data.getPlayerByColor(colorIndex).getName();
				Integer tempScore = _data.getScores().get(name);
				if (tempScore != null)
					tempScore += _data.getGameScore().get(colorIndex);
				else
					tempScore = _data.getGameScore().get(colorIndex);
				_data.getScores().put(name, tempScore);
			}
		}
		saveData();
	}

	public void changeAutomatic(String click) {
		switch (click) {
		case "single":
			if (nextIsAutomatic)
				nextIsAutomatic = false;
			else
				treatPlayerChange();
			break;
		case "double":
			nextIsAutomatic = !nextIsAutomatic;
			if (nextIsAutomatic)
				treatPlayerChange();
			break;
		}
	}
}
