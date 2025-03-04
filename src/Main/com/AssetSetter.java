package Main.com;
import object.com.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;

        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 40 * gp.tileSize;

        gp.obj[2] = new OBJ_Key(gp);
        gp.obj[2].worldX = 38 * gp.tileSize;
        gp.obj[2].worldY = 8 * gp.tileSize;

        gp.obj[3] = new OBJ_Door(gp);
        gp.obj[3].worldX = 10 * gp.tileSize;
        gp.obj[3].worldY = 11 * gp.tileSize;

        gp.obj[4] = new OBJ_Door(gp);
        gp.obj[4].worldX = 8 * gp.tileSize;
        gp.obj[4].worldY = 28 * gp.tileSize;

        gp.obj[5] = new OBJ_Door(gp);
        gp.obj[5].worldX = 12 * gp.tileSize;
        gp.obj[5].worldY = 22 * gp.tileSize;

        gp.obj[6] = new OBJ_Chess(gp);
        gp.obj[6].worldX = 10 * gp.tileSize;
        gp.obj[6].worldY = 7 * gp.tileSize;

        gp.obj[7] = new OBJ_Computer(gp);
        gp.obj[7].worldX = 42 * gp.tileSize;
        gp.obj[7].worldY = 37 * gp.tileSize;

        gp.obj[8] = new OBJ_SystemUnit(gp);
        gp.obj[8].worldX = 15 * gp.tileSize;
        gp.obj[8].worldY = 17 * gp.tileSize;

        gp.obj[9] = new OBJ_Mouse(gp);
        gp.obj[9].worldX = 10 * gp.tileSize;
        gp.obj[9].worldY = 33 * gp.tileSize;

        gp.obj[10] = new OBJ_Speaker(gp);
        gp.obj[10].worldX = 12 * gp.tileSize;
        gp.obj[10].worldY = 45 * gp.tileSize;

        gp.obj[11] = new OBJ_Printer(gp);
        gp.obj[11].worldX = 20 * gp.tileSize;
        gp.obj[11].worldY = 38 * gp.tileSize;

        gp.obj[12] = new OBJ_Chrome(gp);
        gp.obj[12].worldX = 41 * gp.tileSize;
        gp.obj[12].worldY = 25 * gp.tileSize;

        gp.obj[13] = new OBJ_Windows(gp);
        gp.obj[13].worldX = 40 * gp.tileSize;
        gp.obj[13].worldY = 35 * gp.tileSize;

        gp.obj[14] = new OBJ_Facebook(gp);
        gp.obj[14].worldX = 32 * gp.tileSize;
        gp.obj[14].worldY = 7 * gp.tileSize;

        gp.obj[15] = new OBJ_Picsart(gp);
        gp.obj[15].worldX = 42 * gp.tileSize;
        gp.obj[15].worldY = 11 * gp.tileSize;
    }
}