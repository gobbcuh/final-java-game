package object.com;

import Main.com.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_HSInstruction extends SuperObject {
    GamePanel gp;
    public OBJ_HSInstruction(GamePanel gp) {
        this.gp = gp;
        name = "Instruction";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/HS-instruction.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
