package com.SarahDev.game.view;

import com.SarahDev.game.controller.Controller;
import com.SarahDev.game.view.GUI.BoardGUIView;
import com.SarahDev.game.view.GUI.StartGUIView;
import com.SarahDev.game.view.GUI.ScoreGUIView;

public enum ViewType {
	GUI(new StartGUIView(), new BoardGUIView(), new ScoreGUIView());
// web

	protected StartView start;
	protected BoardView board;
	protected ScoreView score;

	private ViewType(StartView start, BoardView board, ScoreView score) {
		this.start = start;
		this.board = board;
		this.score = score;
	}

	public StartView getStart() {
		return start;
	}

	public BoardView getBoard() {
		return board;
	}

	public ScoreView getScore() {
		return score;
	}
	public void setController(Controller controller) {
		this.start.setController(controller);
		this.board.setController(controller);
		this.score.setController(controller);
	}
	
}
