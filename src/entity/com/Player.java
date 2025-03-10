package entity.com;

import Main.com.GamePanel;
import Main.com.KeyHandler;
import Main.com.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    int standCounter = 0;
    boolean moving = false;
    int pixelCounter = 0;

    private boolean hardwarePhase = true;
    private boolean softwarePhase = false;
    private long startTime;
    private boolean hardwareTextShown = false;

    // Hardware items
    private String[] hardwareItems = {"Computer", "System Unit", "Mouse", "Speaker", "Printer"};
    private int hardwareItemsCollected = 0;

    // Software items
    private String[] softwareItems = {"Chrome", "Windows", "Facebook", "Picsart"};
    private int softwareItemsCollected = 0;

    public List<Map<String, Object>> collectedItems = new ArrayList<>();

    public boolean hasKnowledgeCard = false;
    private float zoomScale = 1.0f;
    private final float maxZoomScale = 4.0f;
    private final float zoomSpeed = 0.05f;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 1;
        solidArea.y = 1;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 46;
        solidArea.height = 46;

        setDefaultValues();
        getPlayerImage();

        startTime = System.currentTimeMillis();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setup("1W");
        up2 = setup("2W");
        down1 = setup("1S");
        down2 = setup("2S");
        left1 = setup("1A");
        left2 = setup("2A");
        right1 = setup("1D");
        right2 = setup("2D");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime > 10000 && !hardwareTextShown) {
            gp.ui.showMessage("Category: Hardware");
            gp.playSE(5);
            hardwareTextShown = true;
        }

        if (hasKnowledgeCard && zoomScale < maxZoomScale) {
            zoomScale += zoomSpeed;
        }

        for (int i = collectedItems.size() - 1; i >= 0; i--) {
            Map<String, Object> item = collectedItems.get(i);
            long collectedTime = (long) item.get("collectedTime");
            if (currentTime - collectedTime > 5000) {
                collectedItems.remove(i);
            }
        }

        if (moving == false) {
            if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed || keyH.rightPressed) {

                if (keyH.upPressed == true) {
                    direction = "up";
                } else if (keyH.downPressed == true) {
                    direction = "down";
                } else if (keyH.leftPressed == true) {
                    direction = "left";
                } else if (keyH.rightPressed == true) {
                    direction = "right";
                }

                moving = true;

                // CHECK TILE COLLISION
                collisionOn = false;
                gp.cChecker.checkTile(this);

                // CHECK OBJECT COLLISION
                int objIndex = gp.cChecker.checkObject(this, true);
                pickUpObject(objIndex);
            } else {
                standCounter++;

                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }
        if (moving == true) {
            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY = worldY - speed;
                        break;
                    case "down":
                        worldY = worldY + speed;
                        break;
                    case "left":
                        worldX = worldX - speed;
                        break;
                    case "right":
                        worldX = worldX + speed;
                        break;
                }
            }
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
            pixelCounter += speed;
            if (pixelCounter == 48) {
                moving = false;
                pixelCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;

            if (objectName.equals("Lock")) {
                if (hasKey > 0) {
                    gp.playSE(3);
                    gp.obj[i] = null;
                    hasKey--;
                    gp.ui.showMessage("Unlocked!");
                } else {
                    gp.ui.showMessage("You need a key!");
                }
                return;
            }

            if (objectName.equals("Key")) {
                gp.playSE(1);
                hasKey++;
                gp.obj[i] = null;
                gp.ui.showMessage("You got a key!");
                return;
            }

            if (objectName.equals("Chess")) {
                if (hardwareItemsCollected == hardwareItems.length && softwareItemsCollected == softwareItems.length) {
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    hasKnowledgeCard = true;
                } else {
                    gp.ui.showMessage("You need to collect all items first!");
                }
                return;
            }

            if (hardwarePhase) {
                for (String item : hardwareItems) {
                    if (objectName.equals(item)) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hardwareItemsCollected++;
                        gp.ui.showMessage(item + " is a hardware!");
                        checkHardwareCompletion();

                        Map<String, Object> collectedItem = new HashMap<>();
                        collectedItem.put("name", item);
                        collectedItem.put("definition", getHardwareDefinition(item));
                        collectedItem.put("collectedTime", System.currentTimeMillis());
                        collectedItems.add(collectedItem);

                        return;
                    }
                }
                gp.playSE(7);
                gp.ui.showMessage("Hint: Hardware can be touched!");
            } else if (softwarePhase) {
                for (String item : softwareItems) {
                    if (objectName.equals(item)) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        softwareItemsCollected++;
                        gp.ui.showMessage(item + " is a software!");
                        checkSoftwareCompletion();

                        Map<String, Object> collectedItem = new HashMap<>();
                        collectedItem.put("name", item);
                        collectedItem.put("definition", getSoftwareDefinition(item));
                        collectedItem.put("collectedTime", System.currentTimeMillis());
                        collectedItems.add(collectedItem);

                        return;
                    }
                }
                gp.playSE(7);
                gp.ui.showMessage("Hint: Software is the program that runs this game!");
            }
        }
    }

    private String getHardwareDefinition(String item) {
        switch (item) {
            case "Computer":
                return "A computer is a machine that can do many tasks, like playing games and drawing pictures.";
            case "System Unit":
                return "The system unit is the brain of the computer. It holds all the important parts inside.";
            case "Mouse":
                return "A mouse helps you move the cursor on the screen and click on things.";
            case "Speaker":
                return "Speakers let you hear sounds and music from the computer.";
            case "Printer":
                return "A printer makes paper copies of what you see on the screen.";
            default:
                return "";
        }
    }

    private String getSoftwareDefinition(String item) {
        switch (item) {
            case "Chrome":
                return "Chrome is a program that lets you visit websites and watch videos.";
            case "Windows":
                return "Windows is the program that makes the computer work and shows you all the apps.";
            case "Facebook":
                return "Facebook is a place where you can talk to friends and share pictures.";
            case "Picsart":
                return "Picsart is a program that lets you edit photos and make them look fun.";
            default:
                return "";
        }
    }

    private void checkHardwareCompletion() {
        if (hardwareItemsCollected == hardwareItems.length) {
            hardwarePhase = false;
            softwarePhase = true;
            gp.ui.showMessage("Category: Software");
            gp.playSE(6);
        }
    }

    private void checkSoftwareCompletion() {
        if (softwareItemsCollected == softwareItems.length) {
            gp.ui.showMessage("You have collected all software items!");
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);

        if (hasKnowledgeCard) {
            BufferedImage knowledgeCardImage = getKnowledgeCardImage();
            int originalWidth = knowledgeCardImage.getWidth();
            int originalHeight = knowledgeCardImage.getHeight();
            int zoomWidth = (int) (originalWidth * zoomScale);
            int zoomHeight = (int) (originalHeight * zoomScale);
            int x = gp.screenWidth / 2 - zoomWidth / 2;
            int y = gp.screenHeight / 2 - zoomHeight / 2;

            g2.drawImage(knowledgeCardImage, x, y, zoomWidth, zoomHeight, null);
        }
    }

    private BufferedImage getKnowledgeCardImage() {
        try {
            return ImageIO.read(getClass().getResourceAsStream("/objects/HS-knowledge-card.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}