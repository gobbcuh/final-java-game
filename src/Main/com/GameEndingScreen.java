package Main.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameEndingScreen extends JPanel {
    private Image[] endBackgrounds;
    private Image[] congratsImages;
    private Image[] knowledgeCards;
    private int currentBackgroundIndex = 0;
    private int currentCongratsIndex = 0;
    private int currentKnowledgeCardIndex = -1;
    private Timer backgroundTimer;
    private Timer congratsTimer;
    private Timer knowledgeCardTimer;
    private Clip backgroundMusic;

    private Image queenBinaryImage;
    private Timer queenAnimationTimer;
    private int sparkleAlpha = 255;
    private boolean sparkleFadeOut = true;
    private int queenX;
    private int queenY;
    private int queenTargetX = 195;
    private int queenStartX = -300;
    private int queenWidth;
    private int queenHeight;
    private float queenScale = 3.5f;

    private Image endQuit1;
    private Image endQuit2;
    private boolean showEndQuit1 = false;
    private boolean showEndQuit2 = false;

    public GameEndingScreen(JFrame parentFrame) {
        // Load the three ending background images
        endBackgrounds = new Image[3];
        for (int i = 0; i < 3; i++) {
            endBackgrounds[i] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/end-bg" + (i + 1) + ".png").getImage();
        }

        // Load the three "CONGRATULATIONS!" images
        congratsImages = new Image[3];
        for (int i = 0; i < 3; i++) {
            congratsImages[i] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/congrats" + (i + 1) + ".png").getImage();
        }

        // Load the three knowledge card images
        knowledgeCards = new Image[3];
        for (int i = 0; i < 3; i++) {
            knowledgeCards[i] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/knowledge-card" + (i + 1) + ".png").getImage();
        }

        // Load the end-quit1 and end-quit2 images
        endQuit1 = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/end-quit1.png").getImage();
        endQuit2 = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/end-quit2.png").getImage();

        // Set up a timer to cycle through the background images
        backgroundTimer = new Timer(200, new ActionListener() { // Change background every 200ms
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBackgroundIndex = (currentBackgroundIndex + 1) % 3;
                repaint();
            }
        });
        backgroundTimer.start();

        // Set up a timer to cycle through the "CONGRATULATIONS!" images
        congratsTimer = new Timer(500, new ActionListener() { // Change "CONGRATULATIONS!" image every 500ms
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCongratsIndex = (currentCongratsIndex + 1) % 3;
                repaint();
            }
        });
        congratsTimer.start();

        // Set up a timer to display the knowledge cards sequentially
        knowledgeCardTimer = new Timer(2000, new ActionListener() { // Display each knowledge card every 2 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentKnowledgeCardIndex < 2) {
                    currentKnowledgeCardIndex++;
                    repaint();

                    // Start the queen's animation when the first knowledge card is displayed
                    if (currentKnowledgeCardIndex == 0) {
                        initializeQueen();
                    }
                } else {
                    knowledgeCardTimer.stop();
                    showEndQuit1 = true;
                    repaint();
                }
            }
        });
        knowledgeCardTimer.setInitialDelay(6000); // Start displaying knowledge cards after 6 seconds
        knowledgeCardTimer.start();

        // Play the background music
        playBackgroundMusic();

        // Add mouse listener for end-quit1
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showEndQuit1) {
                    int x = e.getX();
                    int y = e.getY();

                    int quit1Width = endQuit1.getWidth(null);
                    int quit1Height = endQuit1.getHeight(null);
                    int quit1X = (getWidth() - quit1Width) / 2;
                    int quit1Y = getHeight() - quit1Height - 50;

                    if (x >= quit1X && x <= quit1X + quit1Width && y >= quit1Y && y <= quit1Y + quit1Height) {
                        showEndQuit1 = false;
                        showEndQuit2 = true;
                        repaint();

                        new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void initializeQueen() {
        queenBinaryImage = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-binary.png").getImage();
        queenWidth = (int) (queenBinaryImage.getWidth(null) * queenScale); // Scale the width
        queenHeight = (int) (queenBinaryImage.getHeight(null) * queenScale); // Scale the height

        // Timer for the queen's slide animation
        queenAnimationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenX < queenTargetX) {
                    queenX += 5; // Move the queen to the right
                }

                // Sparkle fade animation
                if (sparkleFadeOut) {
                    sparkleAlpha -= 10;
                    if (sparkleAlpha <= 200) sparkleFadeOut = false;
                } else {
                    sparkleAlpha += 10;
                    if (sparkleAlpha >= 255) sparkleFadeOut = true;
                }
                repaint();
            }
        });
        queenX = queenStartX; // Start the queen off-screen to the left
        queenY = (getHeight() - queenHeight) / 2 + 20; // Center vertically
        queenAnimationTimer.start();
    }

    private void playBackgroundMusic() {
        try {
            File musicFile = new File("C:/Users/User/IdeaProjects/final/out/production/final/Main/theme-music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the current background image
        g.drawImage(endBackgrounds[currentBackgroundIndex], 0, 0, getWidth(), getHeight(), this);

        // Draw the "CONGRATULATIONS!" images based on the current index
        if (currentCongratsIndex >= 0) {
            drawCongratsImage(g, 0);
        }
        if (currentCongratsIndex >= 1) {
            drawCongratsImage(g, 1);
        }
        if (currentCongratsIndex >= 2) {
            drawCongratsImage(g, 2);
        }

        // Draw the knowledge cards based on the current index
        if (currentKnowledgeCardIndex >= 0) {
            drawKnowledgeCard(g, 0);
        }
        if (currentKnowledgeCardIndex >= 1) {
            drawKnowledgeCard(g, 1);
        }
        if (currentKnowledgeCardIndex >= 2) {
            drawKnowledgeCard(g, 2);
        }

        // Draw the queen and sparkles if the queen animation has started
        if (queenAnimationTimer != null && queenAnimationTimer.isRunning()) {
            drawQueenAndSparkles(g);
        }

        // Draw end-quit1 if it should be shown
        if (showEndQuit1) {
            int quit1Width = endQuit1.getWidth(null);
            int quit1Height = endQuit1.getHeight(null);
            int quit1X = (getWidth() - quit1Width) / 2;
            int quit1Y = getHeight() - quit1Height - 5;
            g.drawImage(endQuit1, quit1X, quit1Y, quit1Width, quit1Height, this);
        }

        // Draw end-quit2 if it should be shown
        if (showEndQuit2) {
            int quit2Width = endQuit2.getWidth(null);
            int quit2Height = endQuit2.getHeight(null);
            int quit2X = (getWidth() - quit2Width) / 2;
            int quit2Y = getHeight() - quit2Height - 5;
            g.drawImage(endQuit2, quit2X, quit2Y, quit2Width, quit2Height, this);
        }
    }

    private void drawCongratsImage(Graphics g, int index) {
        if (congratsImages[index] != null) {
            int congratsWidth = congratsImages[index].getWidth(this) * 2;
            int congratsHeight = congratsImages[index].getHeight(this) * 2;
            int x = (getWidth() - congratsWidth) / 2;
            int y = 50;
            g.drawImage(congratsImages[index], x, y, congratsWidth, congratsHeight, this);
        }
    }

    private void drawKnowledgeCard(Graphics g, int index) {
        if (knowledgeCards[index] != null) {
            int cardWidth = knowledgeCards[index].getWidth(this) * 2;
            int cardHeight = knowledgeCards[index].getHeight(this) * 2;
            int x = (getWidth() / 4) * (index + 1) - (cardWidth / 2);
            int y = getHeight() - cardHeight - 1;
            x += index * 35;
            g.drawImage(knowledgeCards[index], x, y, cardWidth, cardHeight, this);
        }
    }

    private void drawQueenAndSparkles(Graphics g) {
        // Draw the queen's image
        g.drawImage(queenBinaryImage, queenX, queenY, queenWidth, queenHeight, this);

        // Draw sparkles around the queen
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 20; i++) {
            int sparkleX, sparkleY;
            do {
                sparkleX = queenX + (int) (Math.random() * (queenWidth + 100)) - 50;
                sparkleY = queenY + (int) (Math.random() * (queenHeight + 100)) - 50;
            } while (sparkleX >= queenX && sparkleX <= queenX + queenWidth && sparkleY >= queenY && sparkleY <= queenY + queenHeight);

            int sparkleSize = 5 + (int) (Math.random() * 5);
            g2d.setColor(new Color(255, 255, 255, sparkleAlpha));

            int[] xPoints = {
                    sparkleX, sparkleX + sparkleSize / 4, sparkleX + sparkleSize / 2, sparkleX + sparkleSize / 4, sparkleX,
                    sparkleX - sparkleSize / 4, sparkleX - sparkleSize / 2, sparkleX - sparkleSize / 4
            };
            int[] yPoints = {
                    sparkleY - sparkleSize / 2, sparkleY - sparkleSize / 4, sparkleY, sparkleY + sparkleSize / 4, sparkleY + sparkleSize / 2,
                    sparkleY + sparkleSize / 4, sparkleY, sparkleY - sparkleSize / 4
            };
            g2d.fillPolygon(xPoints, yPoints, 8);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Ending Screen");
        GameEndingScreen panel = new GameEndingScreen(frame);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}