package model;

import javafx.scene.paint.Color;

public class Brush {

	private String name;
	private Color color;
	private int size;

	public Brush(String name, Color color, int size) {
		this.name = name;
		this.color = color;
		// checks if size is within slider values
		if (size < 1 || size > 50) {
			throw new IllegalArgumentException("Not a valid brush size!");
		}
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + "," + color + "," + size + "\n";
	}

}
