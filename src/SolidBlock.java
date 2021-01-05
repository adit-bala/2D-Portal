import java.awt.Color;
/*
Solid Block used as either a barrier or a immovable block, Solid
*/
public class SolidBlock extends Block{

    SolidBlock(int worldX, int worldY) {
        super(worldX, worldY);
    }

    @Override
    public Color getColor() {
        return Color.black;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isPortal() {
        return false;
    }
}
