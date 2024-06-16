import java.awt.*;

public class Dot {

    private int x, y;
    private static final int size = 10;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
