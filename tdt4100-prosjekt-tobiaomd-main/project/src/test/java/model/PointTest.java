package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PointTest {
	private Point point;
	private Point testPoint;

	@BeforeEach
	void setUp() throws Exception {
		point = new Point(50, 50);
		testPoint = new Point(100, 100);
	}

	@Test
	void testContains() {
		Point point1 = new Point(100, 50);
		Point point2 = new Point(50, 50);

		assertFalse(point.contains(point1));
		assertTrue(point.contains(point2));
	}

	@Test
	void testDistance() {
		double pointDistance = Math
				.sqrt(Math.pow(testPoint.getX() - point.getX(), 2) + Math.pow(testPoint.getY() - point.getY(), 2));
		assertEquals(point.distance(testPoint), pointDistance);
	}

	@Test
	void testToString() {
		assertEquals(point.toString(), "(x=50 y=50)");
	}

	@Test
	void testEqualsObject() {
		Point point2 = new Point(50, 50);

		assertTrue(point.equals(point2));
		assertFalse(point.equals(testPoint));
	}

	@Test
	void testCompareTo() {
		assertTrue(point.compareTo(testPoint) < 0);
		assertTrue(testPoint.compareTo(point) > 0);
		assertTrue(point.compareTo(point) == 0);
	}

}
