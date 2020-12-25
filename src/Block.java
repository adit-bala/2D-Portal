import java.awt.Color;

public abstract class Block {

    public static final int SIZE = 40;

    public abstract Color getColor();

    public abstract boolean isSolid();
}