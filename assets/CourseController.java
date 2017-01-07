package assets;

public class CourseController {
	private Course c_model;
	private CourseFrame c_view;
	
	public CourseController(Course model, CourseFrame view){
		c_model = model;
		c_view = view;
		// have view observe model so it updates appropriately
		c_model.addObserver(c_view);
	}
	
	public void showView(){
		c_view.setAlwaysOnTop(true);
		c_view.setVisible(true);
	}
	
	public Course getModel(){
		return c_model;
	}
}
