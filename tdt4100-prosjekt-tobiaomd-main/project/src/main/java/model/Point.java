package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point extends Shapes {

	private int x;
	private int y;

	public Point(int x, int y, Color color) {
		this(x, y);
		setColor(color);
	}

	public Point(int x, int y) {
		setX(x);
		setY(y);
	}

	public Point() {

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double distance(final Point p) {
		return Math.sqrt((getX() - p.getX()) * (getX() - p.getX()) + (getY() - p.getY()) * (getY() - p.getY()));
	}

	public String toString() {
		return "(x=" + getX() + " y=" + getY() + ")";
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Point) {
			Point temporaryPoint = (Point) obj;
			if (x == temporaryPoint.getX() && y == temporaryPoint.getY())
				return true;
		}
		return false;
	}

	public int compareTo(final Object obj) {
		if (obj instanceof Point) {
			Point temporaryPoint = (Point) obj;
			Point perspectivePoint = new Point(0, 0);
			return (int) (distance(perspectivePoint) - temporaryPoint.distance(perspectivePoint));
		}
		return 0;
	}

	public void paint(final GraphicsContext gc) {
		gc.setFill(getColor());
		gc.fillOval(x - 2, y - 2, 4, 4);
	}

	//not really used -> hard coded with lineWidth = 3 in mind
	@Override
	public boolean contains(final Point pc) {
		if (this.distance(pc) <= 3) {
			return true;
		}
		return false;
	}
}
