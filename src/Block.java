import java.awt.*;

public abstract class Block {

    public static final int SIZE = 40;
    private Point worldCoords;
    private Point pixelCoords;
    private Color color;

    Block(int worldX, int worldY) {
        this.worldCoords = new Point(worldX, worldY);
        this.pixelCoords = new Point(worldX * SIZE, worldY * SIZE);

    }



    public abstract Color getColor();

    public abstract boolean isSolid();

    public Point getWorldCoords() {
        return worldCoords;
    }

    public Point getPixelCoords() {
        return pixelCoords;
    }
}