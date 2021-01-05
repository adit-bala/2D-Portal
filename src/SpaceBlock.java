import java.awt.Color;
/*
"Air Block", Not Solid
*/
public class SpaceBlock extends Block{

    SpaceBlock(int worldX, int worldY) {
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

    @Override
    public boolean isPortal() {
        return true;
    }
}
