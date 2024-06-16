import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class Board {

    private int size;
    private int[][] grid;
    private static final int sizeBoard = 40;
    private Image wallImage;
    private Image ghostRoomImage;

    public Board(int size) {
        this.size = size;
        grid = new int[size][size];
        start();
        declareImages();
    }

    private void start() {
        Random random = new Random();
        int playerSpawnArea = 3;
        int ghostRoomArea = 3;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (i == 0 || j == 0 || i == size - 1 || j == size - 1) {
                    grid[i][j] = 1;
                } else if (i >= size / 2 - ghostRoomArea / 2 - 2 && i <= size / 2 + ghostRoomArea / 2 - 2 &&
                        j >= size / 2 - ghostRoomArea / 2 && j <= size / 2 + ghostRoomArea / 2) {
                    grid[i][j] = 2;
                } else if (i >= size / 2 + 1 - playerSpawnArea / 2 && i <= size / 2 + 1 + playerSpawnArea / 2 &&
                        j >= size / 2 - playerSpawnArea / 2 && j <= size / 2 + playerSpawnArea / 2) {
                    grid[i][j] = 0;
                } else if (random.nextInt(5) == 0) {
                    grid[i][j] = 1;
                } else {
                    grid[i][j] = 0;
                }
            }
        }
    }

    private void declareImages() {
        wallImage = new ImageIcon("src/sprites/wall.png").getImage();
        ghostRoomImage = new ImageIcon("src/sprites/ghost_room.png").getImage();
    }

    public void draw(Graphics g) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 1) {
                    g.drawImage(wallImage, j * sizeBoard, i * sizeBoard, null);
                } else if (grid[i][j] == 2) {
                    g.drawImage(ghostRoomImage, j * sizeBoard, i * sizeBoard, null);
                }
            }
        }
    }

    public boolean isWall(int x, int y) {
        int gridX = x / sizeBoard;
        int gridY = y / sizeBoard;
        return grid[gridY][gridX] == 1;
    }

    public boolean isGhostRoom(int x, int y) {
        int gridX = x / sizeBoard;
        int gridY = y / sizeBoard;
        return grid[gridY][gridX] == 2;
    }

    public int getSize() {
        return size;
    }
}


