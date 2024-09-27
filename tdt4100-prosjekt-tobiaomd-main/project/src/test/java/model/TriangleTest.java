package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class TriangleTest {
	private Triangle triangle;

	@BeforeEach
	void setUp() throws Exception {
		Point vertexC = new Point(10, 10);
		Point vertexB = new Point(50, 10);
		Point vertexA = new Point(30, 40);
		triangle = new Triangle(vertexC, vertexB, vertexA, Color.BLACK, Color.WHITE, 3);
	}

	@Test
	void testContains() {
		Point containedPoint = new Point(20, 20);
		Point notContainedPoint = new Point(300, 300);

		Assertions.assertFalse(triangle.contains(notContainedPoint));
		Assertions.assertTrue(triangle.contains(containedPoint));
	}

	@Test
	void testSurface() {
		Line line1 = new Line(triangle.getVertexA(), triangle.getVertexB());
		double triangleSurfaceValue = line1.distance() * line1.middle().distance(triangle.getVertexC()) / 2;

		Assertions.assertEquals(triangle.surface(), triangleSurfaceValue);
	}

	@Test
	void testPerimeter() {
		Line lineAB = new Line(triangle.getVertexA(), triangle.getVertexB());
		Line lineBC = new Line(triangle.getVertexB(), triangle.getVertexC());
		Line lineCA = new Line(triangle.getVertexC(), triangle.getVertexA());

		Assertions.assertEquals(triangle.perimeter(), lineAB.distance() + lineBC.distance() + lineCA.distance());
	}

	@Test
	void testToString() {
		String string = "(x=30 y=40)(x=50 y=10)(x=10 y=10)";
		Assertions.assertEquals(triangle.toString(), string);
	}

}
