package com.SarahDev.game.view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.SarahDev.game.controller.Controller;
import com.SarahDev.game.controller.GameController;
import com.SarahDev.game.view.BoardView;
import com.SarahDev.game.view.colors.Colors;

public class BoardGUIView implements BoardView {
	private Controller controller;
	protected static PaintGame plateau;
	protected List<Color> colors;
	protected List<String> names;
	protected Map<Color, List<Point>> nodes;
	private boolean _isHuman = true;
	private boolean _nextIsAutomatic;
	private boolean isAlreadyOneClick = false;

	private JFrame frame;
	private JPanel container;
	protected JButton buttonNext, buttonHelp;
	public Dimension dim = new Dimension(1000, 730);

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void display(LinkedHashMap<Integer, String> names) {
		frame = new JFrame("Jeu de Dames Chinoises");
		container = new JPanel();
		frame.setSize(dim);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setLayout(new BorderLayout());

		plateau = initPlateau();
		container.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x, y = 8 - (e.getY() - 95) / 30;
				if (y % 2 == 0)
					x = (e.getX() - 203) / 35 - 8 - y / 2;
				else
					x = (e.getX() - 220) / 35 - 7 - (y + 1) / 2;
				((GameController) controller).treatSelection(new Point(x, y));
				plateau.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		container.add(plateau, BorderLayout.CENTER);

		frame.setJMenuBar(new MenuBar(controller));
		container.add(new BtnNewGame(controller, null), BorderLayout.SOUTH);

		frame.setContentPane(container);
		frame.setVisible(true);
	}
	
	private PaintGame initPlateau() {
		PaintGame paintGame = new PaintGame(this);
		paintGame.setLayout(null);
		buttonNext = createButtonNext();
		buttonHelp = createButtonHelp();
		paintGame.add(buttonNext);
		paintGame.add(buttonHelp);
		return paintGame;
	}

	@Override
	public void displayMove(int index, boolean isHuman, Map<Integer, List<Point>> marbles, List<Point> way,
			List<Integer> scores, int rounds, boolean nextIsPossible, boolean nextIsAutomatic) {
		this._isHuman = isHuman;
		this._nextIsAutomatic = nextIsAutomatic;
		plateau.setNextIsAutomatic(_nextIsAutomatic);
		plateau.setColors(colors);
		plateau.setNodes(nodes);
		plateau.setNames(names);
		plateau.setRounds(rounds);
		plateau.setPlayersScore(scores);
		plateau.setIndexCurrent(index);
		plateau.setWay(way);
		plateau.repaint();
		plateau.isHuman(isHuman);
		plateau.setMarbles(Colors.colorMap(marbles));
		plateau.setPossiblePivots(null);
		updateButtonNext(index, nextIsPossible, isHuman, nextIsAutomatic);
	}

	@Override
	public void showHelpForPlayer(List<Point> possibles) {
		plateau.setPossiblePivots(possibles);
		plateau.repaint();
	}

	@Override
	public void setGameParams(List<Integer> colors, List<String> names, Map<Integer, List<Point>> nodes) {
		this.colors = Colors.colors(colors);
		this.names = names;
		this.nodes = Colors.colorMap(nodes);
	}

	@Override
	public void displayFirstWinner(Map<Integer, List<Point>> marbles, List<String> names, List<Integer> scores,
			String scoresText) {
		int dialogResult = JOptionPane.showConfirmDialog(null, scoresText, "Fin de la partie ?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			plateau.setIndexWinner(-1);
			plateau.repaint();
			((GameController) controller).continueGame(true);
		} else {
			((GameController) controller).continueGame(false);
		}
	}

	@Override
	public void displayWinner(Map<Integer, List<Point>> marbles, List<String> names, List<Integer> scores,
			Map<String, Integer> newScoresMap) {
		buttonNext.setEnabled(false);
		plateau.setMarbles(Colors.colorMap(marbles));
		plateau.setNames(names);
		plateau.setPlayersScore(scores);
		plateau.repaint();

		String strScores = "";
		strScores += "\n Voir tous les scores ?";
		int dialogResult = JOptionPane.showConfirmDialog(null, strScores, "Scores ", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (dialogResult == JOptionPane.YES_OPTION) {
			controller.displayScores();
		}
	}

	@Override
	public void exit() {
		if (frame != null)
			frame.setVisible(false);
	}

	private JButton createButtonNext() {
		Image img = new ImageIcon("resources/next.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		JButton button = new JButton(new ImageIcon(img));
		button.setBounds(860, 500, 100, 100);
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_isHuman)
					changePlayer();
				else {
					if (isAlreadyOneClick) {
						isAlreadyOneClick = false;
						changeAutomatic("double");
					} else {
						isAlreadyOneClick = true;
						Timer t = new Timer("doubleclickTimer", false);
						t.schedule(new TimerTask() {
							@Override
							public void run() {
								if (isAlreadyOneClick) {
									changeAutomatic("single");
								}
								isAlreadyOneClick = false;
							}
						}, 500);
					}
				}
			}
		});
		return button;
	}

	protected void changeAutomatic(String click) {
		((GameController) controller).changeAutomatic(click);
	}

	protected void changePlayer() {
		((GameController) controller).treatPlayerChange();
	}

	private void updateButtonNext(int index, boolean nextIsPossible, boolean isHuman, boolean nextIsAutomatic) {
		Image img;
		if (!isHuman && nextIsAutomatic)
			img = new ImageIcon("resources/pause.png").getImage().getScaledInstance(90, 100, Image.SCALE_SMOOTH);
		else
			img = new ImageIcon("resources/next.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		buttonNext.setIcon(new ImageIcon(img));
		buttonNext.setBackground(colors.get(index));
		buttonNext.setEnabled(nextIsPossible);
	}

	private JButton createButtonHelp() {
		Image img = new ImageIcon("resources/idea.png").getImage().getScaledInstance(90, 100, Image.SCALE_SMOOTH);
		JButton button = new JButton(new ImageIcon(img));
		button.setBounds(860, 50, 90, 100);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((GameController) controller).showHelp();
			}
		});
		return button;
	}

}
