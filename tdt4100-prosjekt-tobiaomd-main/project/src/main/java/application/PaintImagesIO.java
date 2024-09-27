package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PaintImagesIO {
	// needs stage for dialog, canvas for grabbing drawing/image and
	// graphicsContext for drawing the img to canvas after loading
	private final Stage primaryStage;
	private final Canvas canvas;
	private final GraphicsContext graphicsContext;

	public PaintImagesIO(Stage primaryStage, Canvas canvas, GraphicsContext graphicsContext) {
		this.primaryStage = primaryStage;
		this.canvas = canvas;
		this.graphicsContext = graphicsContext;
	}

	public void saveToFile() throws IOException {
		FileChooser savefile = new FileChooser();
		savefile.setTitle("Save File");

		// opens save dialog
		File file = savefile.showSaveDialog(primaryStage);
		// Inits image from canvas dimensions
		WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		// checks if file is chosen
		if (file != null) {
			// Loads canvas to writableimg
			canvas.snapshot(null, writableImage);
			// creates renderedImage from writable image
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
			// writes to chosen file
			ImageIO.write(renderedImage, "png", file);
		}
	}

	public void loadFromFile() throws FileNotFoundException, IOException {
		FileChooser openFile = new FileChooser();
		openFile.setTitle("Open File");
		// opens dialog
		File file = openFile.showOpenDialog(primaryStage);
		// checks if file is chosen
		if (file != null) {
			// tries to create stream from file -> resources not leaked because of try()
			try (InputStream io = new FileInputStream(file)) {
				Image img = new Image(io);
				// draws img to graphics
				graphicsContext.drawImage(img, 0, 0);
			}
		}
	}
}
