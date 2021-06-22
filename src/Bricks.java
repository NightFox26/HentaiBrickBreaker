
public class Bricks {
	
	public int[][][] map;
	public static int brickWidth = 80;
	public static int brickHeight = 30;
	public static int brickSpacing = 5;
	public static int brickBorderPadding = 40;
	public static int ptsBrick = 5;
	
	public Bricks(int rows, int cols) {		
		map = new int[rows][cols][2];
		for(int i=0; i<rows; i++) {			
			for(int j=0; j<cols; j++) {
				map[i][j][0] = 1;
				map[i][j][1] = 1;
			}
		}
		
		setBrickWidth(cols);
	}
	
	private void setBrickWidth(int cols) {
		if(Main.width - (cols*brickWidth) - (cols*brickSpacing) - (2*brickBorderPadding) < 0) {
			brickWidth = (Main.width - (cols*brickSpacing) - (2*brickBorderPadding) - 8) / cols;
		}
	}

}
