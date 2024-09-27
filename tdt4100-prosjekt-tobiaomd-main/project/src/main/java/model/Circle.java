package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Paintable {

	private Point center;
	private int radius;

	public Circle(Point center, int radius, Color color, Color fillColor, double lineWidth) {
		this(center, radius, color);
		setFillColor(fillColor);
		setLineWidth(lineWidth);
	}

	public Circle(Point center, int radius, Color color) {
		this(center, radius);
		setColor(color);
	}

	public Circle(Point center, int radius) {
		setCenter(center);
		setRadius(radius);
	}

	public Circle() {

	}

	public int getDiameter() {
		return radius * 2;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		if (radius < 0) {
			throw new IllegalArgumentException("Radius cannot be negative!");
		}
		this.radius = radius;
	}

	public double surface() {
		return radius * radius * Math.PI;
	}

	public double perimeter() {
		return 2 * radius * Math.PI;
	}

	public String toString() {
		return "Center(" + center + ") Radius=" + radius;
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Circle) {
			Circle temporaryCircle = (Circle) obj;
			if (center.equals(temporaryCircle.getCenter()) && radius == temporaryCircle.getRadius())
				return true;
		}
		return false;
	}

	public int compareTo(final Object obj) {
		if (obj instanceof Circle) {
			Circle temporaryCircle = (Circle) obj;
			return (int) (surface() - temporaryCircle.surface());
		}
		return 0;
	}

	@Override
	public void fill(final GraphicsContext graphicsContext) {
		graphicsContext.setFill(getFillColor());
		graphicsContext.fillOval(getStartX(), getStartY(), getDiameter(), getDiameter());
	}

	@Override
	public void paint(final GraphicsContext graphicsContext) {
		fill(graphicsContext);
		graphicsContext.setLineWidth(getLineWidth());
		graphicsContext.setStroke(getColor());
		graphicsContext.strokeOval(getStartX(), getStartY(), getDiameter(), getDiameter());
	}

	@Override
	public boolean contains(final Point pc) {
		if (getCenter().distance(pc) <= getRadius()) {
			return true;
		}
		return false;
	}

	public int getStartX() {
		return getCenter().getX() - getRadius();
	}

	public int getStartY() {
		return getCenter().getY() - getRadius();
	}

	public int getEndX() {
		return getCenter().getX() + getRadius();
	}

	public int getEndY() {
		return getCenter().getY() + getRadius();
	}

	public void setStartX(int x) {
		getCenter().setX(x - getRadius());
	}

	public void setStartY(int y) {
		getCenter().setX(y - getRadius());
	}

	public void setEndX(int x) {
		getCenter().setX(x + getRadius());
	}

	public void setEndY(int y) {
		getCenter().setX(y + getRadius());
	}

	public int getCenterX() {
		return getCenter().getX();
	}

	public int getCenterY() {
		return getCenter().getY();
	}

	public void setCenterX(int x) {
		getCenter().setX(x);
	}

	public void setCenterY(int y) {
		getCenter().setY(y);
	}

}
