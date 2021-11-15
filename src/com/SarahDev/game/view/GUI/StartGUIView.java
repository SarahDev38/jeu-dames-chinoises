package com.SarahDev.game.view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.SarahDev.game.controller.Controller;
import com.SarahDev.game.view.StartView;
import com.SarahDev.game.view.colors.Colors;

public class StartGUIView implements StartView {
	private Controller controller;

	private JFrame frame;
	private JPanel container;
	private JButton boutonNewGame;
	private Dimension launchDim = new Dimension(600, 450);

	private Color[] colors = new Color[6];
	private String[] names = new String[6];

	private JTextField[] nameFields = new JTextField[6];
	private Font policeLabel = new Font("Arial", Font.PLAIN, 20);
	private Font policeTextField = new Font("Arial", Font.PLAIN, 14);

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void display(LinkedHashMap<Integer, String> playersMap) {
		frame = new JFrame("Jeu de Dames Chinoises");
		frame.setSize(launchDim);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(new MenuBar(controller));

		container = new JPanel();
		container.setLayout(new BorderLayout());
		boutonNewGame = new BtnNewGame(controller, this);
		container.add(boutonNewGame, BorderLayout.SOUTH);

		initializePlayers(playersMap);
		createContent();
		checkEnableNewgame();

		frame.setContentPane(container);
		frame.setVisible(true);
	}

	@Override
	public void startGame() {
		controller.startGame();
	}

	@Override
	public void exit() {
		if (frame != null)
			frame.setVisible(false);
	}

	private void checkEnableNewgame() {
		boutonNewGame.setEnabled(false);
		for (int i = 0; i < 6; i++)
			if (!nameFields[i].getText().trim().equals(""))
				boutonNewGame.setEnabled(true);
	}

	private void initializePlayers(LinkedHashMap<Integer, String> playersMap) {
		int i = 0;
		for (Map.Entry<Integer, String> entry : playersMap.entrySet()) {
			colors[i] = Colors.color(entry.getKey());
			names[i++] = entry.getValue();
		}
	}

	private void createContent() {
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		addTitle(panelCenter);
		JPanel panelPlayers = new JPanel();
		panelPlayers.setLayout(new GridLayout(2, 3));

		for (int i = 0; i < 6; i++) {
			JPanel panelPlayer = new JPanel();
			panelPlayer.setBackground(colors[i]);
			panelPlayer.setLayout(new BoxLayout(panelPlayer, BoxLayout.Y_AXIS));
			addLabelPlayer(panelPlayer);

			JPanel panelName = new JPanel();
			panelName.setBackground(colors[i]);
			panelName.setPreferredSize(new Dimension(200, 40));
			nameFields[i] = createTFNom(i);
			JLabel removeButton = addRemoveButton(i);
			if (names[i].equals("")) {
				nameFields[i].setVisible(false);
				removeButton.setVisible(false);
			}
			panelName.add(nameFields[i]);
			panelName.add(removeButton);

			panelPlayer.add(panelName);

			JPanel panelButtons = new JPanel();
			panelButtons.setBackground(colors[i]);
			addComputerButton(panelButtons, i, removeButton);
			addPlayerButton(panelButtons, i, removeButton);
			panelPlayer.add(panelButtons);

			panelPlayers.add(panelPlayer);
		}
		panelCenter.add(panelPlayers, BorderLayout.CENTER);
		container.add(panelCenter, BorderLayout.CENTER);
	}

	private void addTitle(JPanel panel) {
		JLabel labelTitre = new JLabel("JOUEURS");
		labelTitre.setFont(new Font("Arial", Font.BOLD, 48));
		labelTitre.setHorizontalAlignment(JLabel.CENTER);
		panel.add(labelTitre, BorderLayout.NORTH);
	}

	private void addLabelPlayer(JPanel panel) {
		JLabel labNom = new JLabel("joueur :");
		labNom.setFont(policeLabel);
		if (getLuminance(panel.getBackground()) < 90)
			labNom.setForeground(Color.white);
		panel.add(labNom);
	}

	private int getLuminance(Color color) {
		return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
	}

	private JTextField createTFNom(int i) {
		String name;
		if (names[i].startsWith("auto"))
			name = "auto";
		else if (names[i].startsWith("joueur"))
			name = "nom :";
		else
			name = names[i];
		JTextField tf = new JTextField(name);
		tf.setFont(policeTextField);
		tf.setHorizontalAlignment(JTextField.RIGHT);
		tf.setEditable(!tf.getText().equals("auto"));
		tf.setPreferredSize(new Dimension(150, 20));
		tf.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				checkEnableNewgame();
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					startGame();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				controller.updatePlayerName(i, ((JTextField) e.getSource()).getText());
			}
		});
		return tf;
	}

	private void addComputerButton(JPanel panelButtons, int i, JLabel removeButton) {
		Image img = new ImageIcon("resources/computer.png").getImage().getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
		JLabel computerButton = new JLabel(new ImageIcon(img));
		computerButton.addMouseListener(new TypeListener(i, "auto", removeButton));
		panelButtons.add(computerButton);
	}

	private void addPlayerButton(JPanel panelButtons, int i, JLabel removeButton) {
		Image img = new ImageIcon("resources/user.png").getImage().getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
		JLabel playerButton = new JLabel(new ImageIcon(img));
		playerButton.addMouseListener(new TypeListener(i, "", removeButton));
		panelButtons.add(playerButton);
	}

	private JLabel addRemoveButton(int i) {
		Image img = new ImageIcon("resources/remove.png").getImage().getScaledInstance(25, 25,
				java.awt.Image.SCALE_SMOOTH);
		JLabel removeButton = new JLabel(new ImageIcon(img));
		removeButton.addMouseListener(new RemoveBtnListener(i));
		return removeButton;
	}

	private class TypeListener implements MouseListener {
		private int index;
		private String text;
		private JLabel removeButton;

		public TypeListener(int index, String text, JLabel removeButton) {
			this.index = index;
			this.text = text;
			this.removeButton = removeButton;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean isHuman  =!text.equals("auto");
			nameFields[index].setVisible(true);
			nameFields[index].setEditable(isHuman);
			removeButton.setVisible(true);
			nameFields[index].setText(text);
			checkEnableNewgame();
			controller.updatePlayerName(index, text);
			controller.changePlayerType(index, isHuman);
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
	}

	private class RemoveBtnListener implements MouseListener {
		private int index;

		public RemoveBtnListener(int index) {
			this.index = index;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			nameFields[index].setText("");
			nameFields[index].setVisible(false);
			((JLabel) e.getSource()).setVisible(false);
			checkEnableNewgame();
			controller.deletePlayer(index);
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
	}
}
