import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ImageProcessor {
    Shape line;
    Shape arc;
    Shape square;
    Shape oval;
    Shape rectangle;
    Shape roundRectangle;

    BufferedImage referenceToOriginalImage;
    private int imageWidth;
    private int imageHeight;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;

    private int averagingRadius = 0;
    private int averagingPrecision = 0;

    private List<PixelObject> pixelObjects;
    private BufferedImage imageCopy;
    private Graphics2D g2;
    private int type = 1;
    private int squareHeight = 10;
    private int squareWidth = 10;
    private int verticalAmount = 100;

    private int drawWhat = 1;
    private int howManyItemsCanBeDrawn = 5;

    private long colorDeviation = 100;// 0-764
    private int squaresPerWidth = 100;
    private int shapeSizeRandomness = 100;
    private boolean randomRotate = false;
    private boolean symetricShape = false;
    private int shapeLocationRandomness;

    public boolean isSymetricShape() {
	return symetricShape;
    }

    public void setSymetricShape(boolean symetricShape) {
	this.symetricShape = symetricShape;
    }

    public boolean isRandomRotate() {
	return randomRotate;
    }

    public void setRandomRotate(boolean randomRotate) {
	this.randomRotate = randomRotate;
    }

    public int setShape(int shape) {
	drawWhat = shape;
	return howManyItemsCanBeDrawn;
    }

    public ImageProcessor() {
    }

    public BufferedImage getTransformedImage(BufferedImage img) {
	this.referenceToOriginalImage = img;
	squareWidth = img.getWidth() / squaresPerWidth;// how many pixels
	verticalAmount = img.getHeight() / squareWidth;
	averagingRadius = (squareWidth / 2) * averagingRadius;
	averagingPrecision = 3;
	setImageInfo();

	pixelObjects = new ArrayList<PixelObject>();

	squareHeight = squareWidth;

	type = BufferedImage.TYPE_INT_RGB;
	if (hasAlphaChannel) {
	    type = BufferedImage.TYPE_INT_ARGB;
	}
	imageCopy = new BufferedImage(img.getWidth(), img.getHeight(), type);

	g2 = imageCopy.createGraphics();

	return transformImage();
    }

    public BufferedImage transformImage() {
	int i = squareWidth;
	while (i <= referenceToOriginalImage.getWidth()) {
	    int x = i, y = 0;
	    for (int j = 0; j < verticalAmount; j++) {
		y = j * squareHeight;
		Color color;
		if (averagingRadius == 0) {
		    color = new Color(getPixel(x, y));
		} else {
		    color = getAverageColor(x, y);
		}

		if (shapeSizeRandomness > 0) {
		    int ran = 1;
		    if (Math.random() > 0.5) {
			ran = -1;
		    } else {
			ran = 1;
		    }

		    int width = new Random().nextInt(squareWidth * shapeSizeRandomness) + squareWidth;
		    int height = new Random().nextInt(squareHeight * shapeSizeRandomness) + squareHeight;
		    int rx = new Random().nextInt(squareHeight * shapeLocationRandomness) + squareWidth;
		    int ry = new Random().nextInt(squareHeight * shapeLocationRandomness) + squareHeight;
		    rx = x + (rx * ran);
		    ry = y + (ry * ran);
		    if (symetricShape) {
			height = width;
		    }
		    pixelObjects.add(new PixelObject(color, rx, ry, width, height));
		} else {
		    pixelObjects.add(new PixelObject(color, x, y, squareWidth, squareHeight));
		}

	    }
	    i += squareWidth;
	}

	if (shapeSizeRandomness > 0) {
	    Collections.shuffle(pixelObjects, new Random(System.nanoTime()));
	    Collections.sort(pixelObjects);
	}
	// if(drawWhat==4){
	//// g2.setStroke(new BasicStroke(pixelObjects.get(0).getH()));
	// g2.setStroke(new BasicStroke(pixelObjects.get(0).getW()));
	// }

	Color previous = Color.BLACK;

	for (int j = 0; j < pixelObjects.size(); j++) {
	    PixelObject p = pixelObjects.get(j);
	    Color color = p.getColor();
	    Color verticalNeighbour = Color.BLACK;

	    // if(j<pixelObjects.size()-squareWidthAmount){
	    // PixelObject p2 = pixelObjects.get(j+squareWidthAmount);
	    // verticalNeighbour=p2.getColor();
	    // }

	    // if(getColourDistance(previous,color)>colorDeviation&&getColourDistance(previous,verticalNeighbour)>colorDeviation){
	    // color=previous;
	    // }else{
	    // previous=color;
	    // }
	    int x = p.getX(), y = p.getY(), w = p.getW(), h = p.getH();
	    Shape shape = getShape(x, y, w, h);
	    g2.setColor(color);

	    if (drawWhat == 4) {
		g2.draw(shape);
		g2.setStroke(new BasicStroke(p.getW()));
	    } else {
		// g2.create();
		// g2.rotate(Math.random()*(2*Math.PI));

		// double rangle=Math.random()*(2*Math.PI);
		//
		// Path2D.Double path = new Path2D.Double();
		// path.append(shape, false);
		// AffineTransform t = new AffineTransform();
		// t.rotate(rangle,x,y);
		// path.transform(t);
		if (randomRotate) {
		    double rangle = Math.random() * (2 * Math.PI);

		    Path2D.Double path = new Path2D.Double();
		    path.append(shape, false);
		    AffineTransform t = new AffineTransform();
		    t.rotate(rangle, x, y);
		    path.transform(t);
		    g2.fill(path);
		} else {
		    g2.fill(shape);
		}
		g2.fill(shape);
		// g2.fill(path);
		// g2.dispose();
	    }

	}

	return imageCopy;
    }

    // http://stackoverflow.com/questions/2103368/color-logic-algorithm
    public double getColourDistance(Color c1, Color c2) {
	double rmean = (c1.getRed() + c2.getRed()) / 2;
	int r = c1.getRed() - c2.getRed();
	int g = c1.getGreen() - c2.getGreen();
	int b = c1.getBlue() - c2.getBlue();
	double weightR = 2 + rmean / 256;
	double weightG = 4.0;
	double weightB = 2 + (255 - rmean) / 256;
	return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    private Shape getShape(int x, int y, int w, int h) {
	switch (drawWhat) {
	case 1:
	    return getElipseShape(x, y, w, h);
	case 2:
	    return getRectangleShape(x, y, w, h);
	case 3:
	    return getRoundRectangleShape(x, y, w, h);
	case 4:
	    return getLineShape(x, y, w, h);
	case 5:
	    return getTestShape(x, y, w, h);
	default:
	    return getRectangleShape(x, y, w, h);
	}
    }

    public void setImageInfo() {
	imageWidth = referenceToOriginalImage.getWidth();
	imageHeight = referenceToOriginalImage.getHeight();
	hasAlphaChannel = referenceToOriginalImage.getAlphaRaster() != null;
	pixelLength = 3;
	if (hasAlphaChannel) {
	    pixelLength = 4;
	}
	pixels = ((DataBufferByte) referenceToOriginalImage.getRaster().getDataBuffer()).getData();
    }

    public int getPixel(int x, int y) {
	int pos = (y * pixelLength * imageWidth) + (x * pixelLength);

	int argb = -16777216; // 255 alpha
	if (hasAlphaChannel) {
	    argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
	}

	argb += ((int) pixels[pos++] & 0xff); // blue
	argb += (((int) pixels[pos++] & 0xff) << 8); // green
	argb += (((int) pixels[pos++] & 0xff) << 16); // red
	return argb;
    }

    public Color getAverageColor(int x, int y) {
	int r = 0, g = 0, b = 0;
	for (int i = 0; i < averagingPrecision; i++) {
	    int randomX = new Random().nextInt(averagingRadius * averagingPrecision) + averagingRadius;
	    int randomY = new Random().nextInt(averagingRadius * averagingPrecision) + averagingRadius;
	    int multi = 1;
	    if (i % 2 == 0) {
		multi *= -1;
	    }

	    int x2 = x + (randomX * multi);
	    int y2 = y + (randomY * multi);

	    Color c;
	    try {
		c = new Color(getPixel(x2, y2));
	    } catch (Exception e) {
		c = new Color(getPixel(x, y));
	    }
	    r += c.getRed();
	    g += c.getGreen();
	    b += c.getBlue();

	}

	r /= averagingPrecision;
	g /= averagingPrecision;
	b /= averagingPrecision;

	return new Color(r, g, b);
    }

    public Color getAverageColor(Color c1, Color c2) {
	int r = 0, g = 0, b = 0;
	r += c1.getRed();
	g += c1.getGreen();
	b += c1.getBlue();
	r += c2.getRed();
	g += c2.getGreen();
	b += c2.getBlue();

	r /= 2;
	g /= 2;
	b /= 2;

	return new Color(r, g, b);
    }

    private Shape getRoundRectangleShape(int x, int y, int w, int h) {
	return new RoundRectangle2D.Float(x, y, w, h, w / 2, w / 2);
    }

    private Shape getRectangleShape(int x, int y, int w, int h) {
	return new Rectangle2D.Float(x, y, w, h);
    }

    private Shape getElipseShape(int x, int y, int w, int h) {
	return new Ellipse2D.Float(x, y, w, h);
    }

    private Shape getLineShape(int x, int y, int w, int h) {
	return new Line2D.Float(x, y, x + w, y + h);
    }

    private Shape getTestShape(int x, int y, int w, int h) {
	return new TestShape(x, y, w, h).getShape();
    }

    public void setShapeSizeRandomness(int shapeRandomness) {
	this.shapeSizeRandomness = shapeRandomness;
    }

    public void setSquaresPerWidth(int squaresPerWidth) {
	this.squaresPerWidth = squaresPerWidth;
    }

    public void setAveragingRadius(int averagingRadius) {
	this.averagingRadius = averagingRadius;
    }

    public void setShapeLocationRandomness(int shapeLocationRandomness) {
	this.shapeLocationRandomness = shapeLocationRandomness;
    }

    // public Shape createShape(int pointCount, double max_deviation, int
    // median_radius, int[] center) {
    // int[][] pathPoints = new int[pointCount][2];
    // Random rand = new Random();
    // for (int i = 0; i < pointCount; i++) {
    // double angle = (2 * Math.PI / ((double) pointCount)) * ((double) i);
    // double temp_deviation = (0.5 - rand.nextDouble()) * 2.0 * max_deviation;
    // double temp_radius = median_radius + temp_deviation;
    // int x = (int) Math.round(temp_radius * Math.cos(angle));
    // int y = (int) Math.round(temp_radius * Math.sin(angle));
    // pathPoints[i] = new int[] { x + center[0], y + center[1] };
    // }
    // // somehow make pathPoints into a Shape with smooth curves
    // return new Path2d.Float(1);
    // }
}
