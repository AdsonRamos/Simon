package simon;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	protected MenuPanel menu;
	protected Simon simon;
	protected RecordPanel record;
	
	public MainWindow() {
		super("Simon");
		
		menu = new MenuPanel(this);
		record = new RecordPanel(this);
		
		add(menu);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(MenuPanel.WIDTH, MenuPanel.HEIGHT);
		pack();
		setLocationRelativeTo(null);
		
		try {
			setIconImage(ImageIO.read(new File("res/ico.png")));
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new MainWindow();
	}
}
