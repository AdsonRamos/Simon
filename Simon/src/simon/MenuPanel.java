package simon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements ActionListener, KeyListener {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;

	//private int a, b, c, d;

	protected int l = 0;
	
	private Random r;

	private int count;

	private int menuChoice = 0;

	private Timer timer;

	protected Color colors[] = new Color[4];

	protected int[][] matrixNumbers = { { 0, 1, 2, 3 }, { 0, 1, 3, 2 }, { 0, 2, 1, 3 }, { 0, 2, 3, 1 }, { 0, 3, 1, 2 }, { 0, 3, 2, 1 }, 
	{1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0},
	{2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 1, 0}, {2, 3, 0, 1},
	{3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}};

	protected BufferedImage title, start, records, exit, logo;

	protected MainWindow window;
	
	public MenuPanel(MainWindow window) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		this.window = window;

		colors[matrixNumbers[l][0]] = new Color(0, 102, 0);
		colors[matrixNumbers[l][1]] = new Color(138, 0, 0);
		colors[matrixNumbers[l][2]] = new Color(128, 128, 0);
		colors[matrixNumbers[l][3]] = new Color(0, 0, 132);

		addKeyListener(this);
		setFocusable(true);

		r = new Random();

		try {
			loadImages();
		} catch (IOException e) {
			e.printStackTrace();
		}

		count = 0;

		timer = new Timer(20, this);
		timer.start();
	}

	protected void loadImages() throws IOException {
		title = ImageIO.read(new File("res/simon.png"));
		start = ImageIO.read(new File("res/start.png"));
		records = ImageIO.read(new File("res/records.png"));
		exit = ImageIO.read(new File("res/exit.png"));
		logo = ImageIO.read(new File("res/ico.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}

	protected void update() {
		count++;
		if (count % 50 == 0) {
			l = r.nextInt(24);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	protected void render(Graphics2D g) {
		g.setColor(colors[matrixNumbers[l][0]].darker());
		g.fillRect(0, 0, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][1]].darker());
		g.fillRect(WIDTH / 2, 0, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][2]].darker());
		g.fillRect(0, WIDTH / 2, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][3]].darker());
		g.fillRect(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);

		g.drawImage(title, 153, 35, null);

		g.drawImage(start, 228, 244, null);
		g.drawImage(records, 203, 284, null);
		g.drawImage(exit, 243, 324, null);

		if (menuChoice == 0) {
			g.drawImage(logo, 185, 236, null);
		} else if (menuChoice == 1) {
			g.drawImage(logo, 160, 276, null);
		} else if (menuChoice == 2) {
			g.drawImage(logo, 200, 316, null);
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			menuChoice--;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			menuChoice++;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(menuChoice == 0) {
				startGame();
			} else if(menuChoice == 1) {
				toRecords();
			} else if (menuChoice == 2) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		}

		if (menuChoice > 2) {
			menuChoice = 0;
		} else if (menuChoice < 0) {
			menuChoice = 2;
		}
	}

	private void toRecords() {
		window.removeKeyListener(window.menu);
		window.remove(window.menu);
		
		RecordPanel record = new RecordPanel(window);
		
		window.record = record;
		window.addKeyListener(record);
		window.add(record);
		window.pack();
	}

	private void startGame() {
		window.removeKeyListener(window.menu);
		window.remove(window.menu);
		
		Simon simon = new Simon();
		
		window.simon = simon;
		window.setContentPane(simon);
		window.pack();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
