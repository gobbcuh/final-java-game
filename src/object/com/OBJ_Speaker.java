package object.com;

import Main.com.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Speaker extends SuperObject {
    GamePanel gp;
    public OBJ_Speaker(GamePanel gp) {
        this.gp = gp;
        name = "Speaker";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/speaker.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
