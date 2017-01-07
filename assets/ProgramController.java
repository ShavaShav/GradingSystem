package assets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ListIterator;

import javax.swing.JFileChooser;

public class ProgramController {
	private Program p_model;
	private ProgramFrame p_view;
	
	// constructor
	public ProgramController(Program model, ProgramFrame view){
		p_model = model;
		p_view = view;
		
		// have view observe model so it updates appropriately
		p_model.addObserver(p_view);
		setModelObservers();
		
		// add listeners
		view.addLoadListener(new LoadListener());
		view.addSaveListener(new SaveListener());
	}
	
	// to be invoked when creating a new controller from a serialized model - need to reconnect observers
	private void setModelObservers(){
		ListIterator<Course> courseList = p_model.getIterator();
		while(courseList.hasNext()){
			Course c = courseList.next();
			c.addObserver(p_model); // program model observes course model
			for (Task t : c.taskList){
				t.addObserver(c); // course model observes task model
			}
		}
	}
	
	public void saveProgram(String filePath){
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);	
			System.out.println("Writing Program Object...");
			out.writeObject(p_model);
			out.close();
			fileOut.close();
		} catch(FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static Program getModelFromPath(String filePath){
		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);	
			System.out.println("Loading Program Object...");
			Program model = (Program)in.readObject();
			System.out.println(model+" -> loaded");
			in.close();
			fileIn.close();
			return model;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch(FileNotFoundException ex) {
			return new Program();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public class SaveListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(p_view) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				saveProgram(file.getPath());
			}
		}
	}
	
	public class LoadListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(p_view) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				p_model =getModelFromPath(file.getPath());
				p_view.dispose();
				StartProgram.go(p_model);
			}	
		}
	}
}

