package simon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class RecordPanel extends MenuPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage deletar, voltar;

	private int choice = 0;

	private File recordFile;
	
	private int currentRecord;
	
	private Scanner s;
	
	public RecordPanel(MainWindow window) {
		super(window);
		recordFile = new File("res/record.dat");
		
		if(recordFile.exists()) {
			try {
				
				s = new Scanner(recordFile);
				if(s.hasNext()) {
					currentRecord = s.nextInt();					
				} else {
					currentRecord = 0;
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				recordFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			currentRecord = 0;
		}
		
	}
	
	@Override
	protected void loadImages() throws IOException {
		super.loadImages();
		
		deletar = ImageIO.read(new File("res/deletar.png"));
		voltar = ImageIO.read(new File("res/voltar.png"));
	}
	
	@Override
	protected void render(Graphics2D g) {
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(colors[matrixNumbers[l][0]].darker());
		g.fillRect(0, 0, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][1]].darker());
		g.fillRect(WIDTH / 2, 0, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][2]].darker());
		g.fillRect(0, WIDTH / 2, WIDTH / 2, HEIGHT / 2);

		g.setColor(colors[matrixNumbers[l][3]].darker());
		g.fillRect(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);
		
		g.drawImage(deletar, 355, 440, null);
		g.drawImage(voltar, 375, 480, null);
		
		if(choice == 0) {
			g.drawImage(logo, 310, 430, null);
		} else if(choice == 1) {
			g.drawImage(logo, 330, 470, null);
		}

		g.setColor(Color.PINK);
		g.setFont(new Font("Trebuchet MS", Font.BOLD, 34));
		if(currentRecord != 0) {
			g.drawString("Melhor pontuação: "+currentRecord, 150, 250);
		} else {
			g.drawString("(VAZIO)", 240, 250);
		}
		
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			choice--;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			choice++;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(choice == 0) {
				Object[] options = { "Não", "Sim" };
				this.repaint();
				int dialog = JOptionPane.showOptionDialog(null, "Deseja realmente apagar os recordes?", "Atenção!",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (dialog == 1) {
					currentRecord = 0;
					try {
						PrintWriter pw  = new PrintWriter(recordFile);
						pw.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			} else if(choice == 1) {
				toMenu();
			} 
		}

		if (choice > 1) {
			choice = 0;
		} else if (choice < 0) {
			choice = 1;
		}
	}

	private void toMenu() {
		window.remove(window.record);
		window.removeKeyListener(window.record);
		
		MenuPanel menu = new MenuPanel(window);
		window.menu = menu;
		window.add(menu);
		window.pack();
		window.addKeyListener(menu);
		window.requestFocus();
		window.setFocusable(true);
	}
}
