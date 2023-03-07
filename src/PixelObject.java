import java.awt.Color;

public class PixelObject  implements Comparable<PixelObject>{
    private Color color;
    private int x, y, w, h;

    public PixelObject(Color color,int x, int y, int w, int h) {
	super();
	this.color=color;
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
    
	public int compareTo(PixelObject o) {
	    int a1=this.w*this.h;
	    int a2=o.getW()*o.getH();
	    return a2-a1;
	}
}

