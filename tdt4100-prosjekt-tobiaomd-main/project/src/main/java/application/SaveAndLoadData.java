package application;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SaveAndLoadData {
	public void saveToFile(List<String> listOfBrushesAsString, OutputStream out) throws IOException;
	public List<String> loadFromFile() throws IOException;
}
