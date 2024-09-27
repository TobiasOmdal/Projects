package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shapes {
	private Color color = Color.BLACK;
	private double lineWidth;

	public Shapes(Color color) {
		this.color = color;
	}

	public Shapes() {

	}

	public double getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(double lineWidth) {
		if (lineWidth < 1 || lineWidth > 50) {
			throw new IllegalArgumentException("Not a valid line width.");
		}
		this.lineWidth = lineWidth;

	}

	public abstract void paint(final GraphicsContext gc);

	public abstract boolean contains(final Point pc);

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}