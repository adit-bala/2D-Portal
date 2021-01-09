import java.awt.*;

/*
Abstract class Block that any type of entity extends, other than Player and the movable cube companion
 */
public abstract class Block {

    public static final int SIZE = 60;
    // Coordinate of block in 2D Array
    private Point worldCoords;
    // Coordinate of block in terms of Pixels
    private Point pixelCoords;
    private Color color;

    Block(int worldX, int worldY) {
        this.worldCoords = new Point(worldX, worldY);
        this.pixelCoords = new Point(worldX * SIZE, worldY * SIZE);

    }

    public abstract Color getColor();

    public abstract boolean isSolid();

    public abstract boolean isPortal();

    public Point getWorldCoords() {
        return worldCoords;
    }

    public Point getPixelCoords() {
        return pixelCoords;
    }
}