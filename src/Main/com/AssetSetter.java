package Main.com;
import object.com.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new OBJ_Computer(gp);
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY= 7 * gp.tileSize;

        gp.obj[1] = new OBJ_SystemUnit(gp);
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 40 * gp.tileSize;

        gp.obj[2] = new OBJ_Mouse(gp);
        gp.obj[2].worldX = 38 * gp.tileSize;
        gp.obj[2].worldY = 8 * gp.tileSize;

        gp.obj[3] = new OBJ_Speaker(gp);
        gp.obj[3].worldX =  10 * gp.tileSize;
        gp.obj[3].worldY =  11 * gp.tileSize;

        gp.obj[4] = new OBJ_Printer(gp);
        gp.obj[4].worldX =  8 * gp.tileSize;
        gp.obj[4].worldY =  28 * gp.tileSize;
    }
}
