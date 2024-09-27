package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import application.BrushesIO;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Brushes {

	private ToggleGroup group;
	private BrushesIO brushesIO;
	private GraphicsContext graphicsContext;
	private ColorPicker strokeColorPicker;
	private Slider lineWidthSlider;
	private List<String> loadedBrushes;
	private Map<ToggleButton, Brush> brushes;

	public Brushes(ToggleGroup group, GraphicsContext graphicsContext, ColorPicker strokeColorPicker, Slider lineWidthSlider) {
		this.group = group;
		this.graphicsContext = graphicsContext;
		this.strokeColorPicker = strokeColorPicker;
		this.lineWidthSlider = lineWidthSlider;

		brushes = new HashMap<>();
		brushesIO = new BrushesIO("brushes");

		try {
			loadedBrushes = brushesIO.loadFromFile();
		} catch (IOException e) {
			Alert loadingBrushesError = new Alert(AlertType.ERROR);
			loadingBrushesError.setTitle("Load");
			loadingBrushesError.setHeaderText("Loading brushes failed");
			loadingBrushesError.setContentText(e.getMessage());
			loadingBrushesError.showAndWait();
		}

		if (loadedBrushes != null) {
			initBrushesFromTextFile(loadedBrushes);
		}

	}

	public void initBrushesFromTextFile(final List<String> loadedBrushList) {
		if (loadedBrushList.size() > 0) {
			for (final var temporaryBrushAsString : loadedBrushList) {
				final String[] tempBrushInformation = temporaryBrushAsString.split(",");

				final String tempName = tempBrushInformation[0];
				final Color tempColor = Color.web(tempBrushInformation[1]);
				final int tempSize = Integer.parseInt(tempBrushInformation[2]);

				addBrush(new Brush(tempName, tempColor, tempSize));
			}
		}
	}

	private ToggleButton convertBrushToTogglebutton(final Brush brush) {
		final String toggleButtonLabel = brush.getName();
		ToggleButton temporaryToggleButton = new ToggleButton(toggleButtonLabel);
		temporaryToggleButton.setToggleGroup(group);
		temporaryToggleButton.setMinWidth(90);
		temporaryToggleButton.setPrefWidth(100);
		temporaryToggleButton.setOnMouseClicked(e -> {
			strokeColorPicker.setValue(brushes.get(temporaryToggleButton).getColor());
			lineWidthSlider.setValue(brushes.get(temporaryToggleButton).getSize());
			graphicsContext.setLineWidth(brushes.get(temporaryToggleButton).getSize());
			graphicsContext.setStroke(brushes.get(temporaryToggleButton).getColor());
		});
		return temporaryToggleButton;
	}

	public void printBrushes(VBox root) {
		root.getChildren().clear();
		root.setSpacing(10);

		for (final ToggleButton toggleButton : brushes.keySet()) {
			root.getChildren().add(toggleButton);
		}
	}

	private boolean brushesContainsBrush(final Brush brush) {
		final String comparedBrushName = brush.getName();

		for (final var existingBrush : brushes.values()) {
			final String existingBrushName = existingBrush.getName();

			if (existingBrushName.equals(comparedBrushName)) {
				return true;
			}
		}

		return false;
	}

	public void addBrush(final Brush brush) {
		boolean illegalActionHasOccurred = false;
		boolean brushHasNoName = brush.getName().equals("");

		if (brushes.size() > 20) {
			Alert tooManyBrushesWarning = new Alert(AlertType.WARNING);
			tooManyBrushesWarning.setTitle("Brush Amount");
			tooManyBrushesWarning.setContentText("Brush Amount Has Reached The Max.");
			tooManyBrushesWarning.showAndWait();
			illegalActionHasOccurred = true;
		}
		
		if (brushHasNoName) {
			Alert brushNameEmpty = new Alert(AlertType.INFORMATION);
			brushNameEmpty.setTitle("Brush");
			brushNameEmpty.setHeaderText("Select a brush name before adding");
			brushNameEmpty.showAndWait();
		}

		if (!illegalActionHasOccurred && !brushesContainsBrush(brush) && !brushHasNoName) {
			ToggleButton brushEquivalentToggleButton = convertBrushToTogglebutton(brush);
			brushes.put(brushEquivalentToggleButton, brush);
			
			try {
				brushesIO.saveToFile(getBrushesAsString());
			} catch (IOException e) {
				Alert savingBrushesError = new Alert(AlertType.ERROR);
				savingBrushesError.setTitle("Save");
				savingBrushesError.setHeaderText("Saving brushes failed");
				savingBrushesError.setContentText(e.getMessage());
				savingBrushesError.showAndWait();
			}
		}
	}

	// method thats called when clicking remove
	public void removeBrush() {
		boolean buttonIsSelected = false;

		for (final ToggleButton toggleButton : brushes.keySet()) {

			if (toggleButton.isSelected()) {
				buttonIsSelected = true;
				brushes.remove(toggleButton);
				break;
			}
		}

		// Alerts if remove is clicked without togglebutton being selected
		if (!buttonIsSelected) {
			Alert removingBrushesError = new Alert(AlertType.INFORMATION);
			removingBrushesError.setTitle("Brush");
			removingBrushesError.setHeaderText("Select a brush before removing");
			removingBrushesError.showAndWait();
		}

		// tries to save all buttons except removed
		try {
			brushesIO.saveToFile(getBrushesAsString());
		} catch (IOException e) {
			Alert savingBrushesError = new Alert(AlertType.ERROR);
			savingBrushesError.setTitle("Save");
			savingBrushesError.setHeaderText("Saving brushes failed");
			savingBrushesError.setContentText(e.getMessage());
			savingBrushesError.showAndWait();
		}
	}

	public List<Brush> getBrushes() {
		return new ArrayList<Brush>(brushes.values());
	}

	public List<ToggleButton> getToggleButtons() {
		return new ArrayList<ToggleButton>(brushes.keySet());
	}

	public Map<ToggleButton, Brush> getBrushAndToggleButtons() {
		return brushes;
	}

	public List<String> getBrushesAsString() {
		return getBrushes().stream().map(Brush::toString).collect(Collectors.toList());
	}

}
