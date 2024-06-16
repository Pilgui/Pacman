import java.awt.*;
import javax.swing.ImageIcon;

public class Upgrade {

    private final int x;
    private final int y;
    private String type;
    private Image image;

    public Upgrade(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        declareImage();
    }

    private void declareImage() {
        switch (type) {
            case "SpeedBoost":
                image = new ImageIcon("src/sprites/speed_boost.png").getImage();
                break;
            case "ExtraLife":
                image = new ImageIcon("src/sprites/extra_life.png").getImage();
                break;
            case "SlowDown":
                image = new ImageIcon("src/sprites/slow_down.png").getImage();
                break;
            case "KillGhosts":
                image = new ImageIcon("src/sprites/kill_ghosts.png").getImage();
                break;
            case "Shield":
                image = new ImageIcon("src/sprites/shield.png").getImage();
                break;
            default:
                break;
        }
    }

    public void activate(Player player, Game game) {
        switch (type) {
            case "SpeedBoost":
                player.setSpeed(player.getSpeed() + 0.2f);
                break;
            case "ExtraLife":
                player.setLives(player.getLives() + 1);
                break;
            case "SlowDown":
                player.setSpeed(player.getSpeed() - 0.2f);
                break;
            case "KillGhosts":
                game.setGhostsVulnerable(true);
                break;
            case "Shield":
                player.setShield(true);
                break;
            default:
                break;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
