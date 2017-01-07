package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgramFrame extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1L;
	private Program program;	
	private HashMap<Course, CourseController> c_controllers;
	
	private JButton newProgram, addCourse, saveButton, loadButton;	
	private JLabel progLabel, credLabel;
	private JPanel coursePanel, mainPanel;
	private int height, width;
	
	
	public ProgramFrame(Program p){
		program = p;	// use model given
		c_controllers = new HashMap<Course, CourseController>();
		height = 600;
		width = 700;
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setTitle("Grade Tracker");
		this.add(createHeader(), BorderLayout.PAGE_START);
		mainPanel = createMainPanel();
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(createFooter(), BorderLayout.PAGE_END);		
		try{
			updateScreen();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private JPanel createHeader(){
		JPanel programButtons = new JPanel(new FlowLayout());
		JPanel pb2 = new JPanel(new GridLayout(1,3));		
		newProgram = new JButton("New Program");
		loadButton = new JButton("Load Program");
		saveButton = new JButton("Save Program");
		pb2.add(newProgram);
		newProgram.addActionListener(this);
		// Load and Save listeners implemented by controller
		pb2.add(loadButton);
		pb2.add(saveButton);
		programButtons.add(pb2);
		return programButtons;
	}
	
	private JPanel createMainPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel mainLabels = new JPanel(new GridLayout(0,1));
		mainLabels.setSize(width, 30);
		progLabel  = new JLabel("Program: ");
		credLabel  = new JLabel("Credits: ");
		mainLabels.add(progLabel);
		mainLabels.add(credLabel);
		mainPanel.add(mainLabels, BorderLayout.PAGE_START);
		if (program.getNumCourses() == 0){
			coursePanel = new JPanel();
			coursePanel.add(new JLabel("No courses yet..."));
		} else {
			coursePanel = createCoursePanel();
		}
		mainPanel.add(coursePanel, BorderLayout.CENTER);		
		return mainPanel;
	}
	
	private JPanel createFooter(){
		JPanel bottom = new JPanel(new FlowLayout());
		addCourse  = new JButton("Add Course");
		bottom.add(addCourse);
		addCourse.addActionListener(this);
		return bottom;
	}
	
	public void addCourseController(){
		
	}
	
	public JPanel createCoursePanel(){
		JPanel coursePanel = new JPanel(new GridLayout(0,6));
		String[] columnNames = {"Course #",
				"Name",
				"Credits",
				"Semester",
				"Room",
		"Info"};
		for (int i = 0; i < columnNames.length; i++){
			coursePanel.add(new JLabel(columnNames[i]));
		}
		if (program != null){			
			ListIterator<Course> courses = program.getIterator();

			while (courses.hasNext()){
				Course c = courses.next();
				coursePanel.add(new JLabel(c.getCode()));
				coursePanel.add(new JLabel(c.getName()));
				coursePanel.add(new JLabel(Integer.toString(c.getCreditHours())));
				coursePanel.add(new JLabel(c.getSemester().toString()));
				coursePanel.add(new JLabel(c.getClassRoom()));
				JButton infoButton = new JButton("Info");
				coursePanel.add(infoButton);
				infoButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						// if course doesn't have controller, add it on fly
						if (!c_controllers.containsKey(c)){
							CourseFrame c_view = new CourseFrame(c);
							c_controllers.put(c, new CourseController(c, c_view));							
						}
						// show the view for course using controller
						c_controllers.get(c).showView();
					}		
				});
			}
		}
		return coursePanel;
	}
	
	public void addLoadListener(ActionListener lal){
		loadButton.addActionListener(lal);
	}
	
	public void addSaveListener(ActionListener sal){
		saveButton.addActionListener(sal);
	}

	public void updateScreen(){
		// Update program information
		progLabel.setText("Program: " + program.getName());
		credLabel.setText("Credits: " + program.getCredits());
		
		// Update course panel
		if (program.getNumCourses() > 0){
			mainPanel.remove(coursePanel);
			coursePanel = createCoursePanel();
			mainPanel.add(coursePanel);			
		}
		
		this.setVisible(true);
	}

	// Updates screen when model is updated
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Program Frame has received update");
		updateScreen();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Program")){
			// pass this frame so that it can be closed by np_view
			NewProgramFrame np_view = new NewProgramFrame(this);
			np_view.setVisible(true);
			np_view.setAlwaysOnTop(true);
			np_view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);			
		} else if (e.getActionCommand().equals("Add Course")){
			AddCourseFrame ac_view = new AddCourseFrame(program);
			ac_view.setVisible(true);
			ac_view.setAlwaysOnTop(true);
			ac_view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}
	
}
