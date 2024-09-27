package application;

import java.util.NoSuchElementException;
import java.util.Stack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Circle;
import model.Line;
import model.Rectangle;
import model.Shapes;
import model.Square;
import model.Triangle;

public class ChangeState {

	// declares two stacks of shapes undo and redo state
	// using stacks, because they follow the last in, first out principle
	private Stack<Shapes> undoHistory;
	private Stack<Shapes> redoHistory;
	private GraphicsContext graphicsContext;

	// init fields; graphics and stacks
	public ChangeState(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
		undoHistory = new Stack<Shapes>();
		redoHistory = new Stack<Shapes>();
	}

	public Stack<Shapes> getUndoHistory() {
		return undoHistory;
	}

	public Stack<Shapes> getRedoHistory() {
		return redoHistory;
	}

	// makes sure that stacks are not empty when removing elements.
	// if they are empty, they alert the user.
	private boolean ensureUndoHistoryNotEmpty() {
		if (getUndoHistory().size() == 0) {
			Alert stackIsEmptyWarning = new Alert(AlertType.WARNING);
			stackIsEmptyWarning.setTitle("Undo History");
			stackIsEmptyWarning.setHeaderText("Could not find last element");
			stackIsEmptyWarning.setContentText("Could not retrieve last element from undo stack.");
			stackIsEmptyWarning.showAndWait();
			return false;
		}
		return true;
	}

	private boolean ensureRedoHistoryNotEmpty() {
		if (getRedoHistory().size() == 0) {
			Alert stackIsEmptyWarning = new Alert(AlertType.WARNING);
			stackIsEmptyWarning.setTitle("Redo History");
			stackIsEmptyWarning.setHeaderText("Could not find last element");
			stackIsEmptyWarning.setContentText("Could not retrieve last element from redo stack.");
			stackIsEmptyWarning.showAndWait();
			return false;
		}
		return true;
	}

	// returns last element, if there is one -> throws exception and alerts user if
	// not
	private Shapes getUndoShapes() {
		if (!ensureUndoHistoryNotEmpty()) {
			throw new NoSuchElementException("Cannot find last element of empty stack");
		}
		return undoHistory.lastElement();
	}

	private Shapes getRedoShapes() {
		if (!ensureRedoHistoryNotEmpty()) {
			throw new NoSuchElementException("Cannot find last element of empty stack");
		}
		return redoHistory.lastElement();
	}

	// adds shapes
	public void addShapesToUndo(Shapes Shapes) {
		undoHistory.add(Shapes);
	}

	public void addShapesToRedo(Shapes Shapes) {
		redoHistory.add(Shapes);
	}

	// removes shapes -> throws if none to remove and alerts user
	public void removeShapesFromUndo() {
		if (!ensureUndoHistoryNotEmpty()) {
			throw new NoSuchElementException("Cannot remove element from empty stack");
		}
		undoHistory.pop();
	}

	public void removeShapesFromRedo() {
		if (!ensureRedoHistoryNotEmpty()) {
			throw new NoSuchElementException("Cannot remove element from empty stack");
		}
		redoHistory.pop();
	}

	// function that runs upon clicking FXML ToggleButton undo.
	public void undo() {
		// Checks if empty
		if (ensureUndoHistoryNotEmpty()) {
			// clears canvas -> going to redraw shapes -> removes freedrawing, which is a
			// limit to this functionality.
			graphicsContext.clearRect(0, 0, 1080, 790);

			// retrieves last element from stack, checks what instance it is, converts it
			// and adds it to redoStack
			Shapes lastShapeOfUndoHistory = getUndoShapes();
			if (lastShapeOfUndoHistory instanceof Line) {
				Line temporaryLine = (Line) lastShapeOfUndoHistory;
				addShapesToRedo(temporaryLine);
			}

			else if (lastShapeOfUndoHistory instanceof Rectangle) {
				Rectangle temporaryRectangle = (Rectangle) lastShapeOfUndoHistory;
				addShapesToRedo(temporaryRectangle);
			}

			else if (lastShapeOfUndoHistory instanceof Circle) {
				Circle temporaryCircle = (Circle) lastShapeOfUndoHistory;
				addShapesToRedo(temporaryCircle);
			}

			else if (lastShapeOfUndoHistory instanceof Square) {
				Square temporarySquare = (Square) lastShapeOfUndoHistory;
				addShapesToRedo(temporarySquare);
			}

			else if (lastShapeOfUndoHistory instanceof Triangle) {
				Triangle temporaryTriangle = (Triangle) lastShapeOfUndoHistory;
				addShapesToRedo(temporaryTriangle);
			}

			// removes the shape that was added to redo from undo
			removeShapesFromUndo();

			// draws all shapes that are still in undoStack
			for (int i = 0; i < getUndoHistory().size(); i++) {
				Shapes shapeAtPositionI = getUndoHistory().elementAt(i);

				// Checks the instance of the shapes inside undo stack and calls their paint
				// methods
				if (shapeAtPositionI instanceof Line) {
					Line temporaryLine = (Line) shapeAtPositionI;
					temporaryLine.paint(graphicsContext);
				} else if (shapeAtPositionI instanceof Rectangle) {
					Rectangle temporaryRectangle = (Rectangle) shapeAtPositionI;
					temporaryRectangle.paint(graphicsContext);
				} else if (shapeAtPositionI instanceof Circle) {
					Circle temporaryCircle = (Circle) shapeAtPositionI;
					temporaryCircle.paint(graphicsContext);
				} else if (shapeAtPositionI instanceof Square) {
					Square temporarySquare = (Square) shapeAtPositionI;
					temporarySquare.paint(graphicsContext);
				} else if (shapeAtPositionI instanceof Triangle) {
					Triangle temporaryTriangle = (Triangle) shapeAtPositionI;
					temporaryTriangle.paint(graphicsContext);
				}
			}

		}
	}

	// Function that runs upon click FXML ToggleButton redo
	public void redo() {
		// ensures not empty
		if (ensureRedoHistoryNotEmpty()) {

			// grabs last element of redo stack, draws it to graphics context and adds to
			// undo stack
			Shapes lastShapeOfRedoHistory = getRedoShapes();
			if (lastShapeOfRedoHistory instanceof Line) {
				Line temporaryLine = (Line) lastShapeOfRedoHistory;
				temporaryLine.paint(graphicsContext);
				addShapesToUndo(temporaryLine);
			} else if (lastShapeOfRedoHistory instanceof Rectangle) {
				Rectangle temporaryRectangle = (Rectangle) lastShapeOfRedoHistory;
				temporaryRectangle.paint(graphicsContext);
				addShapesToUndo(temporaryRectangle);
			} else if (lastShapeOfRedoHistory instanceof Circle) {
				Circle temporaryCircle = (Circle) lastShapeOfRedoHistory;
				temporaryCircle.paint(graphicsContext);
				addShapesToUndo(temporaryCircle);
			} else if (lastShapeOfRedoHistory instanceof Square) {
				Square temporarySquare = (Square) lastShapeOfRedoHistory;
				temporarySquare.paint(graphicsContext);
				addShapesToUndo(temporarySquare);
			} else if (lastShapeOfRedoHistory instanceof Triangle) {
				Triangle temporaryTriangle = (Triangle) lastShapeOfRedoHistory;
				temporaryTriangle.paint(graphicsContext);
				addShapesToUndo(temporaryTriangle);
			}
			// removes shape from redo after adding to undo
			removeShapesFromRedo();
		}
	}

}