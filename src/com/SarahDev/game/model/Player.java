package com.SarahDev.game.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.SarahDev.game.controller.utils.Distance;

public class Player implements Serializable {
	private static final long serialVersionUID = -8687240427250921494L;
	private String _name;
	private int _color;
	private Type _type;
	private List<Point> _marbles;

	public Player(String name, Integer color, Type type) {
		this._name = name.trim();
		this._color = color;
		this._type = type;
		this._marbles = new ArrayList<>();
	}

	public void initMarblesPlace() {
		if (!_type.equals(Type.NoOne))
			this._marbles = new ArrayList<>(GameBoard._nodesMap.get(_color));
		else
			this._marbles = new ArrayList<>();
	}

	public void move(Point previous, Point next) {
		if (_marbles.contains(previous))
			_marbles.set(_marbles.indexOf(previous), next);
	}

	public int getTempResult() {
		int result = 0;
		for (Point marble : _marbles)
			if (GameBoard.getColor(marble) == GameBoard.getOppositeColor(_color))
				result++;
		return result;
	}

	public int gameScore() {
		int score = 0;
		int oppositeColor = GameBoard.getOppositeColor(_color);
		for (Point marble : _marbles) {
			if (GameBoard.getColor(marble) != oppositeColor)
				score += Distance.between(GameBoard.getTop(oppositeColor), marble) - 3;
		}
		return score;
	}

	// -------------------- GETTERS ET SETTERS ---------------------------

	public void setName(String name) {
		this._name = name.trim();
	}

	public String getName() {
		return _name;
	}

	public List<Point> getMarbles() {
		return _marbles;
	}

	public void setType(Type type) {
		this._type = type;
	}

	public Type getType() {
		return _type;
	}

	public int getColor() {
		return this._color;
	}

	public void setColor(int colorIndex) {
		this._color = colorIndex;
	}
}