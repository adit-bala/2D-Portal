import java.awt.Color;

public class SpaceBlock extends Block {

	SpaceBlock(int worldX, int worldY) {
		super(worldX, worldY);
	}

	@Override
	public Color getColor() {
		return Color.lightGray;
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public PortalWall[] getWalls() {
		return null;
	}

	@Override
	public boolean isPortalBlock() {
		return false;
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