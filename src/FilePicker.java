import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FilePicker extends JPanel{
    private static final long serialVersionUID = 4469046972842525628L;
    final JFileChooser fileChooser = new JFileChooser();
    private String directoryOfPreviousDirectory = "dir.txt";

    public FilePicker() {
    }

    private void createDirectoryFile() {
	File file = new File(directoryOfPreviousDirectory);
	try {
	    file.setWritable(true);
	    file.createNewFile();
	} catch (IOException e) {
	    feedback(e.toString());
	    // e.printStackTrace();
	}
    }

    private void feedback(String err) {
	try {
	    JOptionPane.showMessageDialog(this, err);
	} catch (HeadlessException e) {
	    e.printStackTrace();
	}
    }

    public boolean isDirectoryDataFileExists() {
	try {
	    return new File(directoryOfPreviousDirectory).exists();
	} catch (Exception e) {
	    return false;
	}
    }

    private void setPreviousDirectory(String dir,String lastFile) {
	if (!isDirectoryDataFileExists()) {
	    createDirectoryFile();
	}
	List<String> list = new ArrayList<String>();
	list.add(dir);
	list.add(lastFile);
	toFile(list, directoryOfPreviousDirectory);
    }

    public File getPreviouslyLoadedDirectory() {
	List<String> list = fromFile(directoryOfPreviousDirectory);
	return new File(list.get(0));
    }
    
    public File getPreviouslyLoadedFile() {
	List<String> list = fromFile(directoryOfPreviousDirectory);
	if(list.size()>1){
	    return new File(list.get(1));
	}
	return null;
    }

    /**
     * @param List<String>
     * @param String
     *            path
     */
    public void toFile(List<String> list, String path) {
	try {
	    FileWriter x = new FileWriter(path);
	    PrintWriter y = new PrintWriter(x);
	    for (int i = 0; i < list.size(); i++) {
		y.print(list.get(i));
		// this prevents printing last line as empty
		if (i < list.size() - 1) {
		    y.print("\r");
		}
	    }
	    x.close();
	    y.close();

	} catch (FileNotFoundException e) {
	    feedback("toFile FileNotFoundException : " + e.toString());
	} catch (IOException e) {
	    feedback("toFile IOEException : " + e.toString());
	}
    }

    /**
     * @return List<String>
     */
    public List<String> fromFile(String path) {
	List<String> lines = new ArrayList<String>();
	try {
	    FileReader x = new FileReader(path);
	    BufferedReader y = new BufferedReader(x);
	    lines.add(y.readLine());
	    String str;
	    while ((str = y.readLine()) != null) {
		lines.add(str);
	    }
	    x.close();
	    y.close();

	} catch (EOFException e) {
	    feedback("\n" + "fromFile EOFE : " + e.toString());
	} catch (FileNotFoundException e) {
	    feedback("\n" + "fromFile FileNotFoundException : " + e.toString());
	} catch (IOException e) {
	    feedback("\n" + "fromFile IOException : " + e.toString());
	}
	return lines;
    }

    public File loadFile() {
	File file=null;
	if (isDirectoryDataFileExists()) {
	    fileChooser.setCurrentDirectory(getPreviouslyLoadedDirectory());
	} else {
	    createDirectoryFile();
	}
	int returnVal = fileChooser.showDialog(this, "Load File");

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    file = fileChooser.getSelectedFile();
	    String dir = file.getParent();
	    setPreviousDirectory(dir,file.getAbsolutePath());
	    return file;
	} else {
	    feedback("File choice was not approved: ");
	}
	return file;

    }

}
