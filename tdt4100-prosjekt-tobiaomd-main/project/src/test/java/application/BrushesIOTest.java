package application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;
import model.Brush;

class BrushesIOTest {
	private BrushesIO brushesIO;
	private Brush brush;

	@BeforeEach
	void setUp() throws Exception {
		brushesIO = new BrushesIO("brushes");
		brush = new Brush("black", Color.BLACK, 3);
	}

	@Test
	void testSaveToFile() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		List<String> listOfBrushesAsString = Arrays.asList(brush.toString());

		brushesIO.saveToFile(listOfBrushesAsString, os);

		String actual = new String(os.toByteArray());
		String expected = brush.toString();

		assertEquals(expected, actual, "Written string representation is not correct.");
	}

	@Test
	void testLoadFromFile() {
		List<String> loadedBrushes = null;

		// clears file, then writes to it, then checks if the loaded List is the same as
		// the written.
		try {
			brushesIO.clearFile();
			brushesIO.saveToFile(Arrays.asList("black,0x000000ff,3\n"));
			loadedBrushes = brushesIO.loadFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assertions.assertEquals(loadedBrushes.get(0).split(",")[0], "black");
		Assertions.assertEquals(loadedBrushes.get(0).split(",")[1], "0x000000ff");
		Assertions.assertEquals(loadedBrushes.get(0).split(",")[2], "3");
	}

}
