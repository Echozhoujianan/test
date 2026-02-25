import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    
    private static final int COLS = 10;
    private static final int ROWS = 20;
    private static final int BLOCK_SIZE = 25;
    
    private Color[][] board = new Color[ROWS][COLS];
    private Tetromino currentPiece;
    private int currentX, currentY;
    private Timer timer;
    private boolean isGameOver;
    private int score;
    
    private JButton leftButton;
    private JButton rightButton;
    private JButton downButton;
    private JButton rotateButton;
    private JPanel buttonPanel;
    
    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        setLayout(new FlowLayout());
        
        initBoard();
        spawnNewPiece();
        
        timer = new Timer(500, this);
        timer.start();
        
        initButtons();
    }
    
    private void initButtons() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 5, 5));
        buttonPanel.setSize(COLS * BLOCK_SIZE, 100);
        
        leftButton = new JButton("Left");
        leftButton.addActionListener(e -> moveLeft());
        
        rightButton = new JButton("Right");
        rightButton.addActionListener(e -> moveRight());
        
        downButton = new JButton("Down");
        downButton.addActionListener(e -> moveDown());
        
        rotateButton = new JButton("Rotate");
        rotateButton.addActionListener(e -> rotate());
        
        buttonPanel.add(leftButton);
        buttonPanel.add(downButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(rotateButton);
        
        add(buttonPanel);
    }
    
    private void initBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = Color.BLACK;
            }
        }
    }
    
    private void spawnNewPiece() {
        currentPiece = Tetromino.getRandomTetromino();
        currentX = COLS / 2 - 1;
        currentY = 0;
        
        if (!isValidMove(0, 0)) {
            isGameOver = true;
            timer.stop();
        }
    }
    
    private boolean isValidMove(int dx, int dy) {
        int[][] coords = currentPiece.getCoords();
        
        for (int[] coord : coords) {
            int newX = currentX + coord[0] + dx;
            int newY = currentY + coord[1] + dy;
            
            if (newX < 0 || newX >= COLS || newY >= ROWS) {
                return false;
            }
            
            if (newY >= 0 && board[newY][newX] != Color.BLACK) {
                return false;
            }
        }
        
        return true;
    }
    
    private void moveLeft() {
        if (isValidMove(-1, 0)) {
            currentX--;
        }
        repaint();
    }
    
    private void moveRight() {
        if (isValidMove(1, 0)) {
            currentX++;
        }
        repaint();
    }
    
    private void moveDown() {
        if (isValidMove(0, 1)) {
            currentY++;
        } else {
            lockPiece();
            clearLines();
            spawnNewPiece();
        }
        repaint();
    }
    
    private void rotate() {
        currentPiece.rotate();
        if (!isValidMove(0, 0)) {
            currentPiece.rotate();
            currentPiece.rotate();
            currentPiece.rotate();
        }
        repaint();
    }
    
    private void lockPiece() {
        int[][] coords = currentPiece.getCoords();
        Color color = currentPiece.getColor();
        
        for (int[] coord : coords) {
            int x = currentX + coord[0];
            int y = currentY + coord[1];
            
            if (y >= 0) {
                board[y][x] = color;
            }
        }
    }
    
    private void clearLines() {
        int linesCleared = 0;
        
        for (int i = ROWS - 1; i >= 0; i--) {
            boolean isLineFull = true;
            
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == Color.BLACK) {
                    isLineFull = false;
                    break;
                }
            }
            
            if (isLineFull) {
                linesCleared++;
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < COLS; j++) {
                        board[k][j] = board[k - 1][j];
                    }
                }
                for (int j = 0; j < COLS; j++) {
                    board[0][j] = Color.BLACK;
                }
                i++;
            }
        }
        
        if (linesCleared > 0) {
            score += linesCleared * 100;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw game area
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        
        // Draw grid lines
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= COLS; i++) {
            g.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        }
        for (int i = 0; i <= ROWS; i++) {
            g.drawLine(0, i * BLOCK_SIZE, COLS * BLOCK_SIZE, i * BLOCK_SIZE);
        }
        
        // Draw locked blocks
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] != Color.BLACK) {
                    g.setColor(board[i][j]);
                    g.fillRect(j * BLOCK_SIZE + 1, i * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                }
            }
        }
        
        // Draw current piece
        if (currentPiece != null) {
            g.setColor(currentPiece.getColor());
            int[][] coords = currentPiece.getCoords();
            for (int[] coord : coords) {
                int x = (currentX + coord[0]) * BLOCK_SIZE + 1;
                int y = (currentY + coord[1]) * BLOCK_SIZE + 1;
                g.fillRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
            }
        }
        
        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("分数: " + score, 10, ROWS * BLOCK_SIZE + 20);
        
        // Draw game over message
        if (isGameOver) {
            g.setColor(Color.RED);
            g.drawString("游戏结束!", 10, ROWS * BLOCK_SIZE + 40);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) return;
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                moveRight();
                break;
            case KeyEvent.VK_DOWN:
                moveDown();
                break;
            case KeyEvent.VK_UP:
                rotate();
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            moveDown();
        }
    }
}