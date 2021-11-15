package com.SarahDev.game.view.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.SarahDev.game.controller.Controller;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private JMenu menuFichier, menuAPropos;
	private JMenuItem menuNouveau, menuRegles, menuScores, menuQuitter, menuPropos;

	public MenuBar(Controller controller) {
		super();
		menuFichier = new JMenu("Fichier");
		menuAPropos = new JMenu("A Propos");
		menuNouveau = new JMenuItem("Nouveau");
		menuRegles = new JMenuItem("Règles");
		menuScores = new JMenuItem("Scores");
		menuQuitter = new JMenuItem("Quitter");
		menuPropos = new JMenuItem("A Propos");
		menuFichier.setMnemonic('F');
		menuAPropos.setMnemonic('O');
		menuFichier.setPreferredSize(new Dimension(100, 20));
		menuAPropos.setPreferredSize(new Dimension(100, 20));
		menuNouveau.setPreferredSize(new Dimension(100, 25));
		menuRegles.setPreferredSize(new Dimension(100, 25));
		menuScores.setPreferredSize(new Dimension(100, 25));
		menuQuitter.setPreferredSize(new Dimension(100, 25));
		menuPropos.setPreferredSize(new Dimension(100, 25));

		this.menuNouveau.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.choosePlayers();
			}
		});

		this.menuRegles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		this.menuScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.displayScores();
			}
		});

		this.menuQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		this.menuPropos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		this.menuFichier.add(menuNouveau);
		this.menuFichier.addSeparator();
		this.menuFichier.add(menuRegles);
		this.menuFichier.add(menuScores);
		this.menuFichier.addSeparator();
		this.menuFichier.add(menuQuitter);
		this.menuAPropos.add(menuPropos);
		this.add(menuFichier);
		this.add(menuAPropos);

	}

}
