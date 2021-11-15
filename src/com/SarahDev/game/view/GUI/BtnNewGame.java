package com.SarahDev.game.view.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.SarahDev.game.controller.Controller;

public class BtnNewGame extends JButton {
	private static final long serialVersionUID = 1L;

	public BtnNewGame(Controller controller, StartGUIView launchGUIView) {
		super();
		this.setText(" nouveau jeu ");
		this.setBackground(Color.BLACK);
		this.setForeground(Color.white);
		this.setBorderPainted(false);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (launchGUIView != null)
					launchGUIView.startGame();
				else {
					controller.closeGame();
					controller.choosePlayers();
				}
			}
		});
	}
}
