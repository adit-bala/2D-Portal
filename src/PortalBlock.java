import java.awt.Color;

public class PortalBlock extends Block {
	private PortalWall[] bounds = new PortalWall[4];

	PortalBlock(int worldX, int worldY) {
		super(worldX, worldY);
		bounds[0] = new PortalWall(Block.SIZE * worldX, Block.SIZE * worldY, Block.SIZE * (worldX + 1),
				Block.SIZE * worldY, "top", this);
		bounds[1] = new PortalWall(Block.SIZE * (worldX + 1), Block.SIZE * worldY, Block.SIZE * (worldX + 1),
				Block.SIZE * (worldY + 1) - 1, "right", this);
		bounds[2] = new PortalWall(Block.SIZE * (worldX + 1), Block.SIZE * (worldY + 1) - 1, Block.SIZE * worldX,
				Block.SIZE * (worldY + 1) - 1, "bottom", this);
		bounds[3] = new PortalWall(Block.SIZE * worldX, Block.SIZE * (worldY + 1) - 1, Block.SIZE * worldX,
				Block.SIZE * worldY, "left", this);
	}

	@Override
	public Color getColor() {
		return Color.gray;
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public PortalWall[] getWalls() {
		return bounds;
	}

	@Override
	public boolean isPortalBlock() {
		return true;
	}

	@Override
	public boolean isLevelBlock() {
		return false;
	}

	@Override
	public boolean isSpikeBlock() {
		return false;
	}
}