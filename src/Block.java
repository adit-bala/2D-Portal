import java.awt.*;

public abstract class Block {

	public static final int SIZE = 80;
	private Point worldCoords;
	private Point pixelCoords;

	Block(int worldX, int worldY) {
		this.worldCoords = new Point(worldX, worldY);
		this.pixelCoords = new Point(worldX * SIZE, worldY * SIZE);
	}

	public abstract boolean isPortalBlock();

	public abstract Color getColor();

	public abstract boolean isSolid();

	public Point getWorldCoords() {
		return worldCoords;
	}

	public Point getPixelCoords() {
		return pixelCoords;
	}

	public abstract PortalWall[] getWalls();

	public abstract boolean isSpikeBlock();

	public abstract boolean isLevelBlock();
}