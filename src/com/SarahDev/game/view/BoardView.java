package com.SarahDev.game.view;

import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface BoardView extends View {

	public void setGameParams(List<Integer> colors, List<String> names, Map<Integer, List<Point>> nodes);

	public void displayMove(int indexCurrent, boolean isHuman,
			Map<Integer, List<Point>> marbles, List<Point> way,
			List<Integer> scores, int rounds, boolean nextIsPossible, boolean nextIsAutomatic);

	public void showHelpForPlayer(List<Point> possibles);

	public void displayWinner(Map<Integer, List<Point>> marbles, List<String> names, List<Integer> scores, Map<String, Integer> newScoresMap);

	public void displayFirstWinner(Map<Integer, List<Point>> marbles, List<String> names, List<Integer> scores, String scoresText);

}
