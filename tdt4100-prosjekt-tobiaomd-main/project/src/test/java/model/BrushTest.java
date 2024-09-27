package model;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BrushTest {

	private Brush brush;

	@BeforeEach
	void setUp() throws Exception {
		brush = new Brush("black", Color.BLACK, 5);
	}

	@Test
	@DisplayName("Test getters")
	void testState() {
		Assertions.assertEquals(brush.getName(), "black");
		Assertions.assertEquals(brush.getColor(), Color.BLACK);
		Assertions.assertEquals(brush.getSize(), 5);
	}

	@Test
	@DisplayName("Test brush converted to text")
	void testToString() {
		final String brushToString = "black,0x000000ff,5\n";
		Assertions.assertEquals(brush.toString(), brushToString);
	}

}
