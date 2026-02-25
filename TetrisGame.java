import javax.swing.JFrame;

public class TetrisGame extends JFrame {
    
    public TetrisGame() {
        setTitle("Tetris");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new TetrisGame();
    }
}