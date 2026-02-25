import java.awt.Color;

public class Tetromino {
    
    public enum Shape {
        I, O, T, L, J, S, Z
    }
    
    private Shape shape;
    private int[][] coords;
    private Color color;
    
    public Tetromino(Shape shape) {
        this.shape = shape;
        initTetromino();
    }
    
    private void initTetromino() {
        switch(shape) {
            case I:
                coords = new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}};
                color = Color.CYAN;
                break;
            case O:
                coords = new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}};
                color = Color.YELLOW;
                break;
            case T:
                coords = new int[][]{{1, 0}, {0, 1}, {1, 1}, {2, 1}};
                color = Color.MAGENTA;
                break;
            case L:
                coords = new int[][]{{0, 0}, {0, 1}, {0, 2}, {1, 2}};
                color = Color.ORANGE;
                break;
            case J:
                coords = new int[][]{{1, 0}, {1, 1}, {1, 2}, {0, 2}};
                color = Color.BLUE;
                break;
            case S:
                coords = new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}};
                color = Color.GREEN;
                break;
            case Z:
                coords = new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}};
                color = Color.RED;
                break;
        }
    }
    
    public int[][] getCoords() {
        return coords;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void rotate() {
        if (shape == Shape.O) return; // O shape doesn't need rotation
        
        int[][] rotated = new int[coords.length][2];
        
        // Rotate 90 degrees around center
        for (int i = 0; i < coords.length; i++) {
            int x = coords[i][0];
            int y = coords[i][1];
            rotated[i][0] = -y + 1;
            rotated[i][1] = x;
        }
        
        coords = rotated;
    }
    
    public static Tetromino getRandomTetromino() {
        Shape[] shapes = Shape.values();
        int randomIndex = (int) (Math.random() * shapes.length);
        return new Tetromino(shapes[randomIndex]);
    }
}