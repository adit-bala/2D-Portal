import java.awt.Color;
/*
Block meant to be allowed to shoot Portals at, Solid
*/
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

    @Override
    public boolean isPortal() {
        return true;
    }
}
