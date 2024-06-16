import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player {

    private int x, y;
    private int dx, dy;
    private float speed;
    private int lives;
    private Image image;
    private final Board board;
    private char direction;
    private char requestedDirection;
    private final int wallSize;
    private boolean isGameStarted = false;
    private final ImageIcon imagePacmanRight = new ImageIcon("src/sprites/pacman_Right.gif");
    private final ImageIcon imagePacmanLeft = new ImageIcon("src/sprites/pacman_Left.gif");
    private final ImageIcon imagePacmanUp = new ImageIcon("src/sprites/pacman_Up.gif");
    private final ImageIcon imagePacmanDown = new ImageIcon("src/sprites/pacman_Down.gif");
    private boolean isShield;
    public Player(int startX, int startY, Board board) {
        this.x = startX;
        this.y = startY;
        this.speed = 5;
        this.lives = 3;
        this.board = board;
        this.wallSize = 40;


        image = imagePacmanRight.getImage();
    }

    public void move() {
        if (x % wallSize == 0 && y % wallSize == 0) {
            switch (requestedDirection) {
                case 'L':
                    if (!isWall((int) (x - speed), y)) {
                        direction = 'L';
                    }
                    break;
                case 'R':
                    if (!isWall((int) (x + speed), y)) {
                        direction = 'R';
                    }
                    break;
                case 'U':
                    if (!isWall(x, (int) (y - speed))) {
                        direction = 'U';
                    }
                    break;
                case 'D':
                    if (!isWall(x, (int) (y + speed))) {
                        direction = 'D';
                    }
                    break;
            }
        }

        switch (direction) {
            case 'L':
                if (!isWall((int) (x - speed), y)) {
                    x -= (int) speed;
                } else {
                    x = (x / wallSize) * wallSize;
                }
                image = imagePacmanLeft.getImage();
                break;
            case 'R':
                if (!isWall((int) (x + speed), y)) {
                    x += (int) speed;
                } else {
                    x = (x / wallSize) * wallSize;
                }
                image = imagePacmanRight.getImage();
                break;
            case 'U':
                if (!isWall(x, (int) (y - speed))) {
                    y -= (int) speed;
                } else {
                    y = (y / wallSize) * wallSize;
                }
                image = imagePacmanUp.getImage();
                break;
            case 'D':
                if (!isWall(x, (int) (y + speed))) {
                    y += (int) speed;
                } else {
                    y = (y / wallSize) * wallSize;
                }
                image = imagePacmanDown.getImage();
                break;
        }
    }

    private boolean isWall(int newX, int newY) {
        if (newX < 0 || newY < 0 || newX >= board.getSize() * wallSize || newY >= board.getSize() * wallSize) {
            return true;
        }

        return board.isWall(newX, newY) ||
                board.isWall(newX + wallSize - 1, newY) ||
                board.isWall(newX, newY + wallSize - 1) ||
                board.isWall(newX + wallSize - 1, newY + wallSize - 1);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                requestedDirection = 'L';
                isGameStarted = true;
                break;
            case KeyEvent.VK_RIGHT:
                requestedDirection = 'R';
                isGameStarted = true;
                break;
            case KeyEvent.VK_UP:
                requestedDirection = 'U';
                isGameStarted = true;
                break;
            case KeyEvent.VK_DOWN:
                requestedDirection = 'D';
                isGameStarted = true;
                break;
        }

        if (x % wallSize == 0 && y % wallSize == 0) {
            switch (requestedDirection) {
                case 'L':
                    if (!isWall((int) (x - speed), y)) {
                        direction = 'L';
                    }
                    break;
                case 'R':
                    if (!isWall((int) (x + speed), y)) {
                        direction = 'R';
                    }
                    break;
                case 'U':
                    if (!isWall(x, (int) (y - speed))) {
                        direction = 'U';
                    }
                    break;
                case 'D':
                    if (!isWall(x, (int) (y + speed))) {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public void loseLife() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBounds(int x , int y){
        this.x = x;
        this.y = y;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public boolean getIsGameStarted(){
        return isGameStarted;
    }

    public boolean getIsShield() {
        return isShield;
    }

    public void setShield(boolean shield) {
        isShield = shield;
    }
}
