package object.com;

import Main.com.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Windows extends SuperObject {
    GamePanel gp;
    public OBJ_Windows(GamePanel gp) {
        this.gp = gp;
        name = "Windows";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/windows.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
