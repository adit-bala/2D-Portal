import java.awt.Color;

public class PortalBlock extends Block{


    PortalBlock(int worldX, int worldY) {
        super(worldX, worldY);
    }

    @Override
    public Color getColor() {
        return Color.gray;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
