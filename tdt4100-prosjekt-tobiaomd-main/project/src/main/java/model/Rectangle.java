package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Square {
	private int width;

	public Rectangle(Point corner, int length, int width, Color color, Color fillColor, double lineWidth) {
		this(corner, length, width, color);
		setFillColor(fillColor);
		setLineWidth(lineWidth);
	}

	public Rectangle(Point corner, int length, int width, Color color) {
		this(corner, length, width);
		setColor(color);
	}

	public Rectangle(Point corner, int length, int width) {
		setCorner(corner);
		setLength(length);
		setWidth(width);
	}

	public Rectangle() {

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width < 0) {
			throw new IllegalArgumentException("Cannot draw rectangle with negative width");
		}
		this.width = width;
	}

	public double surface() {
		return length * width;
	}

	public double perimeter() {
		return 2 * length + 2 * width;
	}

	@Override
	public String toString() {
		return "UpLeftCorner" + corner.toString() + " Length=" + length + " Width=" + width;
	}

	public Line diagonal() {
		return new Line(corner, new Point(getEndX(), getEndY()));
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle temporaryRectangle = (Rectangle) obj;
			if (corner.equals(temporaryRectangle.getCorner()) && length == temporaryRectangle.getLength()
					&& width == temporaryRectangle.getWidth()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void fill(final GraphicsContext gc) {
		gc.setFill(getFillColor());
		gc.fillRect(getStartX(), getStartY(), getLength(), getWidth());
	}

	@Override
	public void paint(final GraphicsContext gc) {
		fill(gc);
		gc.setLineWidth(getLineWidth());
		gc.setStroke(getColor());
		gc.strokeRect(getStartX(), getStartY(), getLength(), getWidth());
	}

	@Override
	public boolean contains(final Point pc) {
		if (getStartX() <= pc.getX() && getEndX() >= pc.getX() && getStartY() <= pc.getY() && getEndY() >= pc.getY()) {
			return true;
		}
		return false;
	}

}
