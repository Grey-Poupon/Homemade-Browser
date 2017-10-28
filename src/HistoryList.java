import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryList extends WebsiteList {

	private List<String> localHistory = new ArrayList<String>();
	private int localHistoryPosition = -1;
	private String globalHistoryPath;

	public HistoryList(String path, int selectionMode, int layoutOrientation, MouseListener ml, int vsbPolicy,
			int hsbPolicy, Dimension prefferedSize, String title) {
		super(path, selectionMode, layoutOrientation, ml, vsbPolicy, hsbPolicy, prefferedSize, title);
		this.globalHistoryPath = path;
	}

	public void newLocalHistory() { // create a new local history removing all
									// the urls in front of the current position
									// this is for when a user goes back 
									//  then types in a new URL creating a new branch of local history
		localHistory = localHistory.subList(0, localHistoryPosition + 1);
	}

	public void saveGlobalHistory(String URL) throws IOException {

		this.addToModel(URL); // adds the url to the current model

		// create writers to write to the history file
		FileWriter file = new FileWriter(globalHistoryPath, true);
		BufferedWriter buff = new BufferedWriter(file);

		buff.write("\n" + URL);// add the url to the file
		buff.close();

	}

	// getter and setters

	public int getLocalHistoryPosition() {
		return localHistoryPosition;
	}

	public void setLocalHistoryPosition(int localHistoryPosition) {
		this.localHistoryPosition = localHistoryPosition;
	}

	public int getLocalHistorySize() {
		return localHistory.size();
	}
}
