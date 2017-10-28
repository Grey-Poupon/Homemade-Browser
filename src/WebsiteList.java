import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class WebsiteList {
	private JList<String> list;
	private JScrollPane scroll;
	private String title;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	public WebsiteList(String path,int selectionMode,int layoutOrientation, MouseListener ml ,int vsbPolicy,
            int hsbPolicy, Dimension prefferedSize,String title ){
		
		listModel = addItemsToModel(getAllItems(path)); // populate the model for the list
		this.list = new JList<String>(listModel);			// create the list with the model
	
		
		//initialise settings 
		this.list.setSelectionMode(selectionMode);
		this.list.setLayoutOrientation(layoutOrientation);
		this.list.addMouseListener(ml);
		this.list.setVisible(true);
		this.list.setVisibleRowCount(-1);
		this.title = title;
		
		this.scroll = new JScrollPane (this.list,vsbPolicy,hsbPolicy);
		this.scroll.setPreferredSize(prefferedSize);
		
	}
	
	private DefaultListModel<String> addItemsToModel(String[] items){
		for(String o: items){ // loop through the items, adding each one to the model
			listModel.addElement(o);
		}
		return listModel;
	}
	public void addToModel(String s){ // adds a string to the model
		listModel.addElement(s);
		
	}
	
	public JScrollPane getWebsiteList(){
		return this.scroll;
	}
	
	public void setVisible(boolean vis){
		this.scroll.setVisible(vis);
	}
	public boolean isVisible(){
		return this.scroll.isVisible();
	}
	
	public Object getSelectedValue(){
		return this.list.getSelectedValue();
	}
	
	public String getTitle(){
		return  title;
	}
	
 	private String[] getAllItems(String path) {// get all the items from the specified file
 		
		List<String> itemList = new ArrayList<String>(); // create a list to hold all the items

		
		// create readers
		FileReader file = null;
		BufferedReader buffer = null;
		
		try {
			if(!new File(path).isFile()){	// if there isn't a file at that path location
				File itemFile = new File(path); //  create the file
				itemFile.createNewFile();
			}
			
			//initialise readers;
			file = new FileReader(path);
			buffer = new BufferedReader(file);
						
			
			while(buffer.ready()){ // while there is still lines to read from the file
				itemList.add(buffer.readLine()); // read the lines and add them to the list
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				file.close();
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] itemArray = new String[itemList.size()];	// create an array to hold and return the items

		
		for(int i =0;i<itemList.size();i++){//loop through the list
			itemArray[i] = itemList.get(i);// add each item to the array
		}
		return itemArray;
		 
	}
	}
