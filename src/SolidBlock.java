import java.awt.Color;

public class SolidBlock extends Block{
    @Override
    public Color getColor() {
        return Color.black;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
