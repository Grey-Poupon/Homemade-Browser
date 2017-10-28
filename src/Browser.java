import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Browser extends JFrame {
	
	private JEditorPane pane	   = new JEditorPane();
	private	JScrollPane	scrollPane = new JScrollPane(pane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private FlowLayout 	layout 	   = new FlowLayout();
	private String 		homeURL    = "https://www.w3.org/";
	
	private List<String> localHistory = new ArrayList<String>();
	private int localHistoryPosition = -1;
	
	private String globalHistoryPath 	= "GlobalHistory";
	private String bookmarkPath 		= "Bookmarks";
	private String configPath 			= "Config";
	private String tempPath 			= "Temp";
	
	private TextBar textBar = new TextBar(Color.WHITE,Color.BLACK,new Dimension(200, 20),new Dimension(100, 50));
	private Button goButton = new Button("GO",Color.WHITE, Color.RED, new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (historyList.getLocalHistoryPosition() != historyList.getLocalHistorySize() - 1) {// if we're not at the front of our local history list
				historyList.newLocalHistory(); // start a new history branch 
			}
			setupPage(textBar.getText(), "Go");	// setup the page using the url in the textbar
		}
	});
	private Button backButton  = new Button("Back",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (localHistoryPosition > 0) { // if we're not at the start of the history
				setupPage(localHistory.get(localHistoryPosition - 1), "Back");// set up page with the previous url
			}
		}
	});
	private Button forwardButton  = new Button("Forward",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (localHistoryPosition < localHistory.size() - 1) { // if we're not at the end 
				setupPage(localHistory.get(localHistoryPosition + 1), "Forward");// set up page with the next url
			}
		}
	});
	private Button refreshButton  = new Button("Refresh",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
				setupPage(localHistory.get(localHistoryPosition), "Go"); // setup the page with the current url
		}
	});
	private Button bookmarkButton  = new Button("Bookmark",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				bookmarkList.newBookmark(localHistory.get(localHistoryPosition)); //try to add a bookmark with the current pages url
			} catch (IOException e) {
				System.out.println(e.getMessage());
				}
		}
	});
	private Button openBookmarksButton  = new Button("Open Bookmarks",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			bookmarkPopOut = new PopOut(bookmarkList.getWebsiteList(),bookmarkList.getTitle());	// create a new window with bookmarks in it	
		}
	});
	private Button openHistoryButton  = new Button("Open History",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			historyPopOut = new PopOut(historyList.getWebsiteList(),historyList.getTitle()); //  create a new window with history in it
		}
	});
	private Button setHomePageButton  = new Button("Set Home",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				setHomePage(localHistory.get(localHistoryPosition)); // set the home page as the current pages url
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	});
	private Button homePageButton  = new Button("Home",new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setupPage(homeURL,"Go"); // setup page with the homepage url
		}
	});

	private HistoryList historyList;
	private PopOut historyPopOut;
	private PopOut bookmarkPopOut;
	
	private BookmarkList bookmarkList;
	
	public static void main(String[] args) {
		Browser browser = new Browser();	// create a browser
		browser.setup(browser);				// set up the browser
		browser.setupPage(browser.homeURL, "Go"); // go to the homepage
	}
	
	
	private void setup(Browser browser) {
		
		browser.setTitle("Browser");	// initialise the settings for the Jframe
		browser.setSize(1000, 800);
		browser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		browser.setVisible(true);
		browser.getContentPane().add(scrollPane);
		
		
		homeURL = getHomePage(); // gets the home page
		
		pane.setEditable(false);
		pane.setLayout(layout);
		pane.addHyperlinkListener(new HyperlinkListener(){

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)){
					setupPage(e.getURL().toString(), "GO");
				}
			}
			
		});
		
		MouseListener historyMouseListener = new MouseListener() {// mouse listener for the history list 
			@Override
			public void mouseClicked(MouseEvent e) {
				setupPage((String)historyList.getSelectedValue(), "Go");// set up the page with the url the user clicked
				historyPopOut.dispose();// remove the history list
			}
			@Override
			public void mouseEntered(MouseEvent e) {				
			}
			@Override
			public void mouseExited(MouseEvent e) {				
			}
			@Override
			public void mousePressed(MouseEvent e) {				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {				
			}
		};
		MouseListener bookmarkMouseListener = new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				setupPage((String)bookmarkList.getSelectedValue(), "Go");// set up the page with the url the user clicked
				bookmarkPopOut.dispose();// remove the bookmark list
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}			
		};
		
		
		// create the history and bookmark lists
		historyList = new HistoryList(globalHistoryPath,ListSelectionModel.SINGLE_SELECTION,JList.VERTICAL,historyMouseListener, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,new Dimension(200,400), "History");
		bookmarkList = new BookmarkList(bookmarkPath,ListSelectionModel.SINGLE_SELECTION,JList.VERTICAL,bookmarkMouseListener, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED,new Dimension(200,400), "Bookmarks");
			
		// add all the elements into the panes flowlayout
		pane.add(goButton.getButton());
		pane.add(textBar.getTextBar());
		pane.add(backButton.getButton());
		pane.add(forwardButton.getButton());
		pane.add(refreshButton.getButton());
		pane.add(bookmarkButton.getButton());
		pane.add(homePageButton.getButton());
		pane.add(setHomePageButton.getButton());
		pane.add(openBookmarksButton.getButton());
		pane.add(openHistoryButton.getButton());
	}
	
	private void setupPage(String URL, String button) { // set up a page with the given URL
		try {
			pane.setPage(URL); // set up page with url
						
			if (button == "Go") { // if the go button was pressed add this to the local and global history
				historyList.saveGlobalHistory(URL);
				saveLocalHistory(URL);
			}
			
			if (button == "Go" || button == "Forward") {// if the go or forward button was pressed, increment the local history position
				localHistoryPosition++;
			}
			
			if (button == "Back") {// if the back button was pressed, decrement the local history position
				localHistoryPosition--;
			}
			textBar.setText(URL); // set the text in the text bar to the url the page is being set to
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void saveLocalHistory(String URL) { // add the url to the local history
		localHistory.add(URL);
	}

	private void setHomePage(String URL) throws IOException{ // change the homepage to the url given
		homeURL = URL; // set the local instance of the homepage to the url
		
		
		//initialise variables
		FileWriter fileWrite = new FileWriter(tempPath, true);
		BufferedWriter bufferWrite = new BufferedWriter(fileWrite);
		 
		
		FileReader 		fileRead	= null;
		BufferedReader 	bufferRead 	= null;
		PrintWriter 	printWrite	= null;
		
	
		
		File bookmarkFile = new File(tempPath);
		if(!bookmarkFile.isFile()){// create file if its not there
			bookmarkFile.createNewFile();
			}
		File configFile = new File(configPath);
		if(!configFile.isFile()){// create file if its not there
			configFile.createNewFile();
			}
		
		
		try {
			// Initialise readers to config and writer to a temporary file
			printWrite = new PrintWriter(new File(tempPath));
			fileRead = new FileReader(configPath);
			bufferRead = new BufferedReader(fileRead);

			
		printWrite.println(URL); // write the url to it
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fileRead.close();
				bufferRead.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		fileWrite.close();
		bufferWrite.close();
		printWrite.close();
		
		new File(configPath).delete();// delete config file
		bookmarkFile.renameTo(new File(configPath));// rename temp file to make it the config file

	}

	private String getHomePage(){
		String homePage = "https://www.w3.org/"; // default homepage
		FileReader file = null;
		BufferedReader buffer = null;
		
		try {
			if(!new File(configPath).isFile()){
				File bookmarkFile = new File(configPath);
				bookmarkFile.createNewFile();
			}
			file = new FileReader(configPath);
			buffer = new BufferedReader(file);
						
			if(buffer.ready()){
				homePage = (buffer.readLine());
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				file.close();
				buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return homePage;
	}
}
