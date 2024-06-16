import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JPanel implements Runnable {

    private Board board;
    private final int boardSize;
    private Player player;
    private List<Ghost> ghosts;
    private final int maxGhosts;
    private List<Upgrade> upgrades;
    private List<Dot> dots;
    private Score score;
    private boolean running;
    private boolean ghostsVulnerable;
    private Thread gameThread;
    private Thread upgradesSpawnThread;
    private final JFrame frame;

    public Game(int boardSize, JFrame frame , int maxGhosts) {
        this.frame = frame;
        this.ghostsVulnerable = false;
        this.boardSize = boardSize;
        this.maxGhosts = maxGhosts;
        start(this.boardSize);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);

                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    endGame();
                }
            }
        });
    }

    private void start(int boardSize) {
        board = new Board(boardSize);
        int playerStartX = (boardSize / 2) * 40;
        int playerStartY = (boardSize / 2 + 2) * 40; // игрок спавнится ниже центра
        player = new Player(playerStartX, playerStartY, board);
        ghosts = new ArrayList<>();
        upgrades = new ArrayList<>();
        dots = new ArrayList<>();
        score = new Score();


        int ghostRoomStartX = (boardSize / 2 - 1) * 40;
        int ghostRoomStartY = (boardSize / 2 - 3) * 40;
        for (int i = 0; i < maxGhosts; i++) {
            int ghostX = ghostRoomStartX + (i % 2) * 40;
            int ghostY = ghostRoomStartY + (i / 2) * 40;
            ghosts.add(new Ghost(ghostX, ghostY, board));
        }

        generateDots();
        startUpgradesSpawnThread();

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            if (player.getIsGameStarted()) {
                updateGame();
            }
            repaint();
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        player.move();
        for (Ghost ghost : ghosts) {
            ghost.move();
        }
        checkCollisions();
        score.update();
    }

    private void checkCollisions() {
        for (Ghost ghost : ghosts) {
            if (player.getBounds().intersects(ghost.getBounds())) {
                if (ghost.getIsVulnerable()) {
                    killGhost(ghost);
                } else {
                    if (!player.getIsShield()) {
                        player.setGameStarted(false);
                        JLabel minusLiveLabel = new JLabel("-1 LIFE");
                        minusLiveLabel.setBounds(200, 200, 200, 100);
                        minusLiveLabel.setFont(new Font("Arial", Font.BOLD, 32));
                        minusLiveLabel.setForeground(Color.RED);
                        minusLiveLabel.setVisible(true);
                        add(minusLiveLabel);
                        revalidate();
                        repaint();

                        if (player.getLives() != 0) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                SwingUtilities.invokeLater(() -> {
                                    remove(minusLiveLabel);
                                    revalidate();
                                    repaint();
                                });
                            }).start();
                        }

                        int playerPosX = (boardSize / 2) * 40;
                        int playerPosY = (boardSize / 2 + 2) * 40;
                        player.setBounds(playerPosX, playerPosY);
                        player.loseLife();

                        int ghostRoomStartX = (boardSize / 2 - 1) * 40;
                        int ghostRoomStartY = (boardSize / 2 - 3) * 40;
                        for (int i = 0; i < maxGhosts; i++) {
                            int ghostX = ghostRoomStartX + (i % 2) * 40;
                            int ghostY = ghostRoomStartY + (i / 2) * 40;
                            ghosts.get(i).setPosition(ghostX, ghostY);
                        }
                        score.loseLife();
                        if (player.getLives() <= 0) {
                            endGame();
                        }
                    } else {
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                player.setShield(false);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    }
                }
            }
        }

        for (Upgrade upgrade : upgrades) {
            if (player.getBounds().intersects(upgrade.getBounds())) {
                upgrade.activate(player, this);
                upgrades.remove(upgrade);
                score.addPoints(100);
                break;
            }
        }


        for (Dot dot : dots) {
            if (player.getBounds().intersects(dot.getBounds())) {
                dots.remove(dot);
                score.addPoints(10);
                break;
            }
        }

    }

    private void killGhost(Ghost ghost) {
        score.addPoints(200);
        ghost.setPosition(-40, -40);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            respawnGhost(ghost);
        }).start();
    }

    private void respawnGhost(Ghost ghost) {
        int ghostRoomStartX = (board.getSize() / 2 - 1) * 40;
        int ghostRoomStartY = (board.getSize() / 2 - 3) * 40;
        int ghostX = ghostRoomStartX + new Random().nextInt(2) * 40;
        int ghostY = ghostRoomStartY + new Random().nextInt(2) * 40;
        ghost.setPosition(ghostX, ghostY);
        ghost.setVulnerable(false);
    }

    private void generateDots() {
        for (int i = 1; i < board.getSize() - 1; i++) {
            for (int j = 1; j < board.getSize() - 1; j++) {
                if (!board.isWall(i * 40, j * 40) && !board.isGhostRoom(i * 40, j * 40) &&
                        !(i == board.getSize() / 2 && j == board.getSize() / 2)) {
                    dots.add(new Dot(i * 40 + 15, j * 40 + 15));
                }
            }
        }
    }

    private void startUpgradesSpawnThread() {
        upgradesSpawnThread = new Thread(() -> {
            Random random = new Random();
            while (running) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                int x, y;
                do {
                    x = random.nextInt(board.getSize()) * 40;
                    y = random.nextInt(board.getSize()) * 40;
                } while (board.isWall(x, y) || board.isGhostRoom(x, y));

                String[] types = {"SpeedBoost", "ExtraLife", "SlowDown", "KillGhosts", "Shield"};
                String type = types[random.nextInt(types.length)];
                if (upgrades.size() < 5) {
                    upgrades.add(new Upgrade(x, y, type));
                }
            }
        });
        upgradesSpawnThread.start();
    }

    public void setGhostsVulnerable(boolean vulnerable) {
        for (Ghost ghost : ghosts) {
            ghost.setVulnerable(vulnerable);
        }
        if (vulnerable) {
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setGhostsVulnerable(false);
            }).start();
        }
    }

    private void endGame() {
        running = false;

        String playerName = JOptionPane.showInputDialog(this, "Write your name : ", "Game Over", JOptionPane.PLAIN_MESSAGE);
        HighScores highScores = new HighScores();
        highScores.saveScore(playerName, score.getPoints());
        highScores.display();

        frame.getContentPane().removeAll();
        frame.add(new Menu(frame));
        frame.revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        board.draw(g);
        player.draw(g);
        for (Dot dot : dots) {
            dot.draw(g);
        }
        for (Upgrade upgrade : upgrades) {
            upgrade.draw(g);
        }
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
        score.draw(g, player);
    }
}
