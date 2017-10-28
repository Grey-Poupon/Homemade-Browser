import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class PopOut extends JFrame {

	public PopOut(JScrollPane scroll, String title) {
		//constructor for pop up JFrame
		
		setTitle(title);
		setSize(300, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		getContentPane().add(scroll);
	}
}
