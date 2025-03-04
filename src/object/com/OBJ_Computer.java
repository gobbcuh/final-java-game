package object.com;

import Main.com.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Computer extends SuperObject {
    GamePanel gp;
    public OBJ_Computer(GamePanel gp) {
        this.gp = gp;
        name = "Computer";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/computer.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
