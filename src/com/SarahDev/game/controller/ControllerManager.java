package com.SarahDev.game.controller;

import com.SarahDev.game.view.ViewType;

public class ControllerManager {
	private ControllerFactory factory;
	private Controller controller;

	public ControllerManager(ViewType views) {
		factory = new ControllerFactory(views, this);
	}

	public void launch() {
		controller = factory.getController("launch");
		controller.display();
	}

	public void play() {
		controller = factory.getController("game");
		controller.display();
	}

	public void showScores() {
		controller = factory.getController("score");
		controller.display();
	}
}
