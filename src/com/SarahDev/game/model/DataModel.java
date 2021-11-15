package com.SarahDev.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModel implements Serializable {
	private static final long serialVersionUID = 3087484074774282127L;
	private List<Player> _players = new ArrayList<>();
	private int _rounds = 1;
	private int _currentIndex = 0;
	private boolean _helpAllowed = true;
	private boolean _inProgressGame = false;
	private boolean _openGame = false;
	private boolean _continueGame = false;
	private final Map<String, Integer> _scores = new HashMap<>();
	private final Map<Integer, Integer> _gameScore = new HashMap<>();
	private int _delay = 40;
	public static final int MAX_DELAY = 80;

	public DataModel() {
		initPlayers();
	}

	public DataModel(List<Player> players) {
		_players = players;
	}

	public void initPlayers() {
		_players.clear();
		_players.add(new Player("nom :", 0, Type.Human));
		_players.add(new Player("auto", 1, Type.Computer));
		_players.add(new Player("", 2, Type.NoOne));
		_players.add(new Player("", 3, Type.NoOne));
		_players.add(new Player("", 4, Type.NoOne));
		_players.add(new Player("", 5, Type.NoOne));
	}

	public void setName(int index, String name) {
		_players.get(index).setName(name);
	}

	public String getName(int index) {
		return _players.get(index).getName();
	}

	public List<String> getNames() {
		List<String> playersName = new ArrayList<String>();
		_players.forEach(player -> playersName.add(player.getName()));
		return playersName;
	}

	public void setType(int index, Type type) {
		_players.get(index).setType(type);
	}

	public Type getType(int index) {
		return _players.get(index).getType();
	}

	public Integer getIndexByColor(int color) {
		return _players.indexOf(getPlayerByColor(color));
	}

	public int getColor(int index) {
		return _players.get(index).getColor();
	}

	public Player getPlayer(int index) {
		return _players.get(index);
	}

	public Player getPlayerByColor(int color) {
		for (Player player : _players)
			if (player.getColor() == color)
				return player;
		return null;
	}

	public boolean isCurrentHuman() {
		return getCurrent().getType().equals(Type.Human);
	}

	public Player getCurrent() {
		return _players.get(_currentIndex);
	}

	public int getResult(int index) {
		return _players.get(index).getTempResult();
	}

// -------------------- GETTERS ET SETTERS ---------------------------

	public List<Player> getPlayers() {
		return _players;
	}

	public void setPlayers(List<Player> players) {
		this._players = players;
	}

	public int getRounds() {
		return _rounds;
	}

	public void setRounds(int rounds) {
		this._rounds = rounds;
	}

	public void setInProgressGame(boolean inProgressGame) {
		this._inProgressGame = inProgressGame;
	}

	public boolean isInProgressGame() {
		return _inProgressGame;
	}

	public boolean isOpenGame() {
		return _openGame;
	}

	public void setOpenGame(boolean openGame) {
		this._openGame = openGame;
	}

	public int getCurrentIndex() {
		return _currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this._currentIndex = currentIndex;
	}

	public void resetGameScore() {
		this._gameScore.clear();
	}

	public Map<Integer, Integer> getGameScore() {
		return _gameScore;
	}

	public Map<String, Integer> getScores() {
		return _scores;
	}

	public void resetScores() {
		this._scores.clear();
	}

	public int getDelay() {
		return _delay;
	}

	public void setDelay(int delay) {
		this._delay = delay;
	}

	public boolean isHelpAllowed() {
		return _helpAllowed;
	}

	public void setHelpAllowed(boolean helpAllowed) {
		this._helpAllowed = helpAllowed;
	}

	public boolean isContinueGame() {
		return _continueGame;
	}

	public void setContinueGame(boolean continueGame) {
		this._continueGame = continueGame;
	}
}
