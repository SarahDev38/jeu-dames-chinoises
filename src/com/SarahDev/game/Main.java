package com.SarahDev.game;

import com.SarahDev.game.controller.ControllerManager;
import com.SarahDev.game.view.ViewType;

public class Main {

	public static void main(String[] args) {
		ControllerManager manager = new ControllerManager(ViewType.GUI);
		manager.launch();
	}
}