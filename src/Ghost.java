import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Ghost {

    private int x, y;
    private int dx, dy;
    private final int speed;
    private Image imageGhost1;
    private Image imageGhost2;
    private Image imageGhost3;
    private Image imageGhost4;
    private Image vulnerableImage;
    private Image currentImage;
    private final Board board;
    private final Random random;
    private boolean isVulnerable;
    int randomNumber;

    public Ghost(int startX, int startY, Board board) {
        this.x = startX;
        this.y = startY;
        this.speed = 2;
        this.board = board;
        random = new Random();
        declareImage();
        randomizeDirection();
    }

    private void declareImage() {
        imageGhost1 = new ImageIcon("src/sprites/ghost1.gif").getImage();
        imageGhost2 = new ImageIcon("src/sprites/ghost2.gif").getImage();
        imageGhost3 = new ImageIcon("src/sprites/ghost3.gif").getImage();
        imageGhost4 = new ImageIcon("src/sprites/ghost4.gif").getImage();
        vulnerableImage = new ImageIcon("src/sprites/vulnerable_ghost.gif").getImage();
        Random random = new Random();
        randomNumber = random.nextInt(4) + 1;
        switch (randomNumber){
            case 1:
                currentImage = imageGhost1;
                break;
            case 2:
                currentImage = imageGhost2;
                break;
            case 3:
                currentImage = imageGhost3;
                break;
            case 4:
                currentImage = imageGhost4;
                break;
            default:
                break;
        }
    }

    private void randomizeDirection() {
        int direction = random.nextInt(4);
        switch (direction) {
            case 0:
                dx = speed;
                dy = 0;
                break;
            case 1:
                dx = -speed;
                dy = 0;
                break;
            case 2:
                dx = 0;
                dy = speed;
                break;
            case 3:
                dx = 0;
                dy = -speed;
                break;
        }
    }

    public void move() {
        if (x < 0 || y < 0 || x >= board.getSize() * 40 || y >= board.getSize() * 40) {
            return;
        }

        int newX = x + dx;
        int newY = y + dy;

        if (!board.isWall(newX, newY) && !board.isWall(newX + currentImage.getWidth(null) - 1, newY) &&
                !board.isWall(newX, newY + currentImage.getHeight(null) - 1) &&
                !board.isWall(newX + currentImage.getWidth(null) - 1, newY + currentImage.getHeight(null) - 1)) {
            x = newX;
            y = newY;
        } else {
            randomizeDirection();
        }
    }

    public void setVulnerable(boolean isVulnerable) {
        this.isVulnerable = isVulnerable;
        if(isVulnerable){
            currentImage = vulnerableImage;
        }else {
            switch (randomNumber){
                case 1:
                    currentImage = imageGhost1;
                    break;
                case 2:
                    currentImage = imageGhost2;
                    break;
                case 3:
                    currentImage = imageGhost3;
                    break;
                case 4:
                    currentImage = imageGhost4;
                    break;
                default:
                    break;
            }
        }
    }

    public boolean getIsVulnerable() {
        return isVulnerable;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, currentImage.getWidth(null), currentImage.getHeight(null));
    }

    public void draw(Graphics g) {
        g.drawImage(currentImage, x, y, null);
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
