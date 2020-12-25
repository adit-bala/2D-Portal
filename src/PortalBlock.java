import java.awt.Color;

public class PortalBlock extends Block{
    @Override
    public Color getColor() {
        return Color.gray;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
