package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Paintable extends Shapes {
	private Color fillColor = Color.web("#000000ff");

	public abstract double surface();

	public abstract double perimeter();

	public abstract void fill(final GraphicsContext gc);

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

}
