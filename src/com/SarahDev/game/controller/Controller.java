package com.SarahDev.game.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.SarahDev.game.controller.utils.FileIOUtils;
import com.SarahDev.game.model.DataModel;
import com.SarahDev.game.model.Player;
import com.SarahDev.game.model.Type;
import com.SarahDev.game.view.BoardView;
import com.SarahDev.game.view.ScoreView;
import com.SarahDev.game.view.StartView;
import com.SarahDev.game.view.ViewType;

public abstract class Controller {
	private ControllerManager controllerManager;
	protected StartView startView;
	protected BoardView boardView;
	protected ScoreView scoreView;
	protected DataModel _data;
	protected int _playersCount = 2;

	public Controller(ViewType views, ControllerManager controllerManager) {
		this.controllerManager = controllerManager;
		views.setController(this);
		this.startView = views.getStart();
		this.boardView = views.getBoard();
		this.scoreView = views.getScore();
		readData();
		updatePlayersCount();
	}

	public void choosePlayers() {
		controllerManager.launch();
	}

	public void play() {
		controllerManager.play();
	}

	public void displayScores() {
		controllerManager.showScores();
	}

	public abstract void display();

	public abstract void startGame();

	// ----------------------------- about data

	public DataModel getData() {
		return this._data;
	}

	public void readData() {
		this._data = FileIOUtils.readFile();
	}

	public void saveData() {
		FileIOUtils.writeFile(_data);
	}

	// ----------------------------- data update

	public void updatePlayersCount() {
		_playersCount = 0;
		for (Player player : _data.getPlayers())
			if (!player.getType().equals(Type.NoOne))
				_playersCount++;
	}

	/**
	 * @return the LinkedHashMap of all the players based on _data : key : player's
	 *         color ; value : player's name
	 */
	protected LinkedHashMap<Integer, String> getPlayersMap() {
		LinkedHashMap<Integer, String> playersMap = new LinkedHashMap<Integer, String>();
		if (_data == null || _data.getPlayers() == null) {
			_data = new DataModel();
			setGamePosition();
			saveData();
		}
		_data.getPlayers().forEach(player -> playersMap.put(player.getColor(), player.getName()));
		return playersMap;
	}

	public void updatePlayerName(int index, String name) {
		if (_data.getType(index).equals(Type.Human))
			_data.setName(index, name);
		_data.setInProgressGame(false);
		saveData();
	}

	public void changePlayerType(int index, boolean isHuman) {
		_data.setType(index, isHuman ? Type.Human : Type.Computer);
		_data.setInProgressGame(false);
		_data.setInProgressGame(false);
		saveData();
		updatePlayersCount();
	}

	public void deletePlayer(int index) {
		_data.setType(index, Type.NoOne);
		_data.setInProgressGame(false);
		_data.setInProgressGame(false);
		saveData();
		updatePlayersCount();
	}

	public void closeGame() {
		_data.setOpenGame(false);
		_data.setInProgressGame(false);
		saveData();
	}

	protected void setGamePosition() {
		List<Player>[] repartition = setRepartition();
		_playersCount = repartition[0].size();
		switch (_playersCount) {
		case 2:
			_data.setPlayers(allocatePlayers(repartition, true, false, false, true, false, false));
			break;
		case 3:
			_data.setPlayers(allocatePlayers(repartition, true, false, true, false, true, false));
			break;
		case 4:
			_data.setPlayers(allocatePlayers(repartition, false, true, true, false, true, true));
			break;
		case 5:
			_data.setPlayers(allocatePlayers(repartition, false, true, true, true, true, true));
			break;
		case 6:
			_data.setPlayers(allocatePlayers(repartition, true, true, true, true, true, true));
			break;
		}
		setNames();
	}

	@SuppressWarnings("unchecked")
	private List<Player>[] setRepartition() {
		List<Player> playersList = new ArrayList<>();
		List<Player> noPlayersList = new ArrayList<>();

		for (Player player : getData().getPlayers()) {
			if (player.getType().equals(Type.NoOne))
				noPlayersList.add(player);
			else
				playersList.add(player);
		}
		return new List[] { playersList, noPlayersList };
	}

	private List<Player> allocatePlayers(List<Player>[] playersRepartition, Boolean... isPlayers) {
		List<Player> newPlayers = new ArrayList<Player>();
		int iPlayer = 0;
		int iNoPlayer = 0;
		for (Boolean isPlayer : isPlayers)
			if (isPlayer)
				newPlayers.add(playersRepartition[0].get(iPlayer++));
			else
				newPlayers.add(playersRepartition[1].get(iNoPlayer++));
		return newPlayers;
	}

	private void setNames() {
		int i = 1;
		int j = 1;
		for (Player player : _data.getPlayers()) {
			if (player.getType().equals(Type.NoOne))
				player.setName("");
			if (player.getType().equals(Type.Computer))
				player.setName("auto " +(i++));
			else if (player.getType().equals(Type.Human) && player.getName().equals(""))
					player.setName("joueur " + (j++));
		}
		if (i == 2) {
			for (Player p : _data.getPlayers())
				if (p.getName().startsWith("auto "))
					p.setName("auto");
		}
		if (j == 2) {
			for (Player p : _data.getPlayers())
				if (p.getName().startsWith("joueur "))
					p.setName("joueur");
		}
	}

}
