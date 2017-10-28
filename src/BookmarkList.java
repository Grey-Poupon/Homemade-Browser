import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BookmarkList extends WebsiteList {
	private String bookmarkPath;

	public BookmarkList(String path, int selectionMode, int layoutOrientation, MouseListener ml, int vsbPolicy,
			int hsbPolicy, Dimension prefferedSize, String title) {

		// initialise variables
		super(path, selectionMode, layoutOrientation, ml, vsbPolicy, hsbPolicy, prefferedSize, title);
		this.bookmarkPath = path;
	}

	public void newBookmark(String URL) throws IOException { // add a bookmark
		this.addToModel(URL);

		// create writers to write to the bookmark file
		FileWriter file = new FileWriter(bookmarkPath, true);
		BufferedWriter buff = new BufferedWriter(file);

		buff.write("\n" + URL); // add the url to the file
		buff.close();
	}

}
