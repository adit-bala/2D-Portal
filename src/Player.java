import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player {
	public final int BLOCK_SIZE = 80;
	public final int SIZE = BLOCK_SIZE / 2;
	private double xPos, yPos;
	private double xVel = 0, yVel = 0;
	private double xAccel = 0;
	private final double SPEED = 1;
	private final int MARGIN = 1;
	private final double GRAVITY = .5;
	private double mouseX = 0, mouseY = 0;
	private Block[][] map;
	private ArrayList<PortalWall> walls;
	private PortalWall currentWall = null;
	private boolean inPortalOnRight = false, inPortalOnLeft = false, inPortalAbove = false, inPortalBelow = false,
			onPortalGround = false, teleported = false;
	private PortalWall currentPortal = null;
	private int startX;
	private int startY;
	private Level level;

	public Player(int xPos, int yPos, Level level) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.startX = xPos;
		this.startY = yPos;
		this.level = level;
	}

	public void setMap(Block[][] map, ArrayList<PortalWall> walls) {
		this.map = map;
		this.walls = walls;
	}

	private PortalWall getPortalOnRight() {
		Point tL = new Point(xPos + MARGIN, yPos + MARGIN);
		Point tR = new Point(xPos + MARGIN + SIZE, yPos + MARGIN);
		Point bL = new Point(xPos + MARGIN, yPos + SIZE - MARGIN);
		Point bR = new Point(xPos + MARGIN + SIZE, yPos + SIZE - MARGIN);
		for (PortalWall wall : walls) {
			if (wall.hasPortal) {
				if (wall.intersects(tR, tL) && wall.intersects(bR, bL)) {
					return wall;
				}
			}
		}
		return null;
	}

	private PortalWall getPortalOnLeft() {
		Point tL = new Point(xPos - MARGIN, yPos + MARGIN);
		Point tR = new Point(xPos - MARGIN + SIZE, yPos + MARGIN);
		Point bL = new Point(xPos - MARGIN, yPos + SIZE - MARGIN);
		Point bR = new Point(xPos - MARGIN + SIZE, yPos + SIZE - MARGIN);
		for (PortalWall wall : walls) {
			if (wall.hasPortal) {
				if (wall.intersects(tL, tR) && wall.intersects(bL, bR)) {
					return wall;
				}
			}
		}
		return null;
	}

	private PortalWall getPortalAbove() {
		Point tL = new Point(xPos + MARGIN, yPos - MARGIN);
		Point tR = new Point(xPos - MARGIN + SIZE, yPos - MARGIN);
		Point bL = new Point(xPos + MARGIN, yPos + SIZE - MARGIN);
		Point bR = new Point(xPos - MARGIN + SIZE, yPos + SIZE - MARGIN);
		for (PortalWall wall : walls) {
			if (wall.hasPortal) {
				if (wall.intersects(tL, bL) && wall.intersects(tR, bR)) {
					return wall;
				}
			}
		}
		return null;
	}

	private PortalWall getPortalBelow() {
		Point tL = new Point(xPos + MARGIN, yPos + MARGIN);
		Point tR = new Point(xPos - MARGIN + SIZE, yPos + MARGIN);
		Point bL = new Point(xPos + MARGIN, yPos + SIZE + MARGIN);
		Point bR = new Point(xPos - MARGIN + SIZE, yPos + SIZE + MARGIN);
		for (PortalWall wall : walls) {
			if (wall.hasPortal) {
				if (wall.intersects(tL, bL) && wall.intersects(tR, bR)) {
					return wall;
				}
			}
		}
		return null;
	}

	public void move() {
		teleported = false;
		if (xVel < 6 && xVel > -6) {
			xVel += xAccel;
		}
		xPos += xVel;
		if (xVel > 0) {
			// moving right
			if (inPortalAbove || inPortalBelow) {
				double blockX = currentPortal.getBlock().getPixelCoords().x + BLOCK_SIZE;
				if (xPos + SIZE > blockX) {
					xVel = 0;
					xPos = blockX - SIZE;
				}
			} else {
				if (inPortalOnLeft) {
					if (getPortalOnLeft() == null) {
						inPortalOnLeft = false;
					}
				} else {
					PortalWall portalOnRight = getPortalOnRight();
					if (portalOnRight != null) {
						if (twoPortalsOut()) {
							currentPortal = portalOnRight;
							inPortalOnRight = true;
						}
					} else {
						if (inPortalOnRight) {
							teleportFrom(currentPortal);
							PortalWall otherPortal = getOtherPortal(currentPortal);
							if (otherPortal.left) {
								currentPortal = otherPortal;
							} else if (otherPortal.right) {
								inPortalOnRight = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnLeft = true;
							} else if (otherPortal.top) {
								inPortalOnRight = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalBelow = true;
							} else {
								inPortalOnRight = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalAbove = true;
							}
						}
					}
				}
				if (!teleported) {
					Block topRight = getBlockAt(new Point(xPos + SIZE, yPos));
					Block bottomRight = getBlockAt(new Point(xPos + SIZE, yPos + SIZE - MARGIN));
					if (spikeBlock(topRight, bottomRight)) {
						xPos = startX;
						yPos = startY;
					} else if (levelBlock(topRight, bottomRight)) {
						xPos = startX;
						yPos = startY;
						level.nextLevel();
					} else {
						if (!canMoveThrough(topRight) && !inPortalOnRight) {
							xPos = topRight.getPixelCoords().x - SIZE;
							xVel = 0;
						}
						if (!canMoveThrough(bottomRight) && !inPortalOnRight) {
							xPos = bottomRight.getPixelCoords().x - SIZE;
							xVel = 0;
						}
					}
				}
			}
		}
		if (xVel < 0) {
			// moving left
			if (inPortalAbove || inPortalBelow) {
				double blockX = currentPortal.getBlock().getPixelCoords().x + MARGIN + 1;
				if (xPos < blockX) {
					xVel = 0;
					xPos = blockX;
				}
			} else {
				if (inPortalOnRight) {
					if (getPortalOnRight() == null) {
						inPortalOnRight = false;
					}
				} else {
					PortalWall portalOnLeft = getPortalOnLeft();
					if (portalOnLeft != null) {
						if (twoPortalsOut()) {
							currentPortal = portalOnLeft;
							inPortalOnLeft = true;
						}
					} else {
						if (inPortalOnLeft) {
							teleportFrom(currentPortal);
							PortalWall otherPortal = getOtherPortal(currentPortal);
							if (otherPortal.right) {
								currentPortal = otherPortal;
							} else if (otherPortal.left) {
								inPortalOnLeft = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnRight = true;
							} else if (otherPortal.top) {
								inPortalOnLeft = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalBelow = true;
							} else {
								inPortalOnLeft = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalAbove = true;
							}
						}
					}
				}
				if (!teleported) {
					Block topLeft = getBlockAt(new Point(xPos - MARGIN, yPos));
					Block bottomLeft = getBlockAt(new Point(xPos - MARGIN, yPos + SIZE - MARGIN));
					if (spikeBlock(topLeft, bottomLeft)) {
						xPos = startX;
						yPos = startY;
					} else if (levelBlock(topLeft, bottomLeft)) {
						xPos = startX;
						yPos = startY;
						System.out.println("NEXTLEVEL");
					} else {
						if (!canMoveThrough(topLeft) && !inPortalOnLeft) {
							xPos = topLeft.getPixelCoords().x + BLOCK_SIZE;
							xVel = 0;
						}
						if (!canMoveThrough(bottomLeft) && !inPortalOnLeft) {
							xPos = bottomLeft.getPixelCoords().x + BLOCK_SIZE;
							xVel = 0;
						}
					}
				}
			}
		}
		yVel += GRAVITY;
		yPos += yVel;
		if (yVel < 0) {
			// moving up
			if (inPortalOnRight || inPortalOnLeft) {
				// System.out.println("rl");
				double blockY = currentPortal.getBlock().getPixelCoords().y;
				if (yPos < blockY) {
					yVel = 0;
					yPos = blockY;
				}
			} else {
				if (inPortalBelow) {
					if (getPortalBelow() == null) {
						inPortalBelow = false;
					}
				} else {
					PortalWall portalAbove = getPortalAbove();
					if (portalAbove != null) {
						if (twoPortalsOut()) {
							currentPortal = portalAbove;
							inPortalAbove = true;
						}
					} else {
						if (inPortalAbove) {
							teleportFrom(currentPortal);
							PortalWall otherPortal = getOtherPortal(currentPortal);
							if (otherPortal.bottom) {
								currentPortal = otherPortal;
							} else if (otherPortal.top) {
								inPortalAbove = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalBelow = true;
							} else if (otherPortal.right) {
								inPortalAbove = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnLeft = true;
							} else {
								inPortalAbove = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnRight = true;
							}
						}
					}
				}
				if (!teleported) {
					Block topLeft = getBlockAt(new Point(xPos, yPos - MARGIN));
					Block topRight = getBlockAt(new Point(xPos + SIZE - MARGIN, yPos - MARGIN));
					if (spikeBlock(topLeft, topRight)) {
						xPos = startX;
						yPos = startY;
					} else if (levelBlock(topLeft, topRight)) {
						xPos = startX;
						yPos = startY;
						System.out.println("NEXTLEVEL");
					} else {
						if (!canMoveThrough(topLeft) && !inPortalAbove) {
							yVel = 0;
							yPos = topLeft.getPixelCoords().y + BLOCK_SIZE;
						}
						if (!canMoveThrough(topRight) && !inPortalAbove) {
							yVel = 0;
							yPos = topRight.getPixelCoords().y + BLOCK_SIZE;
						}
					}
				}
			}
		}
		if (yVel > 0) {
			// moving down
			if (inPortalOnRight || inPortalOnLeft) {
				// System.out.println("rl");
				double blockY = currentPortal.getBlock().getPixelCoords().y + BLOCK_SIZE - SIZE;
				if (yPos > blockY) {
					yVel = 0;
					onPortalGround = true;
					yPos = blockY;
				}
			} else {
				if (inPortalAbove) {
					if (getPortalAbove() == null) {
						inPortalAbove = false;
					}
				} else {
					PortalWall portalBelow = getPortalBelow();
					if (portalBelow != null) {
						if (twoPortalsOut()) {
							currentPortal = portalBelow;
							inPortalBelow = true;
						}
					} else {
						if (inPortalBelow) {
							teleportFrom(currentPortal);
							PortalWall otherPortal = getOtherPortal(currentPortal);
							if (otherPortal.top) {
								currentPortal = otherPortal;
							} else if (otherPortal.bottom) {
								inPortalBelow = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalAbove = true;
							} else if (otherPortal.left) {
								inPortalBelow = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnRight = true;
							} else {
								inPortalBelow = false;
								currentPortal = getOtherPortal(currentPortal);
								inPortalOnLeft = true;
							}
						}
					}
				}
				if (!teleported) {
					Block bottomLeft = getBlockAt(new Point(xPos, yPos + SIZE));
					Block bottomRight = getBlockAt(new Point(xPos + SIZE - MARGIN, yPos + SIZE));
					if (spikeBlock(bottomLeft, bottomRight)) {
						xPos = startX;
						yPos = startY;
					} else if (levelBlock(bottomLeft, bottomRight)) {
						xPos = startX;
						yPos = startY;
						System.out.println("NEXTLEVEL");
					} else {
						if (!canMoveThrough(bottomLeft) && !inPortalBelow) {
							yVel = 0;
							yPos = bottomLeft.getPixelCoords().y - SIZE;
						}
						if (!canMoveThrough(bottomRight) && !inPortalBelow) {
							yVel = 0;
							yPos = bottomRight.getPixelCoords().y - SIZE;
						}
					}
				}
			}
		}
		constrainSpeed();
		highlightTargetPortalLocation();
	}

	private boolean spikeBlock(Block block1, Block block2) {
		if (block1 != null && block2 != null) {
			return (block1.isSpikeBlock() || block2.isSpikeBlock());
		}
		return false;
	}

	private boolean levelBlock(Block block1, Block block2) {
		if (block1 != null && block2 != null) {
			return (block1.isLevelBlock() && block2.isLevelBlock());
		}
		return false;
	}

	private void highlightTargetPortalLocation() {
		double x = xPos + 10;
		double y = yPos + 10;
		double step = 1000;
		double yIncr = (mouseY - y) / step;
		double xIncr = (mouseX - x) / step;
		boolean searching = true;
		Block portalBlock = null;
		Block currentBlock = null;
		while (searching) {
			x += xIncr;
			y += yIncr;
			if (x < 0 || x >= (30 * BLOCK_SIZE) || y < 0 || y >= (12 * BLOCK_SIZE)) {
				searching = false;
			} else {
				currentBlock = getBlockAt(new Point(x, y));
				if (currentBlock!= null && currentBlock.isPortalBlock()) {
					searching = false;
					portalBlock = currentBlock;
				} else if (currentBlock!= null && currentBlock.isSolid()) {
					searching = false;
				}
			}
		}
		unhighlight();
		if (portalBlock != null) {
			double min = 1000;
			PortalWall closest = null;
			for (PortalWall wall : portalBlock.getWalls()) {
				if (wall.intersects(new Point(xPos + (SIZE / 2), yPos + (SIZE / 2)), new Point(x, y))) {
					double dist = wall.distance(new Point(xPos + (SIZE / 2), yPos + (SIZE / 2)));
					if (dist < min) {
						min = dist;
						closest = wall;
					}
				}
			}
			if (closest != null) {
				closest.setHighlight(true);
				currentWall = closest;
			}
		}
	}

	private void rotate(double angle) {
		double oldxVel = xVel;
		double oldyVel = yVel * -1;
		xVel = (oldxVel * Math.cos(angle)) + (oldyVel * Math.sin(angle));
		yVel = (-1 * oldxVel * Math.sin(angle)) + (oldyVel * Math.cos(angle));
		yVel *= -1;
	}

	private void teleportFrom(PortalWall firstPortal) {
		teleported = true;
		if (twoPortalsOut()) {
			PortalWall secondPortal = getOtherPortal(firstPortal);
			if (secondPortal != null) {
				if (secondPortal.left) {
					if (firstPortal.left) {
						rotate(Math.PI);
						xPos = secondPortal.a.x - MARGIN;
						yPos = secondPortal.a.y - (yPos % BLOCK_SIZE) - SIZE;
					}
					if (firstPortal.right) {
						rotate(0);
						xPos = secondPortal.a.x - MARGIN;
						yPos = secondPortal.b.y + (yPos % BLOCK_SIZE);
					}
					if (firstPortal.bottom) {
						rotate(Math.PI * 1.5);
						yPos = secondPortal.a.y - (xPos % BLOCK_SIZE + SIZE);
						xPos = secondPortal.a.x - MARGIN;
					}
					if (firstPortal.top) {
						rotate(Math.PI * .5);
						yPos = secondPortal.b.y + (xPos % BLOCK_SIZE);
						xPos = secondPortal.a.x - MARGIN;
					}
				}
				if (secondPortal.right) {
					if (firstPortal.left) {
						rotate(0);
						xPos = secondPortal.a.x - SIZE + MARGIN;
						yPos = secondPortal.a.y + (yPos % BLOCK_SIZE);
					}
					if (firstPortal.right) {
						rotate(Math.PI);
						xPos = secondPortal.a.x - SIZE + MARGIN;
						yPos = secondPortal.a.y - (yPos % BLOCK_SIZE) + SIZE;
					}
					if (firstPortal.bottom) {
						rotate(Math.PI * .5);
						yPos = secondPortal.a.y + (xPos % BLOCK_SIZE);
						xPos = secondPortal.a.x - SIZE + MARGIN;
					}
					if (firstPortal.top) {
						rotate(Math.PI * 1.5);
						yPos = secondPortal.b.y - (xPos % BLOCK_SIZE + SIZE);
						xPos = secondPortal.a.x - SIZE + MARGIN;
					}
				}
				if (secondPortal.bottom) {
					if (firstPortal.left) {
						rotate(Math.PI * .5);
						xPos = secondPortal.a.x - (yPos % BLOCK_SIZE) - SIZE;
						yPos = secondPortal.a.y + MARGIN - SIZE;
					}
					if (firstPortal.right) {
						rotate(Math.PI * 1.5);
						xPos = secondPortal.b.x + (yPos % BLOCK_SIZE);
						yPos = secondPortal.a.y + MARGIN - SIZE;

					}
					if (firstPortal.bottom) {
						rotate(Math.PI);
						xPos = secondPortal.a.x - (xPos % BLOCK_SIZE) - SIZE;
						yPos = secondPortal.a.y + MARGIN - SIZE;
					}
					if (firstPortal.top) {
						xPos = secondPortal.b.x + (xPos % BLOCK_SIZE);
						yPos = secondPortal.a.y + MARGIN - SIZE;
					}
				}
				if (secondPortal.top) {
					if (firstPortal.left) {
						rotate(Math.PI * 1.5);
						xPos = secondPortal.a.x + (yPos % BLOCK_SIZE);
						yPos = secondPortal.a.y - MARGIN;
					}
					if (firstPortal.right) {
						rotate(Math.PI * .5);
						xPos = secondPortal.b.x - (yPos % BLOCK_SIZE) - SIZE;
						yPos = secondPortal.a.y - MARGIN;
					}
					if (firstPortal.bottom) {
						xPos = secondPortal.a.x + (xPos % BLOCK_SIZE);
						yPos = secondPortal.a.y - MARGIN;
					}
					if (firstPortal.top) {
						rotate(Math.PI);
						xPos = secondPortal.b.x - (xPos % BLOCK_SIZE) - SIZE;
						yPos = secondPortal.a.y - MARGIN;
					}
				}
			}
		}
	}

	private double dampen(double num, double factor) {
		boolean neg = num < 0;
		num = Math.abs(num);
		num *= factor;
		if (neg) {
			return num * -1;
		}
		return num;
	}

	private void constrainSpeed() {
		boolean yNeg = yVel < 0;
		if (Math.abs(yVel) > 40) {
			yVel = yNeg ? -40 : 40;
		}
		boolean xNeg = xVel < 0;
		if (Math.abs(xVel) > 40) {
			xVel = xNeg ? -40 : 40;
		}
		if (inAir()) {
			xVel = dampen(xVel, .99);
		} else {
			xVel = dampen(xVel, .80);
		}
	}

	private boolean canMoveThrough(Block block) {
		return (block != null && !block.isSolid());
	}

	private PortalWall getOtherPortal(PortalWall first) {
		for (PortalWall wall : walls) {
			if (wall.hasPortal && wall.hasBluePortal != first.hasBluePortal) {
				return wall;
			}
		}
		return null;
	}

	private boolean twoPortalsOut() {
		int count = 0;
		for (PortalWall wall : walls) {
			if (wall.hasPortal) {
				count++;
			}
		}
		return count == 2;
	}

	private boolean inAir() {
		if (inPortalOnRight || inPortalOnLeft) {
			return !onPortalGround;
		}
		Block bottomLeft = getBlockAt(new Point(xPos, yPos + SIZE));
		Block bottomRight = getBlockAt(new Point(xPos + SIZE - MARGIN, yPos + SIZE));
		if (canMoveThrough(bottomLeft) && canMoveThrough(bottomRight)) {
			return true;
		} else {
			return false;
		}
	}

	private Block getBlockAt(Point p) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Block currBlock = map[i][j];
				if (p.x >= currBlock.getPixelCoords().x && p.x < currBlock.getPixelCoords().x + Block.SIZE
						&& p.y >= currBlock.getPixelCoords().y && p.y < currBlock.getPixelCoords().y + Block.SIZE) {
					return currBlock;
				}
			}
		}
		return null;
	}

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_A) {
			xAccel = -SPEED;
		}
		if (key == KeyEvent.VK_D) {
			xAccel = SPEED;
		}
		if (key == KeyEvent.VK_W) {
			if (!inAir() && !inPortalOnLeft && !inPortalOnRight && !inPortalAbove && !inPortalBelow) {
				onPortalGround = false;
				yVel = -12;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_A) {
			xAccel = (xAccel > 0) ? SPEED : 0;
		}
		if (key == KeyEvent.VK_D) {
			xAccel = (xAccel < 0) ? -SPEED : 0;
		}
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (currentWall != null) {
				removeBluePortals();
				currentWall.placeBluePortal();
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (currentWall != null) {
				removeOrangePortals();
				currentWall.placeOrangePortal();
			}
		}
	}

	private void removeOrangePortals() {
		for (PortalWall wall : walls) {
			wall.removeOrangePortal();
		}
	}

	private void removeBluePortals() {
		for (PortalWall wall : walls) {
			wall.removeBluePortal();
		}
	}

	private void unhighlight() {
		for (PortalWall wall : walls) {
			wall.setHighlight(false);
		}
		currentWall = null;
	}
}