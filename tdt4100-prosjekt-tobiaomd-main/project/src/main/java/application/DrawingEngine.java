package application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import model.Circle;
import model.Line;
import model.Paintable;
import model.Point;
import model.Rectangle;
import model.Shapes;
import model.Square;
import model.Triangle;

//class for drawing shapes, freedrawing and erasing
public class DrawingEngine {
	// class uses canvas for clearing screen, graphicscontext for drawing
	// and changeState for adding the shapes to the undo stack when they are drawn
	private Canvas canvas;
	private GraphicsContext graphicsContext;
	private ChangeState changeState;

	public DrawingEngine(GraphicsContext graphicsContext, Canvas canvas, ChangeState changeState) {
		this.changeState = changeState;
		this.graphicsContext = graphicsContext;
		this.canvas = canvas;
	}

	// sets background as the hex of white and clears the undo and redo stacks
	public void clear() {
		Color tempColor = (Color)graphicsContext.getFill();
		Color color = Color.web("#FFFFFF");
		graphicsContext.setFill(color);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		changeState.getRedoHistory().clear();
		changeState.getUndoHistory().clear();
		graphicsContext.setFill(tempColor);
	}

	// initializes the shapes upon clicking -> sets color, starting position for all
	public void initShape(final Shapes shape, final Point position) {
		shape.setColor((Color) graphicsContext.getStroke());
		shape.setLineWidth(graphicsContext.getLineWidth());

		// if Shape is an instance of paintable (fillable shapes) I set fill color.
		if (shape instanceof Paintable) {
			Paintable paintable = (Paintable) shape;
			paintable.setFillColor((Color) graphicsContext.getFill());
		}

		if (shape instanceof Line) {
			Line line = (Line) shape;
			line.setStart(position);
		}

		else if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			rectangle.setCorner(position);
		}

		else if (shape instanceof Square) {
			Square square = (Square) shape;
			square.setCorner(position);
		}

		else if (shape instanceof Circle) {
			Circle circle = (Circle) shape;
			circle.setCenter(position);
		}

		// if it is a triangle we draw it by clicking three times, instead of dragging.
		// this is because it is easier to set the vertices and drawing lines between
		// them
		else if (shape instanceof Triangle) {
			Triangle triangle = (Triangle) shape;
			// getCount checks how many times the screen is clicked -> if 3 we draw the
			// triangle
			if (triangle.getCount() == 0) {
				triangle.setVertexA(position);
				triangle.setCount(1);
			} else if (triangle.getCount() == 1) {
				triangle.setVertexB(position);
				triangle.setCount(2);
			} else if (triangle.getCount() == 2) {
				triangle.setVertexC(position);
				triangle.paint(graphicsContext);
				triangle.setCount(0);
				// adds shape to undo stack
				changeState.addShapesToUndo(new Triangle(triangle.getVertexA(), triangle.getVertexB(),
						triangle.getVertexC(), triangle.getColor(), triangle.getFillColor(), triangle.getLineWidth()));
			}
		}
	}

	// draws the shapes -> after the mouse is released
	public void drawShape(final Shapes shape, final Point position) {
		// gets position X and Y
		final int positionX = position.getX();
		final int positionY = position.getY();

		// sets end position for shapes and draws them from existing attributes
		if (shape instanceof Line) {
			Line line = (Line) shape;
			line.setEnd(position);
			line.paint(graphicsContext);
			changeState.addShapesToUndo(new Line(line.getStart(), line.getEnd(), line.getColor(), line.getLineWidth()));
		}

		else if (shape instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) shape;
			rectangle.setLength((int) Math.abs((positionX - rectangle.getStartX())));
			rectangle.setWidth((int) Math.abs((positionY - rectangle.getStartY())));

			// Prevents the rectangle from inverting if drawn from bottom right to top left
			if (rectangle.getStartX() > positionX) {
				rectangle.setStartX(positionX);
			}

			if (rectangle.getStartY() > positionY) {
				rectangle.setStartY(positionY);
			}
			rectangle.paint(graphicsContext);

			changeState.addShapesToUndo(new Rectangle(rectangle.getCorner(), rectangle.getLength(),
					rectangle.getWidth(), rectangle.getColor(), rectangle.getFillColor(), rectangle.getLineWidth()));
		}

		else if (shape instanceof Square) {
			Square square = (Square) shape;
			//find if change in y or change in x is greater
			square.setLength(Math.max(Math.abs((positionX - square.getStartX())), Math.abs(positionY - square.getStartY())));

			if (square.getStartX() > positionX) {
				square.setStartX(positionX);
			}

			if (square.getStartY() > positionY) {
				square.setStartY(positionY);
			}

			square.paint(graphicsContext);

			changeState.addShapesToUndo(new Square(square.getCorner(), square.getLength(), square.getColor(),
					square.getFillColor(), square.getLineWidth()));
		}

		else if (shape instanceof Circle) {
			Circle circle = (Circle) shape;
			circle.setRadius(
					(int) ((Math.abs(positionX - circle.getCenterX()) + Math.abs(positionY - circle.getCenterY()))
							/ 2));

			if (circle.getCenterX() > positionX) {
				circle.setCenterX(positionX);
			}
			if (circle.getCenterY() > positionY) {
				circle.setCenterY(positionY);
			}

			circle.paint(graphicsContext);

			changeState.addShapesToUndo(new Circle(circle.getCenter(), circle.getRadius(), circle.getColor(),
					circle.getFillColor(), circle.getLineWidth()));
		}
	}

	// free draw when clicking
	public void freeDrawInit(final Color color, final Point position, final double lineWidth) {
		graphicsContext.setLineWidth(lineWidth);
		graphicsContext.setStroke(color);
		graphicsContext.beginPath();
		graphicsContext.lineTo(position.getX(), position.getY());
	}

	// free draw when dragging
	public void freeDraw(final Point position) {
		graphicsContext.lineTo(position.getX(), position.getY());
		graphicsContext.stroke();
	}

	// free draw upon mouse released
	public void freeDrawClose(final Point position) {
		graphicsContext.lineTo(position.getX(), position.getY());
		graphicsContext.stroke();
		graphicsContext.closePath();
	}

	// clears in a smaller scale
	public void erase(final Point position, final double lineWidth) {
		graphicsContext.clearRect(position.getX() - lineWidth / 2, position.getY() - lineWidth / 2, lineWidth,
				lineWidth);
	}

}
