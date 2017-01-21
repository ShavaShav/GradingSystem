package assets;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import javax.swing.SwingUtilities;

public class StartProgram {
	private final static String defaultSavePath = "temp_program.ser";
	private static ProgramController controller;
	
	// given the program model, create controller and frame
	public static void go(Program model){
		ProgramFrame view = new ProgramFrame(model);
		// controller is replaced here, so only 1 program can be open at a time
		controller  = new ProgramController(model, view);
		view.setVisible(true);
		view.setSize(700, 600);
		// auto-save when closed to the default save path
		view.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				controller.saveProgram(defaultSavePath);
				System.exit(0); // close JVM
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        // load default save if it exists, otherwise start new
		        File defaultSave = new File(defaultSavePath);
		        Program model;
		        // load temp file if exists
		        if (defaultSave.exists())
		        	model = ProgramController.getModelFromPath(defaultSavePath);
		        else
		        	model = new Program();
		        // attach model to controller and view
		        go(model);
		    }
		});
	}
}
