package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class RectangleTest {
	private Rectangle rectangle;

	@BeforeEach
	void setUp() throws Exception {
		rectangle = new Rectangle(new Point(10, 10), 50, 30, Color.BLACK, Color.WHITE, 3);
	}

	@Test
	void testContains() {
		Point notContainedPoint = new Point(200, 200);
		Point containedPoint = new Point(20, 20);
		Assertions.assertFalse(rectangle.contains(notContainedPoint));
		Assertions.assertTrue(rectangle.contains(containedPoint));
	}

	@Test
	void testSurface() {
		double valueOfSurface = 50 * 30;
		Assertions.assertEquals(rectangle.surface(), valueOfSurface);
	}

	@Test
	void testPerimeter() {
		double valueOfPerimeter = 2 * 50 + 2 * 30;
		Assertions.assertEquals(rectangle.perimeter(), valueOfPerimeter);
	}

	@Test
	void testToString() {
		String string = "UpLeftCorner(x=10 y=10) Length=50 Width=30";
		Assertions.assertEquals(rectangle.toString(), string);
	}

	@Test
	void testDiagonal() {
		Line line = new Line(new Point(10, 10), new Point(60, 40), Color.BLACK, 3);
		Assertions.assertEquals(rectangle.diagonal(), line);
	}

	@Test
	void testEqualsObject() {
		Rectangle rectangle1 = new Rectangle(new Point(10, 10), 50, 30, Color.BLACK, Color.WHITE, 3);
		Rectangle rectangle2 = new Rectangle(new Point(10, 10), 20, 30, Color.BLACK, Color.WHITE, 3);

		Assertions.assertTrue(rectangle.equals(rectangle1));
		Assertions.assertFalse(rectangle.equals(rectangle2));
	}

	@Test
	void testCompareTo() {
		Rectangle rectangle2 = new Rectangle(new Point(10, 10), 20, 30, Color.BLACK, Color.WHITE, 3);

		Assertions.assertTrue(rectangle2.compareTo(rectangle) < 0);
		Assertions.assertTrue(rectangle.compareTo(rectangle2) > 0);
		Assertions.assertTrue(rectangle2.compareTo(rectangle2) == 0);
	}

}
