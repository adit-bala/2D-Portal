import java.awt.Color;

public class PlayerBlock extends Block{
    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
