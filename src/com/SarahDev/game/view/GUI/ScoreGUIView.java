package com.SarahDev.game.view.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.SarahDev.game.controller.Controller;
import com.SarahDev.game.view.ScoreView;

public class ScoreGUIView implements ScoreView {
	private Controller controller;
	private JFrame frame;
	private JPanel container = new JPanel();

	public static Dimension dim = new Dimension(1000, 700);

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void display(LinkedHashMap<Integer, String> playersMap) {
	}

	@Override
	public void displayScore(Map<String, Integer> scores) {
		frame = new JFrame("Jeu de Dames Chinoises");
		container = new JPanel();
		frame.setSize(dim);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container.setLayout(new BorderLayout());

		frame.setJMenuBar(new MenuBar(controller));
		container.add(new BtnNewGame(controller, null), BorderLayout.SOUTH);
		showScore(scores);
		frame.setContentPane(container);
		frame.setVisible(true);
	}

	public void showScore(Map<String, Integer> scores) {
		JPanel panelScore = new JPanel();
		panelScore.setLayout(new BoxLayout(panelScore, BoxLayout.Y_AXIS));

		String header = "                                         *************************************************  \n";
		header += "                       SCORES                            ";
		header += " *************************************************  \n";
		JLabel labelHeader = new JLabel(header);
		panelScore.add(labelHeader);

		if (!scores.isEmpty()) {
			int i = 1;
			for (Map.Entry<String, Integer> entry : scores.entrySet()) {
				String score = " n° " + i + " //      " + entry.getKey() + " :     " + entry.getValue() + " points";
				JLabel labelScore = new JLabel(score);
				panelScore.add(labelScore);
				i++;
			}
		}

		panelScore.setVisible(true);
		container.add(panelScore);
	}

	@Override
	public void exit() {
		if (frame != null)
			frame.setVisible(false);
	}
}
