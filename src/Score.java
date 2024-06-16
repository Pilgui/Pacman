import java.awt.*;
import javax.swing.*;

public class Score {

    private int points;
    private int lives;
    private long startTime;
    private long elapsedTime;

    public Score() {
        this.points = 0;
        this.lives = 3;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
        elapsedTime = System.currentTimeMillis() - startTime;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void loseLife() {
        this.lives--;
    }

    public int getPoints() {
        return points;
    }

    public long getElapsedTime() {
        return elapsedTime / 1000;
    }

    public void draw(Graphics g, Player player) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        String pointsText = "Points: " + points;
        String livesText = "Lives: " + player.getLives();
        String timeText = "Time: " + getElapsedTime() + "s";
        String isShieldText;

        if (player.getIsShield()) {
            isShieldText = "Shield: ON";
        }else isShieldText = "Shield: OFF";

        g.drawString(pointsText, 20, 20);
        g.drawString(livesText, 20, 50);
        g.drawString(timeText, 20, 80);
        g.drawString(isShieldText, 20, 110);
    }
}
