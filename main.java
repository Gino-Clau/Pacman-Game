package App;
import javax.swing.JFrame;


public class main {
	public static void main(String[] args) throws Exception{
		int rowCount = 19;
		int columnCount = 21;
        int tileSize = 32;
        int boardWidth = tileSize * columnCount;
        int boardHeight = tileSize * rowCount;


        JFrame frame = new JFrame("Pac Man");
        frame.setSize(boardWidth,boardHeight);   
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
	}
}
