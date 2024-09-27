package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Paintable {

	private Point vertexC;
	private Point vertexB;
	private Point vertexA;
	private int count = 0;

	public Triangle(Point vertexC, Point vertexB, Point vertexA, Color color, Color fillColor, double lineWidth) {
		this(vertexC, vertexB, vertexA, color, fillColor);
		setLineWidth(lineWidth);
	}

	public Triangle(Point vertexC, Point vertexB, Point vertexA, Color color, Color fillColor) {
		this(vertexC, vertexB, vertexA, color);
		setFillColor(fillColor);
	}

	public Triangle(Point vertexC, Point vertexB, Point vertexA, Color color) {
		this(vertexC, vertexB, vertexA);
		setColor(color);
	}

	public Triangle(Point vertexC, Point vertexB, Point vertexA) {
		setVertexA(vertexA);
		setVertexB(vertexB);
		setVertexC(vertexC);
	}

	public Triangle() {

	}

	public Point getVertexB() {
		return vertexB;
	}

	public void setVertexB(Point vertexB) {
		this.vertexB = vertexB;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Point getVertexC() {
		return vertexC;
	}

	public void setVertexC(Point vertexC) {
		this.vertexC = vertexC;
	}

	public Point getVertexA() {
		return vertexA;
	}

	public void setVertexA(Point vertexA) {
		this.vertexA = vertexA;
	}

	@Override
	public String toString() {
		return "" + vertexA + vertexB + vertexC;
	}

	@Override
	public double surface() {
		Line line1 = new Line(vertexA, vertexB);
		return line1.distance() * line1.middle().distance(vertexC) / 2;
	}

	@Override
	public double perimeter() {
		return (vertexA.distance(vertexB) + vertexB.distance(vertexC) + vertexC.distance(vertexA));
	}

	@Override
	public void fill(final GraphicsContext gc) {
		gc.setFill(getFillColor());
		double[] xCords = new double[] { vertexA.getX(), vertexB.getX(), vertexC.getX() };
		double[] yCords = new double[] { vertexA.getY(), vertexB.getY(), vertexC.getY() };
		gc.fillPolygon(xCords, yCords, 3);
	}

	@Override
	public void paint(final GraphicsContext gc) {
		fill(gc);
		gc.setLineWidth(getLineWidth());
		gc.setStroke(getColor());
		double[] xCords = new double[] { vertexA.getX(), vertexB.getX(), vertexC.getX() };
		double[] yCords = new double[] { vertexA.getY(), vertexB.getY(), vertexC.getY() };
		gc.strokePolygon(xCords, yCords, 3);
	}

	@Override
	public boolean contains(final Point pc) {
		return PointInTriangle(pc, vertexA, vertexB, vertexC);
	}

	// signed distance function -> if point is inside, the distance is negative.
	private float sign(final Point p1, final Point p2, final Point p3) {
		return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
	}

	// Uses the sign method to determine if point pt is inside triangle.
	private boolean PointInTriangle(final Point pt, final Point v1, final Point v2, final Point v3) {
		boolean b1, b2, b3;

		b1 = sign(pt, v1, v2) < 0.0f;
		b2 = sign(pt, v2, v3) < 0.0f;
		b3 = sign(pt, v3, v1) < 0.0f;

		return ((b1 == b2) && (b2 == b3));
	}
}
