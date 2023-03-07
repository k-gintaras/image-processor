import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSlider;
import net.miginfocom.swing.MigLayout;

public class ImageProcessorWindow extends JFrame implements ComponentListener {

    /**
     * 
     */
    private static final long serialVersionUID = 7160742615693352209L;
    private JPanel contentPane;
    private JTextField txtFeedback;
    private BufferedImage img;
    private ImagePanel imagePanel;
    private FilePicker filePicker;
    private ImageProcessor imageProcessor;
    private JTextField textField;
    private JCheckBox checkBox;

    boolean flip = true;
    private JSlider slider;
    private JSlider slider_1;
    private JCheckBox checkBox_1;
    private JSlider slider_2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    ImageProcessorWindow frame = new ImageProcessorWindow();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
    public ImageProcessorWindow() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 800, 600);
	setExtendedState(JFrame.MAXIMIZED_BOTH);
	// windowHelper.setUndecorated(true);
	// windowHelper.setVisible(true);

	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);

	Panel panel = new Panel();
	contentPane.add(panel, BorderLayout.NORTH);

	Panel panel_1 = new Panel();
	contentPane.add(panel_1, BorderLayout.SOUTH);

	filePicker = new FilePicker();
	imageProcessor = new ImageProcessor();
	imagePanel = new ImagePanel();
	contentPane.add(imagePanel, BorderLayout.CENTER);

	JMenuBar menuBar = new JMenuBar();
	JMenu popupMenu = new JMenu("Settings");
	popupMenu.setMnemonic(KeyEvent.VK_F);
	menuBar.add(popupMenu);

	JLabel lblNewLabel_1 = new JLabel("averagingRadius");
	textField = new JTextField();
	textField.setText("1");
	textField.setColumns(10);
	popupMenu.add(lblNewLabel_1);
	popupMenu.add(textField);

	setJMenuBar(menuBar);

	JButton btnLoadFile = new JButton("Load File");
	btnLoadFile.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		File file = filePicker.loadFile();
		displayImage(file);
		flip = true;
	    }
	});
	panel.setLayout(new MigLayout("", "[77px][61px][31px][73px][5px][65px][5px][121px][59px][71px][63px]", "[23px][24px]"));
	panel.add(btnLoadFile, "cell 0 0,alignx left,aligny top");

	checkBox = new JCheckBox("Random Rotate");
	panel.add(checkBox, "cell 1 0,alignx left,aligny top");

	checkBox_1 = new JCheckBox("Symetric Shape");
	panel.add(checkBox_1, "cell 2 0,alignx left,aligny top");

	txtFeedback = new JTextField();
	txtFeedback.setText("feedback");
	txtFeedback.setColumns(10);
	panel_1.add(txtFeedback);

	addComponentListener(this);

	if (filePicker.isDirectoryDataFileExists()) {
	    displayImage(filePicker.getPreviouslyLoadedFile());
	}

	JButton btnNewButton = new JButton("Squares");
	btnNewButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayShape(2);
	    }
	});
	panel.add(btnNewButton, "cell 3 0,alignx left,aligny top");

	JButton btnNewButton2 = new JButton("Circles");
	btnNewButton2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayShape(1);
	    }
	});
	panel.add(btnNewButton2, "cell 5 0,alignx left,aligny top");

	JButton btnNewButton4 = new JButton("Round Rectangles");
	btnNewButton4.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayShape(3);
	    }
	});
	panel.add(btnNewButton4, "cell 7 0,alignx left,aligny top");

	JButton btnNewButton5 = new JButton("Lines");
	btnNewButton5.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayShape(4);
	    }
	});
	panel.add(btnNewButton5, "cell 8 0,alignx left,aligny top");

	JButton btnNewButton6 = new JButton("Custom");
	btnNewButton6.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayShape(5);
	    }
	});
	panel.add(btnNewButton6, "cell 9 0,alignx left,aligny top");

	JButton btnNewButton3 = new JButton("Reset");
	btnNewButton3.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		displayImage(img);
	    }
	});
	panel.add(btnNewButton3, "cell 10 0,alignx left,aligny top");
	
		JLabel lblPixelation = new JLabel("Pixelation");
		panel.add(lblPixelation, "cell 0 1,alignx left");
	
		slider = new JSlider();
		slider.setMinimum(1);
		slider.setMaximum(500);
		slider.setValue(100);
		panel.add(slider, "cell 1 1,alignx left,aligny top");
	
	JLabel lblShapeRandomness = new JLabel("Shape Size Randomness");
	panel.add(lblShapeRandomness, "cell 2 1");
	
 slider_1 = new JSlider();
	slider_1.setMinimum(0);
	slider_1.setMaximum(1000);
	slider_1.setValue(0);
	panel.add(slider_1, "cell 3 1,alignx left,aligny top");
	
	JLabel lblShapeLocationRandomness = new JLabel("Shape Location Randomness");
	panel.add(lblShapeLocationRandomness, "cell 5 1");
	
 slider_2 = new JSlider();
	slider_2.setMinimum(1);
	slider_2.setMaximum(1000);
	slider_2.setValue(1);
	panel.add(slider_2, "cell 7 1,alignx left,aligny top");
    }

    private void displayImage(File file) {
	try {
	    img = ImageIO.read(file);
	    imagePanel.setImage(img);
	} catch (Exception e) {
	    feedback("cant load image " + e.toString());
	}
    }

    private void displayImage(BufferedImage img) {
	imagePanel.setImage(img);
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
	repaint();
    }

    private void feedback(String err) {
	try {
	    JOptionPane.showMessageDialog(this, err);
	} catch (HeadlessException e) {
	    e.printStackTrace();
	}
    }

    public void displayShape(int shape) {
	int squaresPerWidth = slider.getValue();
	int shapeSizeRandomness = slider_1.getValue();
	int shapeLocationRandomness = slider_2.getValue();
	int averagingRadius = 1;
	try {
	    String str2 = textField.getText();
	    averagingRadius = Integer.parseInt(str2);
	} catch (Exception e2) {
	}
	imageProcessor.setShape(shape);
	imageProcessor.setShapeSizeRandomness(shapeSizeRandomness);
	imageProcessor.setShapeLocationRandomness(shapeLocationRandomness);
	imageProcessor.setSquaresPerWidth(squaresPerWidth);
	imageProcessor.setAveragingRadius(averagingRadius);
	imageProcessor.setRandomRotate(checkBox.isSelected());
	imageProcessor.setSymetricShape(checkBox_1.isSelected());
	
	BufferedImage modified = imageProcessor.getTransformedImage(img);
	displayImage(modified);
    }

    private static void addPopup(Component component, final JPopupMenu popup) {
	component.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    showMenu(e);
		}
	    }

	    public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    showMenu(e);
		}
	    }

	    private void showMenu(MouseEvent e) {
		popup.show(e.getComponent(), e.getX(), e.getY());
	    }
	});
    }
}
