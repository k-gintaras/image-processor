import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * The MIT License (MIT) Copyright (c)
 * 
 * <2016><Gintaras Koncevicius>(@author Ubaby)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
@SuppressWarnings("unused")
public class MainWindowTesting extends Thread implements ActionListener {
    /**
     * various helpers pre-loaded
     */
    // HelpMeTo help = new HelpMeTo();
    WindowBuilder windowHelper = new WindowBuilder();
    // ListHelper listhelp = new ListHelper();
    // FileHelp fileHelp = new FileHelp();
    // FilesHelper filesHelp = new FilesHelper();

    /**
     * #############################################################################################################
     * MAIN CONCERN START Setup UI
     */
    String startingDir = "C:\\Users\\Ubaby\\Desktop\\test";
    String WINDOW_TITLE = "file";
    String USER_INPUT_TITLE_SUGGESTION = "file";

    int USER_INPUT_WIDTH = 1000;
    int USER_INPUT_HEIGHT = 20;
    int inputId = 0;
    int outputId = 0;
    int delayInMs = 1000;

    public static void main(String[] args) {
	MainWindowTesting t = new MainWindowTesting();
	t.showAsWindow(); // to actually display this thing
	t.enableFilePicker();
    }

    /**
     * #####################################################################################
     * HERE IS WHERE ALL THE MAGIC
     * 
     * @input String userInput = user can enter text any way you like, comma
     *        separated, space separated, whole CSV file
     * @output String = just a string or anything you do with the input user
     *         gave, maybe you just created a new file and just output string
     *         "OK" you parse user input, which can be text or command or comma
     *         separated multiple commands you output whatever you do with that
     *         input to the output text area or maybe just create a file locally
     *         and output OK
     *         #####################################################################################
     */
    public String processInput(String userInput) {
	String result = "", n = "\n";
	// TODO create code to parse user input
	// process user input
	// return some result as a string

	// last row will not have unnecessary new line
	// if(i<len-1){
	// result+=n;
	// }
	return result;// your output
    }

    public String processInputSlowly(String data) {
	delayInMs = 1000;
	int iterations = 1;
	class Quick extends Thread {
	    String result = "";

	    public Quick(String infoIn) {
	    }

	    public void run() {
		for (int i = 0; i < iterations; i++) {
		    try {
			Quick.sleep(delayInMs);
			/**
			 * where each input is processed given iterations
			 */
			result += "" + "\n";

			displayOutput("Loading: (" + i + "/" + iterations + ")" + loading(i));
		    } catch (InterruptedException ex) {
			result += "" + ex.toString() + "\n\n";
		    } catch (Exception e) {
			result += "" + e.toString() + "\n\n";
		    }
		}
		/**
		 * where acumulated result is finally displayed
		 */
		displayOutput(result);// final result after loop ends
	    }

	}
	new Quick(data).start();
	return "OK";
    }

    /**
     * USE and modify SORTER IF NEEDED
     */
    class Sorter implements Comparable<Sorter> {
	public String data;

	public Sorter(String data) {
	    this.data = data;
	}

	public int compareTo(Sorter o) {
	    return this.data.length() - o.data.length();
	}
    }

    /**
     * MAIN CONCERN END IGNORE ALL BELOW
     * #############################################################################################################
     */
    public String loading(int times) {
	String all = "";
	for (int i = 0; i < times; i++) {
	    all += ".";
	}
	return all;
    }

    public MainWindowTesting() {
    }

    public void displayOutput(String result) {
	windowHelper.getTextAreas().get(outputId).setText(result);
    }

    public String getInput() {
	return windowHelper.getTextAreas().get(inputId).getText();
    }

    private void showAsWindow() {
	windowHelper.initializeDisplay(WINDOW_TITLE, 1400, 800);
	
//	windowHelper.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//	windowHelper.setUndecorated(true);
//	windowHelper.setVisible(true);
	
	windowHelper.getMainPanel().setBackground(new Color(100, 100, 100));
	windowHelper.setInputOutputArea();
	windowHelper.setInputOutputArea();
	windowHelper.setInputOutputArea();
	windowHelper.setInputTextArea(USER_INPUT_TITLE_SUGGESTION, 0);
	List<JTextArea> textAreas = windowHelper.getTextAreas();
	textAreas.get(1).setPreferredSize(new Dimension(USER_INPUT_WIDTH, USER_INPUT_HEIGHT));
	inputId = 1;
	windowHelper.setButtonArea("Start", 1);
	windowHelper.setOutputTextArea("Output", 2);
	outputId = 3;
	setListeners();
	windowHelper.setVisible();
    }

    private int filePickerId = 0;

    private void enableFilePicker() {
	windowHelper.setButtonArea("LoadFile", 1);
	JButton b = windowHelper.getButtons().get(1);
	filePickerId = 1;
	File file = null;
//	new FilePicker(windowHelper, b, file);
	
	while(file==null){
	    System.out.println("file empty");
	    try {
		MainWindowTesting.sleep(500);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	System.out.println("file not empty");
    }

    public void setListeners() {
	for (int i = 0; i < windowHelper.getButtons().size(); i++) {
	    /**
	     * todo, ignore filepicker button its own actionlistener
	     */
	    if (i != filePickerId) {
		windowHelper.getButtons().get(i).addActionListener(this);
	    }
	}
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == windowHelper.getButtons().get(0)) {
	    String data = getInput();
	    String result = processInput(data);
	    displayOutput(result);
	}
    }
}
