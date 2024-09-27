package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Square extends Paintable {
	protected int length;
	protected Point corner;

	public Square(Point corner, int length, Color color, Color fillColor, double lineWidth) {
		this(corner, length, color);
		setFillColor(fillColor);
		setLineWidth(lineWidth);
	}

	public Square(Point corner, int length, Color color) {
		this(corner, length);
		setColor(color);
	}

	public Square(Point corner, int length) {
		setLength(length);
		setCorner(corner);
	}

	public Square() {

	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Cannot draw rectangle with negative length");
		}
		this.length = length;
	}

	public int getWidth() {
		return length;
	}

	public Point getCorner() {
		return corner;
	}

	public void setCorner(Point corner) {
		this.corner = corner;
	}

	@Override
	public String toString() {
		return "Upper Left Corner " + corner + " Length=" + length;
	}

	public double surface() {
		return length * length;
	}

	public double perimeter() {
		return 4 * length;
	}

	public Line diagonal() {
		return new Line(corner, new Point(getEndX(), getEndY()));
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Square) {
			Square temporarySquare = (Square) obj;
			if (corner.equals(temporarySquare.getCorner()) && length == temporarySquare.getLength())
				return true;
		}
		return false;
	}

	public int compareTo(final Object obj) {
		if (obj instanceof Square) {
			Square temporarySquare = (Square) obj;
			return (int) (surface() - temporarySquare.surface());
		}
		return 0;
	}

	@Override
	public void fill(final GraphicsContext gc) {
		gc.setFill(getFillColor());
		gc.fillRect(getCorner().getX(), getCorner().getY(), getLength(), getLength());
	}

	@Override
	public void paint(final GraphicsContext gc) {
		fill(gc);
		gc.setLineWidth(getLineWidth());
		gc.setStroke(getColor());
		gc.strokeRect(getStartX(), getStartY(), getLength(), getLength());
	}

	@Override
	public boolean contains(final Point pc) {
		if (getStartX() <= pc.getX() && getEndX() >= pc.getX() && getStartY() <= pc.getY() && getEndY() >= pc.getY()) {
			return true;
		}
		return false;
	}

	public int getStartX() {
		return this.getCorner().getX();
	}

	public int getStartY() {
		return this.getCorner().getY();
	}

	public int getEndX() {
		return this.getCorner().getX() + this.getLength();
	}

	public int getEndY() {
		return this.getCorner().getY() + this.getWidth();
	}

	public void setStartX(int x) {
		getCorner().setX(x);

	}

	public void setStartY(int y) {
		getCorner().setY(y);
	}

	public void setEndX(int x) {
		getCorner().setX(x + getLength());

	}

	public void setEndY(int y) {
		getCorner().setY(+getWidth());

	}

}