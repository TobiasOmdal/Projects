package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.ChangeState;
import application.DrawingEngine;
import application.PaintImagesIO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Brush;
import model.Brushes;
import model.Circle;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Square;
import model.Triangle;

public class AppController implements Initializable {

	// FXML elements
	@FXML
	private Canvas canvas;

	@FXML
	private ToggleButton drowbtn, rubberbtn, linebtn, rectbtn, squabtn, circlebtn, triabtn, textbtn;

	@FXML
	private TextArea text;

	@FXML
	private Button undo, redo, open, save;

	@FXML
	private ColorPicker cpLine, cpFill;

	@FXML
	private Label line_color, fill_color, line_width;

	@FXML
	private Slider slider;

	@FXML
	private VBox brushBox;

	@FXML
	private TextField brushName;

	@FXML
	private BorderPane borderPane;

	// Shapes
	private Line line;
	private Rectangle rect;
	private Circle circ;
	private Triangle tria;
	private Square squa;

	// Classes that change state of program
	private GraphicsContext graphicsContext;
	private ChangeState changeState;
	private PaintImagesIO img;
	private Brushes brushes;
	private DrawingEngine drawingEngine;

	// Fields to prevent declaring multiple variables that are the same
	private Color color;
	private Color fillColor;
	private Point point;
	private double lineWidth;

	// initializing shapes
	public AppController() {
		line = new Line();
		rect = new Rectangle();
		circ = new Circle();
		tria = new Triangle();
		squa = new Square();
	}

	// initializing fields and classes that use FXML
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setLineWidth(1);
		graphicsContext.setFill(Color.web("#00000000"));
		graphicsContext.setLineWidth(slider.getValue());
		changeState = new ChangeState(graphicsContext);
		brushes = new Brushes(drowbtn.getToggleGroup(), graphicsContext, cpLine, slider);
		drawingEngine = new DrawingEngine(graphicsContext, canvas, changeState);
		brushes.printBrushes(brushBox);

		handleSlider();
		drawEventHandler();

	}

	// Adds brush to List of brushes and draws the
	// corresponding ToggleButton to gui
	@FXML
	public void addBrush() {
		brushes.addBrush(new Brush(brushName.getText(), (Color) graphicsContext.getStroke(),
				(int) graphicsContext.getLineWidth()));
		brushes.printBrushes(brushBox);
	}

	// removes brush thats selected
	@FXML
	public void removeBrush() {
		brushes.removeBrush();
		brushes.printBrushes(brushBox);
	}

	@FXML
	public void setLineColor() {
		graphicsContext.setStroke(cpLine.getValue());
	}

	@FXML
	public void setFillColor() {
		graphicsContext.setFill(cpFill.getValue());
	}

	// Changes lineWidth of graphicsContext and modifies
	// slider text depending on lineWidth
	private void handleSlider() {
		slider.valueProperty().addListener(e -> {
			double width = slider.getValue();
			if (textbtn.isSelected()) {
				graphicsContext.setLineWidth(1);
				graphicsContext.setFont(Font.font(slider.getValue()));
				line_width.setText(String.format("%.1f", width));
				return;
			}
			line_width.setText(String.format("%.1f", width));
			graphicsContext.setLineWidth(width);
		});
	}

	@FXML
	public void clearIsClicked() {
		drawingEngine.clear();
	}

	@FXML
	public void undoIsClicked() {
		changeState.undo();
	}

	@FXML
	public void redoIsClicked() {
		changeState.redo();
	}

	private void drawEventHandler() {

		canvas.setOnMousePressed(e -> {

			color = cpLine.getValue();
			fillColor = cpFill.getValue();
			lineWidth = slider.getValue();

			point = new Point((int) e.getX(), (int) e.getY());

			if (drowbtn.isSelected()) {
				drawingEngine.freeDrawInit(color, point, lineWidth);
			} else if (rubberbtn.isSelected()) {
				drawingEngine.erase(point, lineWidth);
			} else if (linebtn.isSelected()) {
				drawingEngine.initShape(line, point);
			} else if (rectbtn.isSelected()) {
				drawingEngine.initShape(rect, point);
			} else if (squabtn.isSelected()) {
				drawingEngine.initShape(squa, point);
			} else if (circlebtn.isSelected()) {
				drawingEngine.initShape(circ, point);
			} else if (triabtn.isSelected()) {
				drawingEngine.initShape(tria, point);
			} else if (textbtn.isSelected()) {
				graphicsContext.setStroke(color);
				graphicsContext.setFill(fillColor);
				graphicsContext.fillText(text.getText(), point.getX(), point.getY());
				graphicsContext.strokeText(text.getText(), point.getX(), point.getY());
			}
		});

		canvas.setOnMouseDragged(e -> {
			int positionX = (int) e.getX();
			int positionY = (int) e.getY();
			point = new Point(positionX, positionY);

			if (drowbtn.isSelected()) {
				drawingEngine.freeDraw(point);
			} else if (rubberbtn.isSelected()) {
				drawingEngine.erase(point, lineWidth);
			}
		});

		canvas.setOnMouseReleased(e -> {
			int positionX = (int) e.getX();
			int positionY = (int) e.getY();
			point = new Point(positionX, positionY);

			if (drowbtn.isSelected()) {
				drawingEngine.freeDrawClose(point);
			} else if (rubberbtn.isSelected()) {
				drawingEngine.erase(point, lineWidth);
			} else if (linebtn.isSelected()) {
				drawingEngine.drawShape(line, point);
			} else if (rectbtn.isSelected()) {
				drawingEngine.drawShape(rect, point);
			} else if (circlebtn.isSelected()) {
				drawingEngine.drawShape(circ, point);
			} else if (squabtn.isSelected()) {
				drawingEngine.drawShape(squa, point);
			}
		});

	}

	@FXML
	public void openIsClicked() {
		// Initializes img if its null -> couldnt initialize before, because
		// the stage (primaryStage) wasnt initialized yet.
		if (img == null) {
			img = new PaintImagesIO((Stage) canvas.getScene().getWindow(), canvas, graphicsContext);
		}
		// tries to load img
		try {
			img.loadFromFile();
			// alerts user if file isnt found
		} catch (FileNotFoundException e) {
			Alert fileNotFoundAlert = new Alert(AlertType.ERROR);
			fileNotFoundAlert.setTitle("Load Failed.");
			fileNotFoundAlert.setContentText(e.getMessage());
			fileNotFoundAlert.showAndWait();
			// alerts if other IOException is thrown
		} catch (IOException e) {
			Alert IOFailAlert = new Alert(AlertType.ERROR);
			IOFailAlert.setTitle("Load Failed.");
			IOFailAlert.setContentText(e.getMessage());
			IOFailAlert.showAndWait();
		}
	}

	@FXML
	public void saveIsClicked() {
		if (img == null) {
			img = new PaintImagesIO((Stage) canvas.getScene().getWindow(), canvas, graphicsContext);
		}
		// saves img
		try {
			img.saveToFile();
			// Alerts user if exception is thrown
		} catch (IOException e) {
			Alert IOFailAlert = new Alert(AlertType.ERROR);
			IOFailAlert.setTitle("Save Failed.");
			IOFailAlert.setContentText(e.getMessage());
			IOFailAlert.showAndWait();
		}
	}

}
