package application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.canvas.GraphicsContext;
import model.Line;
import model.Rectangle;

class ChangeStateTest {
	ChangeState changeState;
	GraphicsContext graphicsContext;

	@BeforeEach
	void setUp() throws Exception {
		changeState = new ChangeState(graphicsContext);
	}

	@Test
	void testGetUndoHistory() {
		Assertions.assertTrue(changeState.getUndoHistory().isEmpty());
	}

	@Test
	void testGetRedoHistory() {
		Assertions.assertTrue(changeState.getRedoHistory().isEmpty());
	}

	@Test
	void testAddShapesToUndo() {
		Rectangle rectangle = new Rectangle();
		Line line = new Line();

		changeState.addShapesToUndo(line);
		Assertions.assertTrue(changeState.getUndoHistory().size() == 1);
		changeState.addShapesToUndo(rectangle);
		Assertions.assertTrue(changeState.getUndoHistory().size() == 2);
	}

	@Test
	void testAddShapesToRedo() {
		Rectangle rectangle = new Rectangle();
		Line line = new Line();

		changeState.addShapesToRedo(line);
		Assertions.assertTrue(changeState.getRedoHistory().size() == 1);
		changeState.addShapesToRedo(rectangle);
		Assertions.assertTrue(changeState.getRedoHistory().size() == 2);
	}

	@Test
	void testRemoveShapesToUndo() {
		Line line = new Line();

		changeState.addShapesToUndo(line);
		changeState.addShapesToUndo(line);

		Assertions.assertTrue(changeState.getUndoHistory().size() == 2);
		changeState.removeShapesFromUndo();
		Assertions.assertTrue(changeState.getUndoHistory().size() == 1);
		changeState.removeShapesFromUndo();
		Assertions.assertTrue(changeState.getUndoHistory().size() == 0);

	}

	@Test
	void testRemoveShapesToRedo() {
		Line line = new Line();

		changeState.addShapesToRedo(line);
		changeState.addShapesToRedo(line);

		Assertions.assertTrue(changeState.getRedoHistory().size() == 2);
		changeState.removeShapesFromRedo();
		Assertions.assertTrue(changeState.getRedoHistory().size() == 1);
		changeState.removeShapesFromRedo();
		Assertions.assertTrue(changeState.getRedoHistory().size() == 0);

		// cannot test, because removeShapes calls a method that creates an Alert ->
		// requires testFX
//		Assertions.assertThrows(NoSuchElementException.class, () -> {
//			changeState.removeShapesFromRedo();
//		});
	}

}
