import java.awt.Color;

public class SolidBlock extends Block {

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
	public boolean isPortalBlock() {
		return false;
	}

	@Override
	public PortalWall[] getWalls() {
		return null;
	}

	@Override
	public boolean isSpikeBlock() {
		return false;
	}

	@Override
	public boolean isLevelBlock() {
		return false;
	}
}