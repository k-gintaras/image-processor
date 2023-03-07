import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel implements ComponentListener {
    private static final long serialVersionUID = 2213436774702062370L;
    private BufferedImage img;

    public ImagePanel(BufferedImage img) {
	this.img = img;
    }

    public ImagePanel() {
    }

    public void setImage(BufferedImage img) {
	this.img = img;
	repaint();
    }

    public void paint(Graphics g) {
	super.paint(g);
	try {
	    Dimension scaledImageDimension = getScaledDimension(new Dimension(img.getWidth(), img.getHeight()), new Dimension(this.getWidth(), this.getHeight()));
	    int imageCenter = (int) scaledImageDimension.getWidth() / 2;
	    int viewCenter = this.getWidth() / 2;
	    g.drawImage(img, viewCenter - imageCenter, this.getY(), (int) scaledImageDimension.getWidth(), (int) scaledImageDimension.getHeight(), this);
	} catch (Exception e) {
	}
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {

    }

    @Override
    public void componentMoved(ComponentEvent arg0) {

    }

    @Override
    public void componentResized(ComponentEvent arg0) {
	repaint();
    }

    @Override
    public void componentShown(ComponentEvent arg0) {

    }

    // http://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	int original_width = imgSize.width;
	int original_height = imgSize.height;
	int bound_width = boundary.width;
	int bound_height = boundary.height;
	int new_width = original_width;
	int new_height = original_height;

	// first check if we need to scale width
	if (original_width > bound_width) {
	    // scale width to fit
	    new_width = bound_width;
	    // scale height to maintain aspect ratio
	    new_height = (new_width * original_height) / original_width;
	}

	// then check if we need to scale even with the new height
	if (new_height > bound_height) {
	    // scale height to fit instead
	    new_height = bound_height;
	    // scale width to maintain aspect ratio
	    new_width = (new_height * original_width) / original_height;
	}

	return new Dimension(new_width, new_height);
    }
}
