package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class LineTest {
	private Line line;

	@BeforeEach
	void setUp() throws Exception {
		line = new Line(new Point(10, 10), new Point(110, 10), Color.BLACK, 3);
	}

	@Test
	void testContains() {
		Point notContainedPoint = new Point(500, 500);
		Point containedPoint = new Point(50, 10);

		Assertions.assertFalse(line.contains(notContainedPoint));
		Assertions.assertTrue(line.contains(containedPoint));
	}

	@Test
	void testDistance() {
		double lineDistance = Math.sqrt(
				Math.pow(line.getEndX() - line.getStartX(), 2) + (Math.pow(line.getEndY() - line.getStartY(), 2)));
		Assertions.assertEquals(lineDistance, line.distance());
	}

	@Test
	void testMiddle() {
		Point middlePoint = new Point((line.getStartX() + line.getEndX()) / 2, (line.getStartY() + line.getEndY()) / 2);
		Assertions.assertEquals(line.middle(), middlePoint);
	}

	@Test
	void testToString() {
		String lineAsString = "start((x=10 y=10)) end((x=110 y=10))";
		Assertions.assertEquals(line.toString(), lineAsString);
	}

	@Test
	void testEqualsObject() {
		Line comparedCircle = new Line(new Point(10, 10), new Point(110, 10), Color.BLACK, 3);
		Line comparedCircle2 = new Line(new Point(10, 10), new Point(200, 10), Color.BLACK, 3);

		Assertions.assertTrue(line.equals(comparedCircle));
		Assertions.assertFalse(line.equals(comparedCircle2));
	}

	@Test
	void testCompareTo() {
		Line comparedCircle = new Line(new Point(10, 10), new Point(100, 10), Color.BLACK, 3);
		Assertions.assertTrue(line.compareTo(comparedCircle) > 0);

		comparedCircle.setEndX(110);
		Assertions.assertTrue(line.compareTo(comparedCircle) == 0);

		comparedCircle.setEndX(120);
		Assertions.assertTrue(line.compareTo(comparedCircle) < 0);
	}

}
