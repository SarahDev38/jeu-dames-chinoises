package com.SarahDev.game.view;

import java.util.LinkedHashMap;

import com.SarahDev.game.controller.Controller;

public interface View {

	public void setController(Controller controller);

	public void display(LinkedHashMap<Integer, String> names);

	public void exit();

}
