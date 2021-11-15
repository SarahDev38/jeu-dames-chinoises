package com.SarahDev.game.view.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.SarahDev.game.view.colors.Colors;

public class PaintGame extends JPanel {
	private static final long serialVersionUID = 1L;
	private int pause = 0;

	private int gameDiameter = 550; // = Math.min(Frame.dim.width-100, Frame.dim.height-100)-50;
	public Dimension dim = new Dimension(1000, 700);
	private static int nodeDiameter = 27; // (int) (gameDiameter/20); // environ 15
	private static int alpha = 35; // pas en pixels entre nodes
	private final int delay = 50;

	private List<Color> colors;
	private List<String> names = null;
	private List<Integer> scores = null;
	private int rounds;
	private boolean isHuman;
	private int indexCurrent = 0;
	private int winnerIndex = -1;

	private Map<Color, List<Point>> nodes;
	private Map<Color, List<Point>> marbles;
	private List<Point> visited, queue;
	private List<Point> possibles = null;

	private Color color;
	private Color oppositecolor;
	private BoardGUIView view;
	private boolean _nextIsAutomatic;
	
	public PaintGame (BoardGUIView view) {
		this.view = view;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawBackground(g2d);
		displayPlayers(g2d);
		displayRounds(g2d);
		displayWinner(g2d);

		drawNodes(g2d);
		drawMarbles(g2d);

		if (possibles != null)
			drawPossibles(g2d);
		if (!isHuman)
			drawComputerMove(g2d);
		else
			drawHumanMove(g2d);
	}

	private void drawBackground(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		g2d.fillOval((dim.width - gameDiameter) / 2, (dim.height - gameDiameter) / 2, gameDiameter, gameDiameter);
	}

