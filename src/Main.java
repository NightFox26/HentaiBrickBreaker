import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
	
	public static int width = 600;
	public static int height = 800;
	public static Color color = Color.BLACK;
	

	public static void main(String[] args) {
		JFrame panel = new JFrame();
		panel.getContentPane().setBackground(color);
		panel.setBounds(0,0, width, height);
		panel.setTitle("Hentai Brick Breaker ---- By NightFox26 ---- For Kabuki Bro !!!");
		panel.setAlwaysOnTop(true);
		panel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panel.setLocationRelativeTo(null);
		Game game = new Game();
		panel.setContentPane(game);
		panel.setVisible(true);
	}

}
