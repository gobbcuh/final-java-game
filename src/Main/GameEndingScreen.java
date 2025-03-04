package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameEndingScreen extends JPanel {
    private Image[] endBackgrounds; // Array to hold the three background images
    private Image[] congratsImages; // Array to hold the three "CONGRATULATIONS!" images
    private Image[] knowledgeCards; // Array to hold the three knowledge card images
    private int currentBackgroundIndex = 0; // Index of the current background image
    private int currentCongratsIndex = 0; // Index of the current "CONGRATULATIONS!" image
    private int currentKnowledgeCardIndex = -1; // Index of the current knowledge card image (-1 means none displayed yet)
    private Timer backgroundTimer; // Timer to cycle through the background images
    private Timer congratsTimer; // Timer to cycle through the "CONGRATULATIONS!" images
    private Timer knowledgeCardTimer; // Timer to control the display of knowledge cards
    private Clip backgroundMusic; // Clip for the background music

    private Image queenBinaryImage;
    private float queenScale = 0.1f; // Initial scale for the queen's image
    private Timer queenAnimationTimer; // Timer for the queen's animation
    private int sparkleAlpha = 255; // Alpha value for sparkles
    private boolean sparkleFadeOut = true; // Sparkle fade direction
    private int queenX; // X position of the queen
    private int queenY; // Y position of the queen
    private int newQueenWidth; // Width of the queen's image
    private int newQueenHeight; // Height of the queen's image
    private boolean queenMoveStarted = false; // Flag to check if the queen has started moving

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

        // Set up a timer to cycle through the background images
        backgroundTimer = new Timer(200, new ActionListener() { // Change background every 200ms
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the current background index
                currentBackgroundIndex = (currentBackgroundIndex + 1) % 3;
                repaint(); // Redraw the panel
            }
        });
        backgroundTimer.start(); // Start the background timer

        // Set up a timer to cycle through the "CONGRATULATIONS!" images
        congratsTimer = new Timer(500, new ActionListener() { // Change "CONGRATULATIONS!" image every 500ms
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the current "CONGRATULATIONS!" image index
                currentCongratsIndex = (currentCongratsIndex + 1) % 3;
                repaint(); // Redraw the panel
            }
        });
        congratsTimer.start(); // Start the "CONGRATULATIONS!" timer

        // Set up a timer to display the knowledge cards sequentially
        knowledgeCardTimer = new Timer(2000, new ActionListener() { // Display each knowledge card every 2 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentKnowledgeCardIndex < 2) {
                    currentKnowledgeCardIndex++;
                    repaint(); // Redraw the panel
                } else {
                    knowledgeCardTimer.stop(); // Stop the timer after all cards are displayed
                    initializeQueen(); // Initialize the queen and start her animation
                }
            }
        });
        knowledgeCardTimer.setInitialDelay(6000); // Start displaying knowledge cards after 6 seconds (adjust as needed)
        knowledgeCardTimer.start(); // Start the knowledge card timer

        // Play the background music
        playBackgroundMusic();
    }

    private void initializeQueen() {
        queenBinaryImage = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-binary.png").getImage();

        // Timer for the queen's animation
        queenAnimationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenScale < 1.0f) {
                    queenScale += 0.02f; // Gradually increase the scale
                }

                // Sparkle fade animation
                if (sparkleFadeOut) {
                    sparkleAlpha -= 10;
                    if (sparkleAlpha <= 200) sparkleFadeOut = false; // Adjusted minimum alpha to 100
                } else {
                    sparkleAlpha += 10;
                    if (sparkleAlpha >= 255) sparkleFadeOut = true;
                }
                repaint();
            }
        });
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
            drawCongratsImage(g, 0); // Always draw congrats1
        }
        if (currentCongratsIndex >= 1) {
            drawCongratsImage(g, 1); // Draw congrats2 if currentCongratsIndex >= 1
        }
        if (currentCongratsIndex >= 2) {
            drawCongratsImage(g, 2); // Draw congrats3 if currentCongratsIndex >= 2
        }

        // Draw the knowledge cards based on the current index
        if (currentKnowledgeCardIndex >= 0) {
            drawKnowledgeCard(g, 0); // Draw knowledge-card1
        }
        if (currentKnowledgeCardIndex >= 1) {
            drawKnowledgeCard(g, 1); // Draw knowledge-card2
        }
        if (currentKnowledgeCardIndex >= 2) {
            drawKnowledgeCard(g, 2); // Draw knowledge-card3
        }

        // Draw the queen and sparkles if the queen animation has started
        if (queenAnimationTimer != null && queenAnimationTimer.isRunning()) {
            drawQueenAndSparkles(g);
        }
    }

    private void drawCongratsImage(Graphics g, int index) {
        if (congratsImages[index] != null) {
            int congratsWidth = congratsImages[index].getWidth(this) * 2; // Increase size by 2x
            int congratsHeight = congratsImages[index].getHeight(this) * 2; // Increase size by 2x
            int x = (getWidth() - congratsWidth) / 2; // Center horizontally
            int y = 50; // Position at the top with a small margin (same for all images)
            g.drawImage(congratsImages[index], x, y, congratsWidth, congratsHeight, this);
        }
    }

    private void drawKnowledgeCard(Graphics g, int index) {
        if (knowledgeCards[index] != null) {
            int cardWidth = knowledgeCards[index].getWidth(this) * 2;
            int cardHeight = knowledgeCards[index].getHeight(this) * 2;
            int x = (getWidth() / 4) * (index + 1) - (cardWidth / 2); // Position cards horizontally (left, middle, right)
            int y = getHeight() - cardHeight - 1; // Position at the bottom with a small margin
            x += index * 35;
            g.drawImage(knowledgeCards[index], x, y, cardWidth, cardHeight, this);
        }
    }

    private void drawQueenAndSparkles(Graphics g) {
        // Draw the queen's image
        int queenWidth = queenBinaryImage.getWidth(this);
        int queenHeight = queenBinaryImage.getHeight(this);
        int screenHeight = getHeight();

        newQueenHeight = (int) (screenHeight / 3 * queenScale);
        newQueenWidth = (int) ((double) queenWidth / queenHeight * newQueenHeight);

        // Position the queen on the left side
        queenX = 195; // Fixed x position on the left side
        queenY = (screenHeight - newQueenHeight) / 2 + 20; // Center vertically

        g.drawImage(queenBinaryImage, queenX, queenY, newQueenWidth, newQueenHeight, this);

        // Draw sparkles around the queen
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 20; i++) { // Increase the number of sparkles
            // Randomly position sparkles around the queen (but not inside her body)
            int sparkleX, sparkleY;
            do {
                sparkleX = queenX + (int) (Math.random() * (newQueenWidth + 100)) - 50; // Wider scatter area
                sparkleY = queenY + (int) (Math.random() * (newQueenHeight + 100)) - 50; // Wider scatter area
            } while (sparkleX >= queenX && sparkleX <= queenX + newQueenWidth && sparkleY >= queenY && sparkleY <= queenY + newQueenHeight);

            // Randomize sparkle size
            int sparkleSize = 5 + (int) (Math.random() * 5); // Vary size between 5 and 10

            // Use a solid white color with the current alpha value
            g2d.setColor(new Color(255, 255, 255, sparkleAlpha));

            // Draw the sparkle as a star shape
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
        // Create a JFrame to test the GameEndingScreen
        JFrame frame = new JFrame("Game Ending Screen");
        GameEndingScreen panel = new GameEndingScreen(frame);
        frame.add(panel);
        frame.setSize(800, 600); // Set the size of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}