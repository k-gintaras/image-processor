import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TestShape {
    private int x,y,w,h;

    public TestShape(int x, int y, int w, int h) {
	this.x = x;
	this.y = y;
	this.w = w;
	this.h= h;
    }

    public Shape getShape() {
	Ellipse2D shapeOne = new Ellipse2D.Double(x, y, w, h);
	Area areaOne = new Area(shapeOne);

	for (int i = 0; i < 360; i += 120) {
	    AffineTransform at = AffineTransform.getTranslateInstance(i, i);
//	    AffineTransform at2=AffineTransform.getScaleInstance(10*i, 10*i);
	    at.rotate(Math.toRadians(i));
//	    at2.scale(10*i, 10*i);
	    
	    Shape shapeTwo= at.createTransformedShape(areaOne);
//	    Shape shapeThree= at2.createTransformedShape(shapeTwo);
	    
	    Area areaTwo = new Area(shapeTwo);
	    areaOne.add(areaTwo);
	}
	return areaOne;
    }

}
