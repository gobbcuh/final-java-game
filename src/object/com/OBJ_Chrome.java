package object.com;

import Main.com.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Chrome extends SuperObject {
    GamePanel gp;
    public OBJ_Chrome(GamePanel gp) {
        this.gp = gp;
        name = "Chrome";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chrome.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
