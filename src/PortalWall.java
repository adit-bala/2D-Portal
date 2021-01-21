import java.awt.Color;

public class PortalWall {
	Point a;
	Point b;
	boolean highlighted = false;
	boolean hasPortal = false;
	boolean hasOrangePortal = false;
	boolean hasBluePortal = false;
	boolean left = false;
	boolean right = false;
	boolean top = false;
	boolean bottom = false;
	PortalBlock block;

	public PortalWall(int x1, int y1, int x2, int y2, String orientation, PortalBlock block) {
		a = new Point(x1, y1);
		b = new Point(x2, y2);
		if (orientation.equals("left")) {
			left = true;
		} else if (orientation.equals("right")) {
			right = true;
		} else if (orientation.equals("top")) {
			top = true;
		} else if (orientation.equals("bottom")) {
			bottom = true;
		}
		this.block = block;
	}

	public void setHighlight(boolean isLit) {
		highlighted = isLit;
	}

	private static boolean onSegment(Point p, Point q, Point r) {
		return (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y)
				&& q.y >= Math.min(p.y, r.y));
	}

	public PortalBlock getBlock() {
		return block;
	}

	private static int orientation(Point p, Point q, Point r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
		if (val == 0) {
			return 0;
		}

		return (val > 0) ? 1 : 2;
	}

	public boolean intersects(Point p2, Point q2) {
		int o1 = orientation(a, b, p2);
		int o2 = orientation(a, b, q2);
		int o3 = orientation(p2, q2, a);
		int o4 = orientation(p2, q2, b);
		if (o1 != o2 && o3 != o4) {
			return true;
		}
		if (o1 == 0 && onSegment(a, p2, b)) {
			return true;
		}
		if (o2 == 0 && onSegment(a, q2, b)) {
			return true;
		}
		if (o3 == 0 && onSegment(p2, a, q2)) {
			return true;
		}
		if (o4 == 0 && onSegment(p2, b, q2)) {
			return true;
		}
		return false;
	}

	public void placeBluePortal() {
		hasPortal = true;
		hasBluePortal = true;
		hasOrangePortal = false;
	}

	public void placeOrangePortal() {
		hasPortal = true;
		hasBluePortal = false;
		hasOrangePortal = true;
	}

	public void removeOrangePortal() {
		hasOrangePortal = false;
		if (!hasBluePortal) {
			hasPortal = false;
		}
	}

	public void removeBluePortal() {
		hasBluePortal = false;
		if (!hasOrangePortal) {
			hasPortal = false;
		}
	}

	public Color getColor() {
		if (hasBluePortal) {
			return Color.blue;
		} else if (hasOrangePortal) {
			return Color.orange;
		} else if (highlighted) {
			return Color.red;
		} else if (hasPortal) {
			return Color.green;
		}
		return Color.gray;
	}

	public double distance(Point b) {
		double x2 = b.x;
		double y2 = b.y;
		return (Math.sqrt(Math.pow((x2 - (int) ((a.x + b.x) / 2)), 2) + Math.pow(y2 - (int) ((a.y + b.y) / 2), 2)));
	}

	public Point getA() {
		return a;
	}

	public Point getB() {
		return b;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public String toString() {
		String str = "";
		str += "a: " + a.toString() + "\n";
		str += "b: " + b.toString() + "\n";
		str += "color: " + getColor() + "\n";
		return str;
	}
}
