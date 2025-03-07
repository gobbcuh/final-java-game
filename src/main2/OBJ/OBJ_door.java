package main2.OBJ;

import main2.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_door extends SuperObject{

    GamePanel gp;

    public OBJ_door(GamePanel gp){

        this.gp = gp;
        name = "Door";

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/object/door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);


        } catch (IOException e){
            e.printStackTrace();
        }
        collision = true;
    }
}