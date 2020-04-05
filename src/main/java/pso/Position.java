package pso;
import java.util.Random;

public class Position {
	private double limit = Double.MAX_VALUE;
	int x;
	int y;

	public Position() {
		x=rand(0, 5);;y=rand(0, 5);
	}
	
	private static int rand(int beginRange, int endRange) {
		Random r = new java.util.Random();
		return r.nextInt(endRange - beginRange) + beginRange;
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	
	public Position clone() {
		Position p = new Position(x, y);
		return p;
	}
	
	 void add (Position v) {
	        x += v.x;
	        y += v.y;
	        limit();
	    }

	    void sub (Position v) {
	        x -= v.x;
	        y -= v.y;
	        limit();
	    }

	    void mul (double s) {
	        x *= s;
	        y *= s;
	        limit();
	    }

	    void div (double s) {
	        x /= s;
	        y /= s;
	        limit();
	    }

	    void normalize () {
	        double m = mag();
	        if (m > 0) {
	            x /= m;
	            y /= m;
	        }
	    }

	    private double mag () {
	        return Math.sqrt(x*x + y*y);
	    }

	    void limit (double l) {
	        limit = l;
	        limit();
	    }

	    private void limit () {
	        double m = mag();
	        if (m > limit) {
	            double ratio = m / limit;
	            x /= ratio;
	            y /= ratio;
	        }
	    }
	
}
