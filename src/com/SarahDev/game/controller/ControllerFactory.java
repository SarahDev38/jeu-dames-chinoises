package com.SarahDev.game.controller;

import com.SarahDev.game.view.ViewType;

public class ControllerFactory {
	private ViewType views;
	private ControllerManager controllerManager;

	public ControllerFactory(ViewType views, ControllerManager controllerManager) {
		this.views = views;
		this.controllerManager = controllerManager;
	}

	protected Controller getController(String request) {
		switch (request) {
		case "launch":
			return new StartController(views, controllerManager);
		case "game":
			return new GameController(views, controllerManager);
		case "score":
			return new ScoreController(views, controllerManager);
		default:
			return null;
		}

	}

}
