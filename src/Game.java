import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean startGame = false;
	private boolean gameOver = false;
	private boolean levelPassed = false;
	private boolean gameWin = false;
	private Timer time;
	private double timeStartGame = 0;
	private double timePassed = 0;
	private int score = 0;
	private int nbBricks = 0;
	private int level = 1;
	
	private int ballXpos = Main.width / 2 - 10;
	private int ballYpos = Main.height - 200;
	private int ballXdir = 1;
	private int ballYdir = -2;
	
	private int barreXpos;
	private int barreYpos;
	private int barreWidth = 100;
	
	private int maxNbImages = 25;
	private BufferedImage imgHentaiInit;
	private BufferedImage imgHentaiBlurry;
	private Bricks bicks;	
	
	Game() {
		this.setBackground(Color.black);
		barreXpos = Main.width / 2 - barreWidth/2 -8;
		barreYpos = Main.height - 120;
		
		time = new Timer(10, this);
		time.start();
		
		addKeyListener(this);
		setFocusable(true);
		
		bicks = new Bricks(2+level,6);
		nbBricks = bicks.map.length * bicks.map[1].length;		
		getNewHentaiImage();
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);	
		
		if(imgHentaiBlurry != null) {
			g.drawImage(imgHentaiBlurry, -8,-20, 600, 800, this);			
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		if(gameOver) {
			g.drawString("...GAMEOVER...", Main.width/2 - 120, 250);
			g.drawString("Press 'enter' to restart !", Main.width/2 -  180, 290);
			
			g.setColor(Color.red);
			g.drawString("...GAMEOVER...", Main.width/2 - 120 - 2, 250-2);
			g.drawString("Press 'enter' to restart !", Main.width/2 -  180-2, 290-2);
		}		
		
		if(levelPassed) {
			g.drawString("Girl's fucked !", Main.width/2 - 120, 250);
			g.drawString("Press 'enter' to fuck next one !", Main.width/2 -  225, 290);
			
			g.setColor(Color.orange);
			g.drawString("Girl's fucked !", Main.width/2 - 120-2, 250-2);
			g.drawString("Press 'enter' to fuck next one !", Main.width/2 - 225-2, 290-2);
		}			
		
		if(gameWin) {
			g.drawString("Congratulation !!!!", Main.width/2 - 120, 250);
			g.drawString("Press 'enter' to Quit !", Main.width/2 - 180, 290);
			
			g.setColor(Color.PINK);
			g.drawString("Congratulation !!!!", Main.width/2 - 120-2, 250-2);
			g.drawString("Press 'enter' to Quit !", Main.width/2 - 180-2, 290-2);
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawString("Score : "+score, 20, 20);
		g.setColor(Color.white);
		g.drawString("Score : "+score, 20-1, 20-1);
		
		DecimalFormat df = new DecimalFormat("0");
		
		g.setColor(Color.black);
		g.drawString("Time : "+df.format(timePassed), Main.width -  180, 20);
		g.setColor(Color.white);
		g.drawString("Time : "+df.format(timePassed), Main.width -  180-1, 20-1);
		
		
		for(int i=0; i<bicks.map.length; i++) {			
			for(int j=0; j<bicks.map[i].length; j++) {
				if(bicks.map[i][j][0] > 0 && bicks.map[i][j][1]> 0){
					g.setColor(Color.pink);
					g.fillRect(j*Bricks.brickWidth+(Bricks.brickSpacing*j)+Bricks.brickBorderPadding, 
							i*Bricks.brickHeight+(Bricks.brickSpacing*i)+Bricks.brickBorderPadding, 
							Bricks.brickWidth, Bricks.brickHeight);
					g.setColor(Color.black);
					g.drawRect(j*Bricks.brickWidth+(Bricks.brickSpacing*j)+Bricks.brickBorderPadding, 
							i*Bricks.brickHeight+(Bricks.brickSpacing*i)+Bricks.brickBorderPadding, 
							Bricks.brickWidth, Bricks.brickHeight);
					bicks.map[i][j][0] = j*Bricks.brickWidth+(Bricks.brickSpacing*j)+Bricks.brickBorderPadding;
					bicks.map[i][j][1] = i*Bricks.brickHeight+(Bricks.brickSpacing*i)+Bricks.brickBorderPadding;
				}
			}
		}
		
		g.setColor(Color.gray);
		g.fillRect(barreXpos, barreYpos, barreWidth, 20);
		g.setColor(Color.orange);
		g.fillOval(ballXpos-ballXdir, ballYpos, 20, 20);
		
		g.setColor(Color.black);
		g.drawRect(barreXpos, barreYpos, barreWidth, 20);		
		g.drawOval(ballXpos-ballXdir, ballYpos, 20, 20);
		
		g.dispose();
	}


	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!gameOver && !gameWin && !levelPassed) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				moveRight();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				moveLeft();
			}			
		}else if(levelPassed && gameWin == false){
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				restartGame();
				getNewHentaiImage();
			}
		}else if(gameWin){
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.exit(0);
			}
		}else {
			restartGame();
			getNewHentaiImage();
		}
	}
	
	private void restartGame() {
		ballXpos = Main.width/2 - 10;
		ballYpos = Main.height - 200;
		ballXdir = 1;
		ballYdir = -2;
		bicks = new Bricks(2+level,6);		
		nbBricks = bicks.map.length * bicks.map[1].length;
		barreXpos = Main.width / 2 - barreWidth/2 -8;
		barreYpos = Main.height - 120;
		gameOver = false;	
		startGame = false;
		gameWin = false;
		levelPassed = false;
	}


	private void moveRight() {	
		launchTimer();
		startGame = true;
		if(barreXpos < (Main.width-barreWidth-10)) {
			barreXpos += 20;			
		}else {
			barreXpos = Main.width-barreWidth-10;
		}
	}
	
	private void moveLeft() {	
		launchTimer();
		startGame = true;
		if(barreXpos > 0) {
			barreXpos -= 20;			
		}else {
			barreXpos = 0;
		}
	}
	
	private void checkBallBarreCollisions(){
		Rectangle rectBall = new Rectangle(ballXpos,ballYpos,20,20);
		Rectangle rectBarre = new Rectangle(barreXpos,barreYpos,barreWidth,20);			
		if(rectBall.intersects(rectBarre)) {
			ballYdir = -ballYdir;
		}
	}
	
	private void checkBallBorderCollisions(){
		if(ballXpos <= 0) {
			ballXdir = -ballXdir;
		}
		
		if(ballXpos >= Main.width-32) {
			ballXdir = -ballXdir;
		}			
		
		if(ballYpos <= 0) {
			ballYdir = -ballYdir;
		}
		
		if(ballYpos >= Main.height) {
			level = 1;
			timePassed = 0;
			score = 0;
			gameOver = true;
		}	
	}
	
	private void launchTimer() {
		if(startGame == false) {
			timeStartGame = System.currentTimeMillis();
		}
	}
	
	private void getNewHentaiImage() {
		Random rn = new Random();
		int range = maxNbImages - 1 + 1;
		int randomNum =  rn.nextInt(range) + 1;
		System.out.println(randomNum);
		try
		{	
			imgHentaiInit = ImageIO.read(getClass().getResourceAsStream("image/img-"+randomNum+".jpg"));
			blurringImage(imgHentaiInit);	
		}
		catch(IOException e)
		{
		  e.printStackTrace();
		}			
	}
	
	private void blurringImage(BufferedImage imgToBlur) {
		Thread t = new Thread(new Runnable(){	
			public void run() {
				int radius = nbBricks;
				if(nbBricks>20) {
					radius = 20;
				}
				
			    int size = radius * 2 + 1;
			    float weight = 1.0f / (size * size);
			    float[] data = new float[size * size];

			    for (int i = 0; i < data.length; i++) {
			        data[i] = weight;
			    }		
				Kernel kernel = new Kernel(size, size, data);
			    ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
			   
			    imgHentaiBlurry = op.filter(imgToBlur, null);	 		
			}
		});
		t.start();		   
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(startGame && !gameOver) {
			ballXpos += ballXdir;
			ballYpos += ballYdir;
			
			timePassed = (System.currentTimeMillis()-timeStartGame)/1000;			
			
			checkBallBarreCollisions();
			checkBallBorderCollisions();
			checkBallBricksCollisions();
			
			if(nbBricks <= 0) {
				startGame = false;
				levelPassed = true;
				level++;
			}
			
			if(level>15){
				startGame = false;
				gameWin = true;
			}
		}
		repaint();			
	}


	private void checkBallBricksCollisions() {
		for(int i=0; i<bicks.map.length; i++) {			
			for(int j=0; j<bicks.map[i].length; j++) {
				if(bicks.map[i][j][0] > 0 && bicks.map[i][j][1]> 0){
					Rectangle ballRect = new Rectangle(ballXpos,ballYpos,20,20);
					Rectangle brickRect = new Rectangle(bicks.map[i][j][0],bicks.map[i][j][1],Bricks.brickWidth,Bricks.brickHeight);
					if(ballRect.intersects(brickRect)) {
						bicks.map[i][j][0] = 0;
						bicks.map[i][j][1] = 0;
						ballYdir = -ballYdir;
						nbBricks --;
						score += Bricks.ptsBrick;						
						blurringImage(imgHentaiInit);
					}
				}
			}
		}		
	}

}
