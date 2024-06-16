import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class HighScores {

    private List<ScoreOfPlayer> scores;
    private final String fileName = "highScores.txt";

    public HighScores() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void loadScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int points = Integer.parseInt(parts[1]);
                    scores.add(new ScoreOfPlayer(playerName, points));
                }
            }
        } catch (FileNotFoundException e) {
            scores = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(String playerName, int points) {
        scores.add(new ScoreOfPlayer(playerName, points));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (ScoreOfPlayer score : scores) {
                bw.write(score.getPlayerName() + "," + score.getPoints());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getScores() {
        String[] scoresArray = new String[scores.size()];
        for (int i = 0; i < scores.size(); i++) {
            ScoreOfPlayer score = scores.get(i);
            scoresArray[i] = (i + 1) + ". " + score.getPlayerName() + ": " + score.getPoints();
        }
        return scoresArray;
    }

    public void display() {
        JFrame highScoresFrame = new JFrame("High Scores");
        highScoresFrame.setSize(400, 600);
        highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JList<String> scoresList = new JList<>(getScores());
        scoresList.setFont(new Font("Arial", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(scoresList);

        highScoresFrame.add(scrollPane);
        highScoresFrame.setVisible(true);
    }

    private static class ScoreOfPlayer {
        private final String playerName;
        private final int points;

        public ScoreOfPlayer(String playerName, int points) {
            this.playerName = playerName;
            this.points = points;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getPoints() {
            return points;
        }
    }
}
