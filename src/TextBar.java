import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;

public class TextBar {
	private JTextField textBar = new JTextField();

	public TextBar(Color backgroundColour, Color foregroundColour, Dimension maxSize, Dimension minSize) {
		// constructor for textbar
		
		this.textBar.setBackground(backgroundColour);
		this.textBar.setForeground(foregroundColour);
		this.textBar.setEditable(true);
		this.textBar.setMaximumSize(maxSize);
		this.textBar.setPreferredSize(maxSize);
		this.textBar.setMinimumSize(minSize);
	}

	// getter and setters

	public JTextField getTextBar() {
		return textBar;
	}

	public void setText(String text) {
		textBar.setText(text);
	}

	public String getText() {
		return this.textBar.getText();
	}
}
