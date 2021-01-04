import java.awt.Color;
/*
Not the actual Player, just a block that allows the Player to be created, Not Solid
*/
public class PlayerBlock extends Block{

    PlayerBlock(int worldX, int worldY) {
        super(worldX, worldY);
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
