package simon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Simon extends JPanel implements ActionListener, MouseListener {

	public static Simon simon;

	// public Renderer renderer;

	public static final int WIDTH = 600, HEIGHT = 600;

	public int flashed = 0, dark, ticks, indexPattern;

	public boolean creatingPattern = true;

	public ArrayList<Integer> pattern;

	public Random random;

	private boolean gameOver;

	private Timer timer;

	private int currRecord;

	private File recordFile;

	private Scanner s;

	private FileWriter writer;

	public Simon() {

		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		addMouseListener(this);

		start();

		recordFile = new File("res/record.dat");
		try {
			if (recordFile.exists()) {
				s = new Scanner(recordFile);
				if (s.hasNext()) {
					currRecord = s.nextInt();
				} else {
					currRecord = 0;
				}
			} else {
				recordFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		timer = new Timer(20, this);
		timer.start();
	}

	public void start() {
		random = new Random();
		pattern = new ArrayList<Integer>();
		indexPattern = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (flashed == 1) {
			g2.setColor(Color.GREEN);
		} else {
			g2.setColor(Color.GREEN.darker());
		}

		g2.fillRect(0, 0, WIDTH / 2, HEIGHT / 2);

		if (flashed == 2) {
			g2.setColor(Color.RED);
		} else {
			g2.setColor(Color.RED.darker());
		}

		g2.fillRect(WIDTH / 2, 0, WIDTH / 2, HEIGHT / 2);

		if (flashed == 3) {
			g2.setColor(Color.ORANGE);
		} else {
			g2.setColor(Color.ORANGE.darker());
		}

		g2.fillRect(0, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);

		if (flashed == 4) {
			g2.setColor(Color.BLUE);
		} else {
			g2.setColor(Color.BLUE.darker());
		}

		g2.fillRect(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);

		g2.setColor(Color.BLACK);
		g2.fillRoundRect(215, 218, 150, 150, 80, 80);
		g2.fillRect(WIDTH / 2 - WIDTH / 12, 0, WIDTH / 7, HEIGHT);
		g2.fillRect(0, WIDTH / 2 - WIDTH / 12, WIDTH, HEIGHT / 7);

		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(200));
		g2.drawOval(-100, -100, WIDTH + 200, HEIGHT + 200);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(10));
		g2.drawOval(0, 0, WIDTH, HEIGHT);

		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", 1, 80));

		if (gameOver) {
			g2.drawString(":(", WIDTH / 2 - 45, HEIGHT / 2 + 10);
		} else {
			g2.drawString(indexPattern + "/" + pattern.size(), WIDTH / 2 - 67, HEIGHT / 2 + 22);
		}

	}

	private void update() {
		
		ticks++;

		if (ticks % 20 == 0) {
			flashed = 0;
			if (dark >= 0) {
				dark--;
			}
		}

		if (creatingPattern) {
			if (dark <= 0) {
				if (indexPattern >= pattern.size()) {
					flashed = random.nextInt(40) % 4 + 1;
					pattern.add(flashed);
					indexPattern = 0;
					creatingPattern = false;
				} else {
					flashed = pattern.get(indexPattern);
					indexPattern++;
				}
				dark = 2;
			}
		} else if (indexPattern == pattern.size()) {
			creatingPattern = true;
			indexPattern = 0;
			dark = 2;
		}

		
		if (gameOver) {
			if (pattern.size() - 1 > currRecord) {
				try {
					writer = new FileWriter(recordFile);
					writer.write("" + (pattern.size() - 1));
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Object[] options = { "Sim", "Não" };

				int option = JOptionPane.showOptionDialog(null, "Deseja jogar novamente?", "Novo recorde",
						JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if(option == 0) {
					start();
					gameOver = false;
				} else if(option == 1) {
					System.exit(JFrame.EXIT_ON_CLOSE);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

		int x = e.getX(), y = e.getY();

		if (!creatingPattern && !gameOver) {
			if (x > 0 && x < WIDTH / 2 && y > 0 && y < HEIGHT / 2) {
				flashed = 1;
				ticks = 1;
			} else if (x > WIDTH / 2 && x < WIDTH && y > 0 && y < HEIGHT / 2) {
				flashed = 2;
				ticks = 1;
			} else if (x > 0 && x < WIDTH / 2 && y > HEIGHT / 2 && y < HEIGHT) {
				flashed = 3;
				ticks = 1;
			} else if (x > WIDTH / 2 && x < WIDTH && y > HEIGHT / 2 && y < HEIGHT) {
				flashed = 4;
				ticks = 1;
			}

			if (flashed != 0) {
				if (pattern.get(indexPattern) == flashed) {
					indexPattern++;
				} else {
					gameOver = true;
				}
			}
		} else if (gameOver) {
			start();
			gameOver = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
