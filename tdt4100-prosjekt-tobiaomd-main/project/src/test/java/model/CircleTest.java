package model;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class CircleTest {

	private Circle circle;

	@BeforeEach
	void setUp() throws Exception {
		circle = new Circle(new Point(10, 10), 10, Color.BLACK, Color.WHITE, 3);
	}

	@Test
	void testConstructor() {

		Assertions.assertEquals(circle.getCenterX(), 10);
		Assertions.assertEquals(circle.getCenterY(), 10);
		Assertions.assertEquals(circle.getRadius(), 10);
		Assertions.assertEquals(circle.getColor(), Color.BLACK);
		Assertions.assertEquals(circle.getFillColor(), Color.WHITE);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Circle(new Point(110, 110), -10, Color.BLACK, Color.WHITE, 3);
		});

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Circle(new Point(110, 110), 10, Color.BLACK, Color.WHITE, -3);
		});
	}

	@Test
	void testContains() {
		Assertions.assertTrue(circle.contains(new Point(12, 12)));
		Assertions.assertFalse(circle.contains(new Point(0, 100)));
	}

	@Test
	void testSurface() {
		double surfaceOfCircle = Math.PI * Math.pow(10, 2);
		Assertions.assertEquals(circle.surface(), surfaceOfCircle);
	}

	@Test
	void testPerimeter() {
		double perimeterOfCircle = 2 * Math.PI * 10;
		Assertions.assertEquals(circle.perimeter(), perimeterOfCircle);
	}

	@Test
	void testToString() {
		String circleAsString = "Center((x=10 y=10)) Radius=10";
		Assertions.assertEquals(circle.toString(), circleAsString);
	}

	@Test
	void testEqualsObject() {
		Circle comparedCircle = new Circle(new Point(10, 10), 10, Color.BLACK, Color.WHITE, 3);
		Circle comparedCircle2 = new Circle(new Point(10, 10), 12, Color.BLACK, Color.WHITE, 3);

		Assertions.assertTrue(circle.equals(comparedCircle));
		Assertions.assertFalse(circle.equals(comparedCircle2));
	}

	@Test
	void testCompareTo() {
		Circle comparedCircle = new Circle(new Point(10, 10), 8, Color.BLACK, Color.WHITE, 3);
		Assertions.assertTrue(circle.compareTo(comparedCircle) > 0);

		comparedCircle.setRadius(10);
		Assertions.assertTrue(circle.compareTo(comparedCircle) == 0);

		comparedCircle.setRadius(20);
		Assertions.assertTrue(circle.compareTo(comparedCircle) < 0);
	}
}
