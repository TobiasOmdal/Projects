package application;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BrushesIO implements SaveAndLoadData {
	private String filename;
	private static final String BRUSH_EXTENSION = "txt";

	public BrushesIO(String filename) {
		this.filename = filename;
	}

	public void clearFile() throws IOException {
		PrintWriter pw = new PrintWriter(getBrushListPath().toFile());
		pw.write("");
		pw.close();
	}

	private Path getBrushUserFolderPath() {
		return Path.of(System.getProperty("user.dir"), "src", "main", "resources", "brushes");
	}

	private Path getBrushListPath() {
		return getBrushUserFolderPath().resolve(filename + "." + BRUSH_EXTENSION);
	}

	private boolean ensureTodoUserFolder() {
		try {
			Files.createDirectories(getBrushUserFolderPath());
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	@Override
	public void saveToFile(List<String> listOfBrushesAsString, OutputStream out) {
		try (var writer = new PrintWriter(out)) {
			for (final String brush : listOfBrushesAsString) {
				writer.write(brush);
			}
		}
	}

	public void saveToFile(List<String> listOfBrushesAsString) throws IOException {
		var brushListPath = getBrushListPath();
		ensureTodoUserFolder();
		try (var output = new FileOutputStream(brushListPath.toFile())) {
			saveToFile(listOfBrushesAsString, output);
		}
	}

	@Override
	public List<String> loadFromFile() throws IOException {
		List<String> brushesInformation = new ArrayList<>();

		var brushListPath = getBrushListPath();
		ensureTodoUserFolder();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(brushListPath.toFile()))) {
			String brushInformationLine;
			while ((brushInformationLine = bufferedReader.readLine()) != null) {
				brushesInformation.add(brushInformationLine);
			}
		}
		return brushesInformation;
	}

}