	private void drawNodes(Graphics2D g2d) {
		Iterator<Entry<Color, List<Point>>> it = nodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Color, List<Point>> entry = it.next();
			entry.getValue().forEach(node -> drawOvalNode(g2d, entry.getKey(), node));
		}
	}

	private void drawMarbles(Graphics2D g2d) {
		Iterator<Entry<Color, List<Point>>> it = marbles.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Color, List<Point>> entry = it.next();
			entry.getValue().forEach(point -> fillOvalNode(g2d, entry.getKey(), point));
		}
	}

	private void drawPossibles(Graphics2D g2d) {
		for (int i = 0; i < possibles.size(); i++) {
			fillOvalNode(g2d, Colors.bright(color), possibles.get(i));
		}
	}

	private void displayPlayers(Graphics2D g2d) {
		g2d.setColor(Color.orange);
		g2d.fillRect(10, 10, 200, 75);
		g2d.setFont(new Font("Alice in Wonderland", Font.BOLD, 40));
		g2d.setColor(Color.black);
		g2d.drawString("Joueurs :", 50, 60);
		int height = 80;
		for (int i = 0; i < names.size(); i++) {
			if (!names.get(i).equals("")) {
				Color color = colors.get(i);
				g2d.setColor(color);
				g2d.fillRect(10, height + 10, 200, 90);
				g2d.setFont(new Font("Delius", Font.BOLD, 30));
				g2d.setColor(Colors.fontColor(color));
				g2d.drawString(names.get(i), 30, height + 50);
				g2d.setFont(new Font("Delius", Font.PLAIN, 24));
				g2d.drawString(scores.get(i) + " /10", 130, height + 80);
				if (i == indexCurrent) {
					g2d.setColor(Color.orange);
					g2d.drawRect(10, height + 10, 200, 88);
					g2d.fillOval(215, height + 40, 20, 20);
				}
				height += 90;
			}
		}
	}

	private void displayWinner(Graphics2D g2d) {
		if (winnerIndex != -1) {
			g2d.setColor(oppositecolor);
			g2d.fillRect(10, 520, 250, 100);
			g2d.setColor(color);
			g2d.fillRect(20, 530, 230, 80);
			g2d.setFont(new Font("Delius", Font.BOLD, 26));
			g2d.setColor(Colors.fontColor(color));
			g2d.drawString("GAGNE !", 80, 580);
		}
	}

	private void displayRounds(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.setFont(new Font("Delius", Font.BOLD, 40));
		g2d.drawString("Tour de jeu " + rounds, 350, 50);

	}

	private void drawHumanMove(Graphics2D g2d) {
		if (visited != null && visited.size() > 0) {
			fillOvalNode(g2d, Colors.bright(color), visited.get(0));
			drawSurrondedNode(g2d, oppositecolor, visited.get(0));
			if (queue != null && queue.size() > 1) {
				for (int i = 0; i < queue.size() - 1; i++)
					fillOvalNode(g2d, Colors.bright(color), queue.get(i));
			}
			Point end;
			if (queue != null && queue.size() > 0) {
				end = queue.get(queue.size() - 1);
			} else {
				end = visited.get(0);
			}
			fillOvalNode(g2d, color, end);
			drawSurrondedNode(g2d, oppositecolor, end);
			g2d.setStroke(new BasicStroke(3));
		}
	}

	private void drawOvalNode(Graphics2D g2d, Color color, Point node) {
		g2d.setColor(color);
		g2d.drawOval(getXPosition(node.x, node.y), getYPosition(node.y), nodeDiameter, nodeDiameter);
	}

	private void drawSurrondedNode(Graphics2D g2d, Color color, Point node) {
		g2d.setColor(color);
		g2d.drawOval(getXPosition(node.x, node.y), getYPosition(node.y), nodeDiameter, nodeDiameter);
	}

	private void fillOvalNode(Graphics2D g2d, Color color, Point node) {
		g2d.setColor(color);
		g2d.fillOval(getXPosition(node.x, node.y), getYPosition(node.y), nodeDiameter, nodeDiameter);
	}

	private void fillRunningNode(Graphics2D g2d, Color color, Point start, Point end, int pause) {
		int xstart = getXPosition(start.x, start.y);
		int ystart = getYPosition(start.y);
		int xend = getXPosition(end.x, end.y);
		int yend = getYPosition(end.y);
		int x = (int) (xstart + (xend - xstart) * Math.sin(pause * Math.PI / (2 * delay)));
		int y = (int) (ystart + (yend - ystart) * Math.sin(pause * Math.PI / (2 * delay)));
		g2d.setColor(color);
		g2d.fillOval(x, y, nodeDiameter, nodeDiameter);
	}

	private void drawComputerMove(Graphics2D g2d) {
		if (queue.size() > 0)
			fillOvalNode(g2d, Color.lightGray, queue.get(queue.size() - 1));
		for (int i = 0; i < visited.size(); i++) {
			fillOvalNode(g2d, Colors.bright(color), visited.get(i));
		}
		drawSurrondedNode(g2d, oppositecolor, visited.get(0));
		
		if (pause < delay) {
			Point start = visited.get(visited.size() - 1);
			Point end = queue.get(0);
			fillOvalNode(g2d, Colors.bright(color), start);
			fillRunningNode(g2d, Colors.bright(color), start, end, pause);

		} else if (queue.size() > 0) {
			visited.add(queue.get(0));
			queue.remove(0);
			if (queue.size() != 0)
				pause = 0;
		}
		
		if (queue.size() == 0) {
			fillOvalNode(g2d, color, visited.get(visited.size() - 1));
			drawSurrondedNode(g2d, oppositecolor, visited.get(visited.size() - 1));
		}
		
		pause++;
		
		if (pause < delay*3)
			repaint();
		else if (_nextIsAutomatic) {
			view.changePlayer();
		}
	}

	protected int getXPosition(double x, double y) {
		return (int) ((dim.width - nodeDiameter) / 2 + (x + 0.5 * y) * alpha);
	}

	protected int getYPosition(double y) {
		return (int) ((dim.height - nodeDiameter) / 2 - y * alpha * Math.sqrt(3) / 2);
	}

	public void setPossiblePivots(List<Point> possibles) {
		this.possibles = possibles;
	}

	public void setWay(List<Point> way) {
		visited = new ArrayList<Point>();
		queue = new ArrayList<Point>();
		if (way != null && way.size() > 0) {
			visited.add(way.get(0));
			if (way.size() > 1) {
				for (int i = 1; i < way.size(); i++) {
					queue.add(way.get(i));
				}
			}
			pause = 0;
		}
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setPlayersScore(List<Integer> scores) {
		this.scores = scores;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public void isHuman(boolean isHuman) {
		this.isHuman = isHuman;
	}
	
	public void setNextIsAutomatic(boolean nextIsAutomatic) {
		this._nextIsAutomatic = nextIsAutomatic;
	}
	
	public void setIndexCurrent(int indexCurrent) {
		this.indexCurrent = indexCurrent;
		color = colors.get(indexCurrent);
		oppositecolor = colors.get((indexCurrent + 3) % 6);
	}

	public void setIndexWinner(int winnerIndex) {
		this.winnerIndex = winnerIndex;
	}

	public void setNodes(Map<Color, List<Point>> nodes) {
		this.nodes = nodes;
	}

	public void setMarbles(Map<Color, List<Point>> marbles) {
		this.marbles = marbles;
	}
}
