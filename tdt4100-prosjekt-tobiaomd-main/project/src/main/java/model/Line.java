package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line extends Shapes {

	private Point start;
	private Point end;

	public Line(Point start, Point end, Color color, double lineWidth) {
		this(start, end);
		setColor(color);
		setLineWidth(lineWidth);
	}

	public Line(Point start, Point end) {
		setStart(start);
		setEnd(end);
	}

	public Line() {
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public double distance() {
		return start.distance(end);
	}

	public Point middle() {
		return new Point((getStartX() + getEndX()) / 2, (getStartY() + getEndY()) / 2);
	}

	public String toString() {
		return "start(" + start + ") end(" + end + ")";
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Line) {
			Line comparedLine = (Line) obj;
			if (start.equals(comparedLine.start) && end.equals(comparedLine.end)) {
				return true;
			}
		}
		return false;
	}

	public int compareTo(final Object obj) {
		if (obj instanceof Line) {
			Line comparedLine = (Line) obj;
			return (int) (this.distance() - comparedLine.distance());
		}
		return 0;
	}

	public void paint(final GraphicsContext gc) {
		gc.setStroke(getColor());
		gc.setLineWidth(getLineWidth());
		gc.strokeLine(getStartX(), getStartY(), getEndX(), getEndY());
	}

	@Override
	public boolean contains(final Point pc) {
		if (start.distance(pc) + end.distance(pc) <= 0.05 + start.distance(end)) {
			return true;
		}
		return false;
	}

	public int getStartX() {
		return getStart().getX();
	}

	public int getStartY() {
		return getStart().getY();
	}

	public int getEndX() {
		return getEnd().getX();
	}

	public int getEndY() {
		return getEnd().getY();
	}

	public void setStartX(int x) {
		getStart().setX(x);
	}

	public void setStartY(int y) {
		getStart().setY(y);
	}

	public void setEndX(int x) {
		getEnd().setX(x);
	}

	public void setEndY(int y) {
		getEnd().setY(y);
	}

}