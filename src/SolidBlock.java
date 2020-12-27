import java.awt.Color;

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
}
