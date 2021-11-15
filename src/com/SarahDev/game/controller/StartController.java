package com.SarahDev.game.controller;

import javax.swing.JOptionPane;

import com.SarahDev.game.view.ViewType;

public class StartController extends Controller {

	public StartController(ViewType views, ControllerManager controllerManager) {
		super(views, controllerManager);
	}

	@Override
	public void display() {
		boardView.exit();
		scoreView.exit();
		startView.display(getPlayersMap());
		if (_data.isOpenGame() && _data.isInProgressGame()) {
			int response = JOptionPane.showConfirmDialog(null, "Reprendre la partie ?", "Partie en cours",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION)
				play();
			else {
				_data.setInProgressGame(false);
				_data.setOpenGame(false);
			}
		}
	}

	public void startGame() {
		if (!_data.isInProgressGame()) {
			_data.setRounds(1);
			_data.setCurrentIndex(0);
		}
		setGamePosition();
		saveData();
		play();
	}
}
