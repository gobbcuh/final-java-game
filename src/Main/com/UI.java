package Main.com;

import object.com.OBJ_Key;
import object.com.OBJ_HSInstruction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage keyImage;
    BufferedImage hsInstructionImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    private long startTime;
    private boolean showInstruction = true;
    private int instructionY = -500;

    public UI(GamePanel gp) {
        this.gp = gp;

        arial_40 = new Font("Lucida Console", Font.BOLD, 40);
        arial_80B = new Font("Lucida Console", Font.BOLD, 55);

        OBJ_Key key = new OBJ_Key(gp);
        keyImage = key.image;

        OBJ_HSInstruction hsInstruction = new OBJ_HSInstruction(gp);
        hsInstructionImage = hsInstruction.image;

        int scaledWidth = (int) (hsInstructionImage.getWidth() * 0.33);
        int scaledHeight = (int) (hsInstructionImage.getHeight() * 0.33);
        hsInstructionImage = scaleImage(hsInstructionImage, scaledWidth, scaledHeight);

        startTime = System.currentTimeMillis();
    }

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

    private void drawOutlinedText(Graphics2D g2, String text, int x, int y, Color textColor, Color outlineColor) {
        // Draw the outline by drawing the text in the outline color with slight offsets
        g2.setColor(outlineColor);
        g2.drawString(text, x - 1, y - 1);
        g2.drawString(text, x + 1, y - 1);
        g2.drawString(text, x - 1, y + 1);
        g2.drawString(text, x + 1, y + 1);

        // Draw the actual text in the center
        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    public void draw(Graphics2D g2) {
        if (gameFinished) {
            g2.setFont(arial_40);
            String text;
            int textLength;
            int x;
            int y;

            text = "You found the knowledge!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 - (gp.tileSize * 3);
            drawOutlinedText(g2, text, x, y, Color.yellow, Color.black);

            g2.setFont(arial_80B);
            text = "Congratulations!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 + (gp.tileSize * 2);
            drawOutlinedText(g2, text, x, y, Color.yellow, Color.black);

            gp.gameThread = null;
        } else {
            g2.setFont(arial_40);
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);

            // Draw the key count with an outline
            String keyText = "x " + gp.player.hasKey;
            int x = 74;
            int y = 65;
            drawOutlinedText(g2, keyText, x, y, Color.white, Color.black);

            // MESSAGE
            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(30F));
                x = gp.tileSize / 2;
                y = gp.tileSize * 5;
                drawOutlinedText(g2, message, x, y, Color.white, Color.black);

                messageCounter++;
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }

            // HS-instruction animation
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            if (elapsedTime < 10000) {
                showInstruction = true;
                if (instructionY < gp.screenHeight / 2 - hsInstructionImage.getHeight() / 2) {
                    instructionY += 5;
                }
            } else if (elapsedTime >= 10000) {
                if (instructionY > -hsInstructionImage.getHeight()) {
                    instructionY -= 5;
                } else {
                    showInstruction = false;
                }
            }

            if (showInstruction) {
                x = gp.screenWidth / 2 - hsInstructionImage.getWidth() / 2;
                g2.drawImage(hsInstructionImage, x, instructionY, null);
            }
        }
    }
}