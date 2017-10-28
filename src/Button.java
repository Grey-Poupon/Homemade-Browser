import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button {
	private JButton button = new JButton();

	
	public Button(String text ,Color foreground, Color background, ActionListener al){ // constructor with colouring options 
		this.button.setText(text);
		this.button.setForeground(foreground);
		this.button.setBackground(background);
		this.button.addActionListener(al);
	}
	
	public Button(String text , ActionListener al){ // basic constructor using default colours
		this.button.setText(text);
		this.button.addActionListener(al);
	}
	
	// getter and setters
	
	public JButton getButton(){
		return this.button;
	}
	public void setText(String text){
		this.button.setText(text);
	}

}
