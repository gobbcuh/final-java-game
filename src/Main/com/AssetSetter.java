package Main.com;
import object.com.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Chess(gp);
        gp.obj[0].worldX = 33 * gp.tileSize;
        gp.obj[0].worldY = 9 * gp.tileSize;

        gp.obj[1] = new OBJ_Door(gp);
        gp.obj[1].worldX = 33 * gp.tileSize;
        gp.obj[1].worldY = 9 * gp.tileSize;

        gp.obj[2] = new OBJ_Key(gp);
        gp.obj[2].worldX = 23 * gp.tileSize;
        gp.obj[2].worldY = 37 * gp.tileSize;

        gp.obj[3] = new OBJ_Computer(gp);
        gp.obj[3].worldX = 43 * gp.tileSize;
        gp.obj[3].worldY = 37 * gp.tileSize;

        gp.obj[4] = new OBJ_SystemUnit(gp);
        gp.obj[4].worldX = 8 * gp.tileSize;
        gp.obj[4].worldY = 17 * gp.tileSize;

        gp.obj[5] = new OBJ_Mouse(gp);
        gp.obj[5].worldX = 5 * gp.tileSize;
        gp.obj[5].worldY = 30 * gp.tileSize;

        gp.obj[6] = new OBJ_Speaker(gp);
        gp.obj[6].worldX = 11 * gp.tileSize;
        gp.obj[6].worldY = 39 * gp.tileSize;

        gp.obj[7] = new OBJ_Printer(gp);
        gp.obj[7].worldX = 21 * gp.tileSize;
        gp.obj[7].worldY = 44 * gp.tileSize;

        gp.obj[8] = new OBJ_Chrome(gp);
        gp.obj[8].worldX = 44 * gp.tileSize;
        gp.obj[8].worldY = 23 * gp.tileSize;

        gp.obj[9] = new OBJ_Windows(gp);
        gp.obj[9].worldX = 27 * gp.tileSize;
        gp.obj[9].worldY = 31 * gp.tileSize;

        gp.obj[10] = new OBJ_Facebook(gp);
        gp.obj[10].worldX = 24 * gp.tileSize;
        gp.obj[10].worldY = 4 * gp.tileSize;

        gp.obj[11] = new OBJ_Picsart(gp);
        gp.obj[11].worldX = 43 * gp.tileSize;
        gp.obj[11].worldY = 11 * gp.tileSize;
    }
}