import java.awt.Color;

public class SpikeBlock extends Block {

    SpikeBlock(int worldX, int worldY) {
        super(worldX, worldY);
    }

    @Override
    public Color getColor() {
        return Color.red;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isPortalBlock() {
        return false;
    }

    @Override
    public PortalWall[] getWalls() {
        return null;
    }

    @Override
    public boolean isSpikeBlock() {
        return true;
    }

    @Override
    public boolean isLevelBlock() {
        return false;
    }
}