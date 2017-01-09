package assets;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

public class StartProgram {
	private final static String defaultSavePath = "/temp/temp_program.ser";
	private static ProgramController controller;
	
	public static void go(Program model){
		ProgramFrame view = new ProgramFrame(model);
		controller  = new ProgramController(model, view);
		view.setVisible(true);
		// auto-save when closed to the default save path
		view.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				controller.saveProgram(defaultSavePath);
				System.exit(0); // close JVM
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		// using MVC pattern to seperate GUI, data, and a controller to handle events
		// each is created only once here and then passed around.
		
		// load default save if it exists, otherwise start new
		File defaultSave = new File(defaultSavePath);
		Program model;

		if (defaultSave.exists())
			model = ProgramController.getModelFromPath(defaultSavePath);
		else
			model = new Program();
		
		go(model);
		 //System.out.println(model.obsString()); // debug structure of model with observers
	}
}
