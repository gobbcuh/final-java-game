package Main.com; // draft 766

import main2.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameIntroScreen extends JPanel {
    private Image[] backgrounds;
    private Timer starTimer;
    private Timer backgroundTimer;
    private Timer loadingTextTimer;
    private Timer dotAnimationTimer;
    private int starAlpha = 255;
    private boolean fadeOut = true;
    private Clip backgroundMusic;
    private JFrame parentFrame;
    private int currentBackgroundIndex = 0;
    private String[] loadingTexts = {"Welcome to the Game", "Please wait for a moment", "Starting the Game"};
    private int currentLoadingTextIndex = 0;
    private boolean isLastTextDisplayed = false;
    private int dotCount = 0;
    private final int maxDots = 3;
    private boolean buttonsVisible = false;
    private float buttonScale = 0.1f;
    private float fadeAlpha = 1.0f;

    public GameIntroScreen(JFrame frame) {
        this.parentFrame = frame;

        backgrounds = new Image[4];
        for (int i = 0; i < 4; i++) {
            backgrounds[i] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/loading-bg" + (i + 1) + ".png").getImage();
        }

        playBackgroundMusic();

        starTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadeOut) {
                    starAlpha -= 25;
                    if (starAlpha <= 50) fadeOut = false;
                } else {
                    starAlpha += 25;
                    if (starAlpha >= 255) fadeOut = true;
                }
                repaint();
            }
        });
        starTimer.start();

        backgroundTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBackgroundIndex = (currentBackgroundIndex + 1) % 4;
                repaint();
            }
        });
        backgroundTimer.start();

        loadingTextTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isLastTextDisplayed) {
                    currentLoadingTextIndex = (currentLoadingTextIndex + 1) % loadingTexts.length;
                    if (currentLoadingTextIndex == loadingTexts.length - 1) {
                        isLastTextDisplayed = true;
                        loadingTextTimer.stop();
                        dotAnimationTimer.stop();
                        Timer pauseTimer = new Timer(5000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showButtons();
                            }
                        });
                        pauseTimer.setRepeats(false);
                        pauseTimer.start();
                    }
                    dotCount = 0;
                    dotAnimationTimer.restart();
                    repaint();
                }
            }
        });
        loadingTextTimer.start();

        dotAnimationTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dotCount < maxDots) {
                    dotCount++;
                    repaint();
                } else {
                    dotAnimationTimer.stop();
                }
            }
        });
    }

    private void showButtons() {
        buttonsVisible = true;
        playSoundEffect("C:/Users/User/IdeaProjects/final/out/production/final/Main/sound-effect1.wav");

        ImageIcon startButtonIcon = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/start-button.png");
        ImageIcon exitButtonIcon = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/quit-button.png");
        ImageIcon startButtonHoverIcon = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/start-button2.png");
        ImageIcon exitButtonHoverIcon = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/quit-button2.png");

        Image startButtonImage = startButtonIcon.getImage().getScaledInstance(
                startButtonIcon.getIconWidth() + 100,
                startButtonIcon.getIconHeight() + 40,
                Image.SCALE_SMOOTH
        );
        Image exitButtonImage = exitButtonIcon.getImage().getScaledInstance(
                exitButtonIcon.getIconWidth() + 100,
                exitButtonIcon.getIconHeight() + 40,
                Image.SCALE_SMOOTH
        );
        Image startButtonHoverImage = startButtonHoverIcon.getImage().getScaledInstance(
                startButtonHoverIcon.getIconWidth() + 100,
                startButtonHoverIcon.getIconHeight() + 40,
                Image.SCALE_SMOOTH
        );
        Image exitButtonHoverImage = exitButtonHoverIcon.getImage().getScaledInstance(
                exitButtonHoverIcon.getIconWidth() + 100,
                exitButtonHoverIcon.getIconHeight() + 40,
                Image.SCALE_SMOOTH
        );

        JButton startButton = new JButton(new ImageIcon(startButtonImage));
        JButton exitButton = new JButton(new ImageIcon(exitButtonImage));
        JButton startButtonHover = new JButton(new ImageIcon(startButtonHoverImage));
        JButton exitButtonHover = new JButton(new ImageIcon(exitButtonHoverImage));

        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        startButtonHover.setBorderPainted(false);
        startButtonHover.setContentAreaFilled(false);
        exitButtonHover.setBorderPainted(false);
        exitButtonHover.setContentAreaFilled(false);

        int buttonWidth = startButton.getIcon().getIconWidth();
        int buttonHeight = startButton.getIcon().getIconHeight();
        int x = (getWidth() - buttonWidth) / 2;
        int startY = (getHeight() - (2 * buttonHeight + 10)) / 2 + 70;
        int exitY = startY + buttonHeight + 10;

        buttonScale = 0.1f;
        startButton.setBounds(
                (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                (int) (startY + (buttonHeight * (1 - buttonScale)) / 2),
                (int) (buttonWidth * buttonScale),
                (int) (buttonHeight * buttonScale)
        );
        exitButton.setBounds(
                (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                (int) (exitY + (buttonHeight * (1 - buttonScale)) / 2),
                (int) (buttonWidth * buttonScale),
                (int) (buttonHeight * buttonScale)
        );
        startButtonHover.setBounds(
                (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                (int) (startY + (buttonHeight * (1 - buttonScale)) / 2),
                (int) (buttonWidth * buttonScale),
                (int) (buttonHeight * buttonScale)
        );
        exitButtonHover.setBounds(
                (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                (int) (exitY + (buttonHeight * (1 - buttonScale)) / 2),
                (int) (buttonWidth * buttonScale),
                (int) (buttonHeight * buttonScale)
        );

        setLayout(null);
        add(startButton);
        add(exitButton);
        add(startButtonHover);
        add(exitButtonHover);

        startButtonHover.setVisible(false);
        exitButtonHover.setVisible(false);

        Timer zoomTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonScale < 1.0f) {
                    buttonScale += 0.05f;
                    startButton.setBounds(
                            (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                            (int) (startY + (buttonHeight * (1 - buttonScale)) / 2),
                            (int) (buttonWidth * buttonScale),
                            (int) (buttonHeight * buttonScale)
                    );
                    exitButton.setBounds(
                            (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                            (int) (exitY + (buttonHeight * (1 - buttonScale)) / 2),
                            (int) (buttonWidth * buttonScale),
                            (int) (buttonHeight * buttonScale)
                    );
                    startButtonHover.setBounds(
                            (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                            (int) (startY + (buttonHeight * (1 - buttonScale)) / 2),
                            (int) (buttonWidth * buttonScale),
                            (int) (buttonHeight * buttonScale)
                    );
                    exitButtonHover.setBounds(
                            (int) (x + (buttonWidth * (1 - buttonScale)) / 2),
                            (int) (exitY + (buttonHeight * (1 - buttonScale)) / 2),
                            (int) (buttonWidth * buttonScale),
                            (int) (buttonHeight * buttonScale)
                    );
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        zoomTimer.start();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSoundEffect("C:/Users/User/IdeaProjects/final/out/production/final/Main/click-intro-button.wav");
                startButton.setVisible(false);
                startButtonHover.setVisible(true);
                Timer hoverTimer = new Timer(200, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        transitionToNextScreen();
                    }
                });
                hoverTimer.setRepeats(false);
                hoverTimer.start();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSoundEffect("C:/Users/User/IdeaProjects/final/out/production/final/Main/click-intro-button.wav");
                exitButton.setVisible(false);
                exitButtonHover.setVisible(true);
                Timer hoverTimer = new Timer(200, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                hoverTimer.setRepeats(false);
                hoverTimer.start();
            }
        });

        repaint();
    }

    private void transitionToNextScreen() {
        GameMainScreen gameMainScreen = new GameMainScreen(parentFrame);
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(gameMainScreen);
        parentFrame.revalidate();
        parentFrame.repaint();
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

    private void playSoundEffect(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
        g2d.drawImage(backgrounds[currentBackgroundIndex], 0, 0, getWidth(), getHeight(), this);
        g2d.setColor(new Color(255, 255, 255, starAlpha));
        for (int i = 0; i < 20; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * getHeight());
            g2d.fillOval(x, y, 3, 3);
        }
        if (!buttonsVisible) {
            String loadingText = loadingTexts[currentLoadingTextIndex];
            String dots = "";
            for (int i = 0; i < dotCount; i++) {
                dots += " .";
            }
            String fullText = loadingText + dots;
            g2d.setFont(new Font("Lucida Console", Font.BOLD, 16));
            int textWidth = g2d.getFontMetrics().stringWidth(fullText);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() - 40;
            g2d.setColor(Color.BLACK);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        g2d.drawString(fullText, x + i, y + j);
                    }
                }
            }
            g2d.setColor(Color.WHITE);
            g2d.drawString(fullText, x, y);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Amazing Adventures");
        GameIntroScreen panel = new GameIntroScreen(frame);
        frame.add(panel);
        frame.setSize(384, 265);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}

class GameMainScreen extends JPanel {
    private Image[] backgrounds;
    private Timer backgroundTimer;
    private int currentBackgroundIndex = 0;
    private JFrame parentFrame;
    private Image queenBinaryImage;
    private float queenScale = 0.1f;
    private Timer queenAnimationTimer;
    private int sparkleAlpha = 255;
    private boolean sparkleFadeOut = true;
    private int queenX;
    private int queenY;
    private int newQueenWidth;
    private int newQueenHeight;
    private Timer queenMoveTimer;
    private boolean queenMoveStarted = false;

    private Image introTextBoxImage;
    private int introTextBoxX;
    private int introTextBoxY;
    private int introTextBoxWidth;
    private int introTextBoxHeight;
    private Timer introTextBoxMoveTimer;
    private boolean introTextBoxVisible = false;
    private boolean introTextBoxStopped = false;

    private Image subTextBoxImage;

    private String queenName = "QUEEN BINARY";
    private int queenNameIndex = 0;
    private int queenNameDisappearIndex = queenName.length();
    private Timer queenNameTimer;
    private Timer queenNameDisappearTimer;
    private boolean queenNameVisible = false;
    private boolean queenNameAnimationComplete = false;

    private String[] textWords;
    private int currentWordIndex = 0;
    private Timer wordDisplayTimer;

    public GameMainScreen(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        this.backgrounds = new Image[3];
        this.backgrounds[0] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/bg-first.png").getImage();
        this.backgrounds[1] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/bg-first2.png").getImage();
        this.backgrounds[2] = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/bg-first3.png").getImage();

        queenBinaryImage = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-binary.png").getImage();
        introTextBoxImage = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/intro-text-box.png").getImage();
        subTextBoxImage = new ImageIcon("C:/Users/User/IdeaProjects/final/out/production/final/Main/sub-text-box.png").getImage();

        String text = "Brave Sophia, something bad happened! A sneaky bug named Glitch stole all the kingdom's knowledge and broke it into pieces! There are three special pieces of knowledge lost: Binary, Hardware and Software, and the Internet. You’re the hero we need! Explore these topics, solve the puzzles, and find the lost knowledge. Only you can stop Glitch and save our world!";
        textWords = text.split(" ");

        wordDisplayTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentWordIndex < textWords.length) {
                    currentWordIndex++;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        backgroundTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
                repaint();
            }
        });
        backgroundTimer.start();

        // Timer for the queen's animation
        queenAnimationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenScale < 1.0f) {
                    queenScale += 0.02f;
                }

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
        queenAnimationTimer.start();

        playSoundEffect("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-appear-sound.wav");

        Timer delayTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queenNameVisible = true;
                queenNameTimer.start();
            }
        });
        delayTimer.setRepeats(false);
        delayTimer.start();

        queenNameTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenNameIndex < queenName.length()) {
                    queenNameIndex++;
                    if (queenNameIndex == 1) {
                        Timer soundDelayTimer = new Timer(4000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playQueenTextIntroSound();
                            }
                        });
                        soundDelayTimer.setRepeats(false);
                        soundDelayTimer.start();
                    }
                    repaint();
                } else {
                    queenNameTimer.stop();
                    Timer pauseTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            queenNameDisappearTimer.start();
                        }
                    });
                    pauseTimer.setRepeats(false);
                    pauseTimer.start();
                }
            }
        });

        queenNameDisappearTimer = new Timer(150, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenNameDisappearIndex > 0) {
                    queenNameDisappearIndex--;
                    repaint();
                } else {
                    queenNameDisappearTimer.stop();
                    queenNameVisible = false;
                    queenMoveStarted = true;
                    queenMoveTimer.start();

                    introTextBoxX = queenX - introTextBoxWidth - 150;
                    introTextBoxVisible = true;

                    playSoundEffect("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-box-slide-sound.wav");

                    introTextBoxMoveTimer.start();
                }
            }
        });

        queenMoveTimer = new Timer(17, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (queenX > 265) {
                    queenX -= 7;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        introTextBoxMoveTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (introTextBoxX < getWidth() - introTextBoxWidth - 320) {
                    introTextBoxX += 5;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    introTextBoxStopped = true;
                    repaint();

                    wordDisplayTimer.start();
                }
            }
        });



    }

    private void playSoundEffect(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playQueenTextIntroSound() {
        try {
            File soundFile = new File("C:/Users/User/IdeaProjects/final/out/production/final/Main/queen-text-intro-sound.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgrounds[currentBackgroundIndex], 0, 0, getWidth(), getHeight(), this);

        if (introTextBoxVisible && introTextBoxImage != null) {
            introTextBoxWidth = introTextBoxImage.getWidth(this);
            introTextBoxHeight = introTextBoxImage.getHeight(this);
            introTextBoxY = (getHeight() - introTextBoxHeight) / 2;
            g.drawImage(introTextBoxImage, introTextBoxX, introTextBoxY, introTextBoxWidth, introTextBoxHeight, this);

            if (subTextBoxImage != null) {
                int subTextBoxWidth = subTextBoxImage.getWidth(this);
                int subTextBoxHeight = subTextBoxImage.getHeight(this);
                int subTextBoxX = introTextBoxX + (introTextBoxWidth - subTextBoxWidth) / 2;
                int subTextBoxY = introTextBoxY - subTextBoxHeight / 2;
                g.drawImage(subTextBoxImage, subTextBoxX, subTextBoxY, subTextBoxWidth, subTextBoxHeight, this);
            }

            if (introTextBoxStopped && queenMoveTimer.isRunning() == false) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setFont(new Font("Century Gothic", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();

                int marginX = (int) (0.7 * 72);
                int marginY = (int) (0.3 * 72);

                int textAreaX = introTextBoxX + marginX;
                int textAreaY = introTextBoxY + marginY + 30;
                int textAreaWidth = introTextBoxWidth - 2 * marginX;
                int textAreaHeight = introTextBoxHeight - 2 * marginY;

                StringBuilder displayedText = new StringBuilder();
                for (int i = 0; i < currentWordIndex; i++) {
                    displayedText.append(textWords[i]).append(" ");
                }

                String[] words = displayedText.toString().split(" ");
                StringBuilder line = new StringBuilder();
                int y = textAreaY + fm.getAscent();
                for (String word : words) {
                    String testLine = line.length() == 0 ? word : line + " " + word;
                    int testWidth = fm.stringWidth(testLine);
                    if (testWidth > textAreaWidth) {
                        drawLine(g2d, line.toString(), textAreaX, y, fm);
                        line = new StringBuilder(word);
                        y += fm.getHeight();
                    } else {
                        line.append(line.length() == 0 ? word : " " + word);
                    }
                }
                if (line.length() > 0) {
                    drawLine(g2d, line.toString(), textAreaX, y, fm);
                }
            }
        }

        // Drawing the queen's image
        int queenWidth = queenBinaryImage.getWidth(this);
        int queenHeight = queenBinaryImage.getHeight(this);
        int screenHeight = getHeight();
        int screenWidth = getWidth();

        newQueenHeight = (int) (screenHeight / 3 * queenScale);
        newQueenWidth = (int) ((double) queenWidth / queenHeight * newQueenHeight);

        if (!queenMoveStarted) {
            queenX = (screenWidth - newQueenWidth) / 2;
            queenY = (screenHeight - newQueenHeight) / 2 + 30;
        }

        g.drawImage(queenBinaryImage, queenX, queenY, newQueenWidth, newQueenHeight, this);

        if (queenNameVisible) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(new Font("Lucida Console", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(queenName);
            int textHeight = fm.getHeight();

            int textX = queenX + (newQueenWidth - textWidth) / 2;
            int textY = queenY - textHeight;

            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.drawString(queenName, textX, textY);

            if (queenNameDisappearIndex == queenName.length()) {
                String animatedText = queenName.substring(0, queenNameIndex);
                g2d.setColor(Color.BLACK);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            g2d.drawString(animatedText, textX + i, textY + j);
                        }
                    }
                }
                g2d.setColor(Color.YELLOW);
                g2d.drawString(animatedText, textX, textY);
            } else {
                String remainingText = queenName.substring(0, queenNameDisappearIndex);
                String whiteText = queenName.substring(queenNameDisappearIndex);

                g2d.setColor(Color.BLACK);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            g2d.drawString(remainingText, textX + i, textY + j);
                        }
                    }
                }
                g2d.setColor(Color.YELLOW);
                g2d.drawString(remainingText, textX, textY);

                g2d.setColor(Color.BLACK);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            g2d.drawString(whiteText, textX + fm.stringWidth(remainingText) + i, textY + j);
                        }
                    }
                }
                g2d.setColor(Color.WHITE);
                g2d.drawString(whiteText, textX + fm.stringWidth(remainingText), textY);
            }
        }

        // Drawing the sparkles
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 20; i++) {
            int sparkleX, sparkleY;
            do {
                sparkleX = queenX + (int) (Math.random() * (newQueenWidth + 100)) - 50;
                sparkleY = queenY + (int) (Math.random() * (newQueenHeight + 100)) - 50;
            } while (sparkleX >= queenX && sparkleX <= queenX + newQueenWidth && sparkleY >= queenY && sparkleY <= queenY + newQueenHeight);

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

    private void drawLine(Graphics2D g2d, String line, int x, int y, FontMetrics fm) {
        String[] words = line.split(" ");
        int currentX = x;

        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z]", "");

            if (cleanWord.equalsIgnoreCase("Glitch")) {
                g2d.setColor(new Color(165, 42, 42));
            } else if (cleanWord.equalsIgnoreCase("Binary")) {
                g2d.setColor(new Color(0, 0, 255));
            } else if (cleanWord.equalsIgnoreCase("Hardware")) {
                g2d.setColor(new Color(255, 0, 0));
            } else if (cleanWord.equalsIgnoreCase("Software")) {
                g2d.setColor(new Color(0, 128, 0));
            } else if (cleanWord.equalsIgnoreCase("Internet")) {
                g2d.setColor(new Color(255, 162, 0));
            } else {
                g2d.setColor(Color.BLACK);
            }

            g2d.drawString(word, currentX, y);
            currentX += fm.stringWidth(word + " ");
        }
    }
}