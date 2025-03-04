package Main.com;

import object.com.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class UI {
    GamePanel gp;
    Font arial_40, arial_80B;
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

        OBJ_HSInstruction hsInstruction = new OBJ_HSInstruction(gp);
        hsInstructionImage = hsInstruction.image;

        int scaledWidth = (int) (hsInstructionImage.getWidth() * 0.80);
        int scaledHeight = (int) (hsInstructionImage.getHeight() * 0.80);
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
        g2.setColor(outlineColor);
        g2.drawString(text, x - 1, y - 1);
        g2.drawString(text, x + 1, y - 1);
        g2.drawString(text, x - 1, y + 1);
        g2.drawString(text, x + 1, y + 1);

        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int width, int margin, Font font, Color textColor) {
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics();
        int lineHeight = metrics.getHeight();

        int topMargin = (int) (0.55 * Toolkit.getDefaultToolkit().getScreenResolution());

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int currentY = y + topMargin;

        for (String word : words) {
            String testLine = currentLine + word + " ";
            int testWidth = metrics.stringWidth(testLine);

            if (testWidth > width - 2 * margin) {
                // Draw the current line
                g2.setColor(textColor);
                g2.drawString(currentLine.toString(), x + margin, currentY);
                currentY += lineHeight;
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }

        if (!currentLine.isEmpty()) {
            g2.setColor(textColor);
            g2.drawString(currentLine.toString(), x + margin, currentY);
        }
    }

    public void drawCollectedItems(Graphics2D g2) {
        int startX = 20;
        int startY = gp.screenHeight - 100;
        int itemSpacing = 80;

        for (int i = 0; i < gp.player.collectedItems.size(); i++) {
            Map<String, Object> item = gp.player.collectedItems.get(i);
            String name = (String) item.get("name");
            String definition = (String) item.get("definition");

            BufferedImage itemImage = getItemImage(name);
            if (itemImage != null) {
                g2.drawImage(itemImage, startX, startY + (i * itemSpacing), 50, 50, null);
            }

            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.WHITE);
            g2.drawString(definition, startX + 60, startY + 30 + (i * itemSpacing));
        }
    }

    private BufferedImage getItemImage(String itemName) {
        switch (itemName) {
            case "Computer":
                return new OBJ_Computer(gp).image;
            case "System Unit":
                return new OBJ_SystemUnit(gp).image;
            case "Mouse":
                return new OBJ_Mouse(gp).image;
            case "Speaker":
                return new OBJ_Speaker(gp).image;
            case "Printer":
                return new OBJ_Printer(gp).image;
            case "Chrome":
                return new OBJ_Chrome(gp).image;
            case "Windows":
                return new OBJ_Windows(gp).image;
            case "Facebook":
                return new OBJ_Facebook(gp).image;
            case "Picsart":
                return new OBJ_Picsart(gp).image;
            default:
                return null;
        }
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
            drawOutlinedText(g2, text, x, y, Color.green, Color.black);

            g2.setFont(arial_80B);
            text = "Congratulations!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 + (gp.tileSize * 3);
            drawOutlinedText(g2, text, x, y, Color.RED, Color.white);

            gp.gameThread = null;
        } else {
            // MESSAGE
            if (messageOn) {
                g2.setFont(arial_40.deriveFont(25F));
                int textLength = (int) g2.getFontMetrics().getStringBounds(message, g2).getWidth();
                int x = gp.screenWidth / 2 - textLength / 2;
                int y = gp.tileSize * 3;
                drawOutlinedText(g2, message, x, y, Color.white, Color.black);

                messageCounter++;
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }

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
                int x = gp.screenWidth / 2 - hsInstructionImage.getWidth() / 2;
                int y = instructionY;

                g2.drawImage(hsInstructionImage, x, y, null);

                String instructionText = "Consider the given category—Hardware or Software—and collect only the correct items based on it. Along the way, find and collect a key to unlock the knowledge card. Remember, you won’t be able to open the lock unless you’ve collected all the hardware and software items!";
                Font instructionFont = arial_40.deriveFont(15F);
                int margin = (int) (0.3 * Toolkit.getDefaultToolkit().getScreenResolution());
                drawWrappedText(g2, instructionText, x, y, hsInstructionImage.getWidth(), margin, instructionFont, Color.black);
            }

            drawCollectedItems(g2);
        }
    }
}