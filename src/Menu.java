import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {

    private JFrame frame;

    public Menu(JFrame frame) {
        this.frame = frame;
        start();
    }

    private void start() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton newGameButton = new JButton("New Game");
        JButton highScoresButton = new JButton("High Scores");
        JButton exitButton = new JButton("Exit");

        newGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        highScoresButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(newGameButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(highScoresButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(exitButton, gbc);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void startNewGame() {
        int maxGhosts;
        String[] options = {"Very Small", "Small", "Medium", "Large" , "Very Large"};
        int size = JOptionPane.showOptionDialog(this, "Select Board Size", "New Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        int boardSize;
        switch (size) {
            case 0:
                boardSize = 7;
                maxGhosts = 1;
                break;
            case 1:
                maxGhosts = 2;
                boardSize = 10;
                break;
            case 2:
                maxGhosts = 3;
                boardSize = 13;
                break;
            case 3:
                maxGhosts = 4;
                boardSize = 16;
                break;
            case 4:
                boardSize = 19;
                maxGhosts = 5;
                break;
            default:
                return;
        }

        frame.getContentPane().removeAll();
        Game game = new Game(boardSize, frame, maxGhosts);
        frame.add(game);
        frame.revalidate();
    }

    private void showHighScores() {
        HighScores highScores = new HighScores();

        JFrame highScoresFrame = new JFrame("High Scores");
        highScoresFrame.setSize(400, 600);
        highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JList<String> scoresList = new JList<>(highScores.getScores());
        scoresList.setFont(new Font("Arial", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(scoresList);

        highScoresFrame.add(scrollPane);
        highScoresFrame.setVisible(true);
    }
}
