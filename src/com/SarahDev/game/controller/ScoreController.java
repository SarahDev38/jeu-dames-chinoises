package com.SarahDev.game.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import com.SarahDev.game.controller.utils.MapUtils;
import com.SarahDev.game.view.ViewType;

public class ScoreController extends Controller {
	protected static Map<String, Integer> scores;

	public ScoreController(ViewType views, ControllerManager controllerManager) {
		super(views, controllerManager);
	}

	@Override
	public void display() {
		startView.exit();
		boardView.exit();
		scoreView.display(getPlayersMap());

		scores = (LinkedHashMap<String, Integer>) MapUtils.sortByValue(_data.getScores());

		scoreView.displayScore(scores);
	}

	@Override
	public void startGame() {
	}
}
