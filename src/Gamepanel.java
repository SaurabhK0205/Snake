import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.font.TextLayout;

import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class Gamepanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 800;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 80;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 1;
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction = 'D';
	boolean running = true;
	Timer timer;
	Random random;
	
	Gamepanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startgame();
	}
	/*public void welcomeScreen(Graphics g) {	
		g.setColor(Color.yellow);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("WELCOME TO DRAKE", (SCREEN_WIDTH - metrics.stringWidth("WELCOME TO DRAKE"))/2, 
				SCREEN_HEIGHT/2);
		startgame();
	}*/
	public void startgame() {
		appleSpawn();
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		/*g.setFont(g.getFont().deriveFont(75F));
		int currentheight = SCREEN_HEIGHT/3;
		final var g2D = (Graphics2D) g;
		final var frc = g2D.getFontRenderContext();
		final String message = "Welcome \nto \nDrake \nPress spacebar to continue";
		for (final var line : message.split("\n")) {
			final var layout = new TextLayout(line, g.getFont(), frc);
			final var bounds = layout.getBounds();
			final var targetwidth = (float) (SCREEN_WIDTH - bounds.getWidth())/2;
			layout.draw(g2D, targetwidth, currentheight);
			currentheight += g.getFontMetrics().getHeight();
		}*/
		if(running){
			/*for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
				}*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			for(int i=0; i<bodyParts; i++) {
				if(i==0) {
					g.setColor(new Color(255,204,153));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(153,76,0));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
			g.setColor(Color.white);
			g.setFont(new Font("Italic",Font.BOLD, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:"+ applesEaten))/2, g.getFont().getSize());
			}
		else {
			gameOver(g);
		}
		}
	public void appleSpawn() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for (int i=bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			appleSpawn();
		}
	}
	public void checkCollisions() {	
		for(int i=bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//for left border
		if(x[0]<0) {
			running = false;
		}
		//for right border
		if(x[0]>SCREEN_WIDTH) {
			running = false;
		}
		//for top border;
		if(y[0]<0) {
			running = false;
		}
		//for bottom border
		if(y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer = new Timer(DELAY,this);
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.yellow);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, 
				SCREEN_HEIGHT/2);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
					}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			
			
		}
	}

}
