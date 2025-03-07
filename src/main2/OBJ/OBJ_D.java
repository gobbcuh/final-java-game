package main2.OBJ;

import main2.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_D extends SuperObject{

    GamePanel gp;

    public OBJ_D(GamePanel gp){

        this.gp = gp;

        name = "D";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/object/d.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}