package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class SquareTest {
	private Square square;

	@BeforeEach
	void setUp() throws Exception {
		square = new Square(new Point(10, 10), 100, Color.BLACK, Color.WHITE, 3);
	}

	@Test
	void testContains() {
		Point notContainedPoint = new Point(300, 300);
		Point containedPoint = new Point(50, 50);

		Assertions.assertTrue(square.contains(containedPoint));
		Assertions.assertFalse(square.contains(notContainedPoint));
	}

	@Test
	void testSurface() {
		double surfaceValue = Math.pow(100, 2);

		Assertions.assertEquals(square.surface(), surfaceValue);
	}

	@Test
	void testPerimeter() {
		double perimeterValue = 4 * 100;

		Assertions.assertEquals(square.perimeter(), perimeterValue);
	}

	@Test
	void testToString() {
		String string = "Upper Left Corner (x=10 y=10) Length=100";
		Assertions.assertEquals(square.toString(), string);
	}

	@Test
	void testDiagonal() {
		Line line = new Line(new Point(10, 10), new Point(110, 110), Color.BLACK, 3);
		Assertions.assertEquals(square.diagonal(), line);
	}

	@Test
	void testEqualsObject() {
		Square comparedSquare = new Square(new Point(10, 10), 100, Color.BLACK, Color.WHITE, 3);
		Square comparedSquare2 = new Square(new Point(10, 10), 30, Color.BLACK, Color.WHITE, 3);

		Assertions.assertTrue(square.equals(comparedSquare));
		Assertions.assertFalse(square.equals(comparedSquare2));
	}

	@Test
	void testCompareTo() {
		Square comparedSquare = new Square(new Point(10, 10), 90, Color.BLACK, Color.WHITE, 3);
		Assertions.assertTrue(square.compareTo(comparedSquare) > 0);

		comparedSquare.setLength(100);
		Assertions.assertTrue(square.compareTo(comparedSquare) == 0);

		comparedSquare.setLength(110);
		Assertions.assertTrue(square.compareTo(comparedSquare) < 0);
	}

}
