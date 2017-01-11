package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ProgramFrame extends JFrame implements Observer, ActionListener{
	private static final long serialVersionUID = 1L;
	private Program program;	
	private HashMap<Course, CourseController> c_controllers;
	private ArrayList<Task> deadlines;
	
	private JButton newProgram, addCourse, saveButton, loadButton;	
	private JLabel progLabel, credLabel, cumAvgLabel, mjrAvgLabel;
	private JPanel mainPanel, deadlinePanel, centralPanel;
	private JScrollPane coursePane, deadlineTablePane;
	private JTable courseTable, deadlineTable;
	private JProgressBar completionBar;
	
	private int height = 800, width = 700;
	
	public ProgramFrame(Program p){
		// use model given
		program = p;	
		// each course model has it's own controller for displaying views
		c_controllers = new HashMap<Course, CourseController>();
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.setTitle("Grade Tracker");
		
		// New program, save and load buttons
		this.add(createHeader(), BorderLayout.PAGE_START);
		
		centralPanel = new JPanel(new GridLayout(0,1));
		
		// Displays program information and course table
		mainPanel = createMainPanel();
		centralPanel.add(mainPanel);
		// add panel containing deadlines
		deadlinePanel = createDeadlinePanel();
		centralPanel.add(deadlinePanel);
		
		this.add(centralPanel, BorderLayout.CENTER);
		
		// Buttons for manipulating the table (deleting, adding courses, etc)
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
		
		mainPanel.add(createProgramInfoPanel(), BorderLayout.PAGE_START);
		
		coursePane = createCourseTablePane(program.getCourseList());
		mainPanel.add(coursePane, BorderLayout.CENTER);		
		
		return mainPanel;
	}
	
	private JPanel createDeadlinePanel(){
		JPanel deadlinePanel = new JPanel(new BorderLayout());
		JPanel limitPanel = new JPanel(new GridLayout(0,1));
		limitPanel.add(new JLabel("Upcoming Deadlines"));
		String[] options = {"1 week", "2 weeks", "3 weeks", "4 weeks"};
		JComboBox limits = new JComboBox(options);
		limits.setSelectedIndex(1); // default to 2 weeks
		limits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				 JComboBox cb = (JComboBox)e.getSource();
				 int weeks = cb.getSelectedIndex() + 1; // as 1 week = 0
				 Date limit = new Date();
				 Calendar calendar = Calendar.getInstance();
				 calendar.setTime(limit);            
				 calendar.add(Calendar.WEEK_OF_YEAR, weeks);
				 limit = calendar.getTime();
				 deadlines = program.getUpcomingDeadlines(limit);
				 updateScreen();
			}
		});
		limitPanel.add(limits);
		deadlinePanel.add(limitPanel, BorderLayout.PAGE_START);
		
		Calendar calendar = Calendar.getInstance();
		 calendar.setTime(new Date());            
		 calendar.add(Calendar.WEEK_OF_YEAR, 2);
		deadlines = program.getUpcomingDeadlines(calendar.getTime());
		deadlineTablePane = createDeadlineTablePane(deadlines);
		deadlinePanel.add(deadlineTablePane, BorderLayout.CENTER);		
		
		return deadlinePanel;
	}
	
	private JPanel createProgramInfoPanel(){
		JPanel programInfoPanel = new JPanel(new GridLayout(0,1));
		programInfoPanel.setSize(width, 30);
		progLabel  = new JLabel("Program: ");
		credLabel  = new JLabel("Credits: ");
		programInfoPanel.add(progLabel);
		programInfoPanel.add(credLabel);
		JPanel gradeLabels = new JPanel(new GridLayout(0,3));
		cumAvgLabel = new JLabel("Cumulative Average: ");
		gradeLabels.add(cumAvgLabel);
		mjrAvgLabel = new JLabel("\tMajor Average: ");
		gradeLabels.add(mjrAvgLabel);
		programInfoPanel.add(gradeLabels);
		completionBar = new JProgressBar(0, program.getCredits());
		completionBar.setValue(0);
		completionBar.setStringPainted(true);
		programInfoPanel.add(completionBar);
		return programInfoPanel;
	}
	
	// returns scroll pane containing course table
	public JScrollPane createCourseTablePane(ArrayList<Course> courseList){
		if (program.getNumCourses() == 0){
			JScrollPane coursePane = new JScrollPane();
			coursePane.add(new JLabel("No courses yet..."));
			return coursePane;
		} else {
			CourseTableModel dataModel = new CourseTableModel(courseList);
		    courseTable = new CourseTable(dataModel);
		    return new JScrollPane(courseTable);
		}
	}
	
	// returns scroll pane containing course table
	public JScrollPane createDeadlineTablePane(ArrayList<Task> deadlines){
		if (deadlines.isEmpty()){
			System.out.println("No deadlines");
			JScrollPane coursePane = new JScrollPane();
			coursePane.add(new JLabel("No tasks for next 2 weeks!"));
			return coursePane;
		} else {
			TaskTableModel dataModel = new TaskTableModel(deadlines);
		    deadlineTable = new TaskTable(dataModel);
		    return new JScrollPane(deadlineTable);
		}
	}
	
	private JPanel createFooter(){
		JPanel bottom = new JPanel(new FlowLayout());	
		
		JButton viewCourse = new JButton("View Course Details");
		viewCourse.addActionListener(this);
		bottom.add(viewCourse);
		
		addCourse  = new JButton("Add Course");
		bottom.add(addCourse);
		addCourse.addActionListener(this);
		
		JButton deleteCourse = new JButton("Delete Course");
		deleteCourse.addActionListener(this);
		bottom.add(deleteCourse);
		
		return bottom;
	}
	
	public void addLoadListener(ActionListener lal){
		loadButton.addActionListener(lal);
	}
	
	public void addSaveListener(ActionListener sal){
		saveButton.addActionListener(sal);
	}

	// Other button listeners implemented here
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
		} else if (e.getActionCommand().equals("Delete Course")){
			int tableRow = courseTable.getSelectedRow();
			int modelRow = courseTable.convertRowIndexToModel(tableRow);
			if (modelRow >= 0)
				program.removeCourse(modelRow);
		} else if (e.getActionCommand().equals("View Course Details")){
			// convert from view to model index, incase view changed
			int tableRow = courseTable.getSelectedRow();
			int modelRow = courseTable.convertRowIndexToModel(tableRow);
			if (modelRow >= 0){ // selected course
				Course c = program.getCourseList().get(modelRow);
				// if course doesn't have controller, add it on fly
				if (!c_controllers.containsKey(c)){
					CourseFrame c_view = new CourseFrame(c);
					c_controllers.put(c, new CourseController(c, c_view));							
				}
				// show the view for course using controller
				c_controllers.get(c).showView();
			}
		}
	}

	public void updateScreen(){
		// Update program information
		progLabel.setText("Program: " + program.getName());
		credLabel.setText("Credits: " + program.getCredits());
		String cumAvg = String.format("%.2f", program.getCumulativeAverage()*100);
		cumAvgLabel.setText("Cumulative Average: "+ cumAvg +" %");
		String mjrAvg = String.format("%.2f", program.getMajorAverage()*100);
		mjrAvgLabel.setText("Major Average: " + mjrAvg + " %");
		completionBar.setValue(program.getCreditsEarned());
		
		// Update course pane
		if (program.getNumCourses() > 0){
			mainPanel.remove(coursePane);
			coursePane = createCourseTablePane(program.getCourseList());
			mainPanel.add(coursePane);
			deadlinePanel.remove(deadlineTablePane);
			deadlineTablePane = createDeadlineTablePane(deadlines);
			deadlinePanel.add(deadlineTablePane);
		}
		
		this.setVisible(true);
	}

	// Updates screen when model is updated
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Program Frame has received update");
		updateScreen();
	}
}
