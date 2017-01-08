package assets;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CourseController {
	private Course c_model;
	private CourseFrame c_view;
	
	public CourseController(Course model, CourseFrame view){
		c_model = model;
		c_view = view;
		// have view observe model so it updates appropriately
		c_model.addObserver(c_view);
		c_view.setAlwaysOnTop(true);
		c_view.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				c_view.setVisible(false);
			}
		});
	}
	
	public void showView(){
		c_view.updateScreen();
	}
	
	public Course getModel(){
		return c_model;
	}
}
