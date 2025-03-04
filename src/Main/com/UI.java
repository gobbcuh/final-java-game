package Main.com;

import object.com.OBJ_Key;
import object.com.OBJ_HSInstruction; // Import the HS-instruction class

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage keyImage;
    BufferedImage hsInstructionImage; // Image for HS-instruction
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    // Variables for HS-instruction animation
    private long startTime;
    private boolean showInstruction = true; // Show immediately
    private int instructionY = -500; // Start off-screen

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Lucida Console", Font.BOLD, 40);
        arial_80B = new Font("Lucida Console", Font.BOLD, 55);

        // Load key image
        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;

        // Load HS-instruction image
        OBJ_HSInstruction hsInstruction = new OBJ_HSInstruction(gp);
        hsInstructionImage = hsInstruction.image;

        // Scale down the HS-instruction image slightly
        int scaledWidth = (int) (hsInstructionImage.getWidth() * 0.35); // Reduce size by 20%
        int scaledHeight = (int) (hsInstructionImage.getHeight() * 0.35); // Reduce size by 20%
        hsInstructionImage = scaleImage(hsInstructionImage, scaledWidth, scaledHeight);

        // Record the start time
        startTime = System.currentTimeMillis();
    }

    // Helper method to scale an image
    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        if (gameFinished) {
            g2.setFont(arial_40);
            g2.setColor(Color.yellow);

            String text;
            int textLength;
            int x;
            int y;

            text = "You found the knowledge!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 - (gp.tileSize * 3);
            g2.drawString(text, x, y);

            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 + (gp.tileSize * 2);
            g2.drawString(text, x, y);

            gp.gameThread = null;
        } else {
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            // MESSAGE
            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

                messageCounter++;
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }

            // HS-instruction animation
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            // Show image immediately
            if (elapsedTime < 10000) { // Show image for 10 seconds
                showInstruction = true;
                if (instructionY < gp.screenHeight / 2 - hsInstructionImage.getHeight() / 2) {
                    instructionY += 5; // Slide down to the middle
                }
            } else if (elapsedTime >= 10000) {
                if (instructionY > -hsInstructionImage.getHeight()) {
                    instructionY -= 5; // Slide up to disappear
                } else {
                    showInstruction = false;
                }
            }

            if (showInstruction) {
                int x = gp.screenWidth / 2 - hsInstructionImage.getWidth() / 2;
                g2.drawImage(hsInstructionImage, x, instructionY, null);
            }
        }
    }
}