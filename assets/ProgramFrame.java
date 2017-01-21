package assets;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
	private ArrayList<Course> coursesInView;
	private Date deadlineLimit;
	
	private JButton btnNewProgram, btnAddCourse, btnSaveProgram, btnLoadProgram;	
	private JLabel lblProgram, lblCredits, lblCumAvg, lblMjrAvg;
	private JScrollPane scrollPaneCourses, scrollPaneDeadlines;
	private JTable courseTable, deadlineTable;
	private JProgressBar completionBar;
	
	public ProgramFrame(Program p){
		// use model given
		program = p;	
		// each course model has it's own controller for displaying views
		c_controllers = new HashMap<Course, CourseController>();

		this.setTitle("Grade Tracker");
		this.getContentPane().setBackground(SystemColor.inactiveCaption);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{538, 0};
		gridBagLayout.rowHeights = new int[]{0, 19, 18, 27, 27, 22, 25, 20, 20, 21, -20, 34, 0, 0, 0, 0, 0, 0, 12, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panelProgButtons = new JPanel();
		panelProgButtons.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_panelProgButtons = new GridBagConstraints();
		gbc_panelProgButtons.insets = new Insets(0, 0, 5, 0);
		gbc_panelProgButtons.fill = GridBagConstraints.BOTH;
		gbc_panelProgButtons.gridx = 0;
		gbc_panelProgButtons.gridy = 0;
		add(panelProgButtons, gbc_panelProgButtons);                  
		
		btnNewProgram = new JButton("New Program");
		panelProgButtons.add(btnNewProgram);
		btnNewProgram.addActionListener(this);
		
		btnLoadProgram = new JButton("Load Program");
		panelProgButtons.add(btnLoadProgram);
		
		btnSaveProgram = new JButton("Save Program");
		panelProgButtons.add(btnSaveProgram);
		
		JPanel panelProgInfo = new JPanel();
		panelProgInfo.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_panelProgInfo = new GridBagConstraints();
		gbc_panelProgInfo.gridheight = 2;
		gbc_panelProgInfo.insets = new Insets(0, 0, 5, 0);
		gbc_panelProgInfo.fill = GridBagConstraints.BOTH;
		gbc_panelProgInfo.gridx = 0;
		gbc_panelProgInfo.gridy = 1;
		add(panelProgInfo, gbc_panelProgInfo);
		panelProgInfo.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblProgram = new JLabel("Program:");
		panelProgInfo.add(lblProgram);
		
		lblCredits = new JLabel("Credits:");
		panelProgInfo.add(lblCredits);
		
		JPanel gradeLabels = new JPanel(new GridLayout(0,3));
		gradeLabels.setBackground(SystemColor.inactiveCaption);
		lblCumAvg = new JLabel("Cumulative Average: ");
		gradeLabels.add(lblCumAvg);
		lblMjrAvg= new JLabel("\tMajor Average: ");
		gradeLabels.add(lblMjrAvg);
		panelProgInfo.add(gradeLabels);
		
		completionBar = new JProgressBar(0, program.getCredits());
		completionBar.setValue(0);
		completionBar.setStringPainted(true);
		GridBagConstraints gbc_completionBar = new GridBagConstraints();
		gbc_completionBar.insets = new Insets(0, 0, 5, 0);
		gbc_completionBar.gridx = 0;
		gbc_completionBar.gridy = 3;
		gbc_completionBar.fill = GridBagConstraints.BOTH;
		add(completionBar, gbc_completionBar);
		
		JPanel panelDeadlineChooser = new JPanel();
		panelDeadlineChooser.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_panelDeadlineChooser = new GridBagConstraints();
		gbc_panelDeadlineChooser.anchor = GridBagConstraints.WEST;
		gbc_panelDeadlineChooser.insets = new Insets(0, 0, 5, 0);
		gbc_panelDeadlineChooser.fill = GridBagConstraints.VERTICAL;
		gbc_panelDeadlineChooser.gridx = 0;
		gbc_panelDeadlineChooser.gridy = 4;
		add(panelDeadlineChooser, gbc_panelDeadlineChooser);
		panelDeadlineChooser.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));		
		addDeadlineChooserComponents(panelDeadlineChooser);
		
		// Add deadline table
		scrollPaneDeadlines = new JScrollPane();
		GridBagConstraints gbc_scrollPaneDeadlines = new GridBagConstraints();
		gbc_scrollPaneDeadlines.gridheight = 6;
		gbc_scrollPaneDeadlines.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneDeadlines.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneDeadlines.gridx = 0;
		gbc_scrollPaneDeadlines.gridy = 5;
		add(scrollPaneDeadlines, gbc_scrollPaneDeadlines);
		
		DeadlineTableModel deadlineTableModel = new DeadlineTableModel(program.getCourseList(), deadlineLimit);
	    deadlineTable = new DeadlineTable(deadlineTableModel);
		scrollPaneDeadlines.setViewportView(deadlineTable);
		
		// Add Course chooser
		JPanel panelCourseChooser = new JPanel();
		panelCourseChooser.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_panelCourseChooser = new GridBagConstraints();
		gbc_panelCourseChooser.anchor = GridBagConstraints.WEST;
		gbc_panelCourseChooser.insets = new Insets(0, 0, 5, 0);
		gbc_panelCourseChooser.fill = GridBagConstraints.VERTICAL;
		gbc_panelCourseChooser.gridx = 0;
		gbc_panelCourseChooser.gridy = 11;
		add(panelCourseChooser, gbc_panelCourseChooser);
		panelCourseChooser.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		addCourseChooserComponents(panelCourseChooser);
		
		// Add course table
		scrollPaneCourses = new JScrollPane();
		GridBagConstraints gbc_scrollPaneCourses = new GridBagConstraints();
		gbc_scrollPaneCourses.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneCourses.gridheight = 6;
		gbc_scrollPaneCourses.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneCourses.gridx = 0;
		gbc_scrollPaneCourses.gridy = 12;
		add(scrollPaneCourses, gbc_scrollPaneCourses);
		
		CourseTableModel courseTableModel = new CourseTableModel(program.getCourseList());
	    courseTable = new CourseTable(courseTableModel);
		scrollPaneCourses.setViewportView(courseTable);
		
		JPanel panelCourseButtons = new JPanel();
		panelCourseButtons.setBackground(SystemColor.inactiveCaption);
		GridBagConstraints gbc_panelCourseButtons = new GridBagConstraints();
		gbc_panelCourseButtons.fill = GridBagConstraints.BOTH;
		gbc_panelCourseButtons.gridx = 0;
		gbc_panelCourseButtons.gridy = 18;
		add(panelCourseButtons, gbc_panelCourseButtons);
		
		JButton btnViewCourseDetails = new JButton("View Course Details");
		panelCourseButtons.add(btnViewCourseDetails);
		btnViewCourseDetails.addActionListener(this);
		btnAddCourse = new JButton("Add Course");
		panelCourseButtons.add(btnAddCourse);
		btnAddCourse.addActionListener(this);
		JButton btnDeleteCourse = new JButton("Delete Course");
		panelCourseButtons.add(btnDeleteCourse);
		btnDeleteCourse.addActionListener(this);

		try{
			updateScreen();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void addDeadlineChooserComponents(JPanel panelDeadlineChooser){
		// Add Deadline chooser
		JLabel lblUpcomingDeadlines = new JLabel("Upcoming Deadlines:");
		panelDeadlineChooser.add(lblUpcomingDeadlines);
		
		String[] options = {"1 week", "2 weeks", "3 weeks", "4 weeks"};
		JComboBox limits = new JComboBox(options);
		limits.setSelectedIndex(1); // default to 2 weeks
		limits.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int weeks = cb.getSelectedIndex() + 1; // as 1 week = 0
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date()); 				 // today          
				calendar.add(Calendar.WEEK_OF_YEAR, weeks); // +2 weeks
				deadlineLimit = calendar.getTime();		 
				updateScreen(); // to update the deadline table
			}
		});
		panelDeadlineChooser.add(limits);

		// default deadline to 2 weeks past current date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());            
		calendar.add(Calendar.WEEK_OF_YEAR, 2);
		deadlineLimit = calendar.getTime();
	}
	
	public void addCourseChooserComponents(JPanel panelCourseChooser){
		JLabel lblCourses = new JLabel("Courses:");
		panelCourseChooser.add(lblCourses);
		ArrayList<Semester> semesters = program.getSemesters();
		Collections.sort(semesters, Collections.reverseOrder());
		Semester[] options = new Semester[semesters.size()];
		semesters.toArray(options);
		JComboBox semesterComboBox = new JComboBox(options);
		semesterComboBox.addItem("All Semesters");
		semesterComboBox.setSelectedIndex(0); // default to most recent semester
		if (program.getNumCourses() > 0)
			coursesInView = program.getCoursesBySemester(
								(Semester) semesterComboBox.getSelectedItem());
		semesterComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				if (cb.getSelectedIndex() == semesters.size()){
					coursesInView = program.getCourseList(); // ALL courses
				} else {
					coursesInView = program.getCoursesBySemester(
										(Semester) cb.getSelectedItem());					
				}
				updateScreen();
			}
		});
		panelCourseChooser.add(semesterComboBox);
	}
	
	public void addLoadListener(ActionListener lal){
		btnLoadProgram.addActionListener(lal);
	}
	
	public void addSaveListener(ActionListener sal){
		btnSaveProgram.addActionListener(sal);
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
			System.out.println("Deleting " + coursesInView.get(modelRow));
			if (modelRow >= 0)
				program.removeCourse(coursesInView.remove(modelRow));
		} else if (e.getActionCommand().equals("View Course Details")){
			// convert from view to model index, incase view changed
			int tableRow = courseTable.getSelectedRow();
			int modelRow = courseTable.convertRowIndexToModel(tableRow);
			if (modelRow >= 0){ // selected course
				Course c = coursesInView.get(modelRow);
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
		lblProgram.setText("Program: " + program.getName());
		lblCredits.setText("Credits: " + program.getCredits());
		String cumAvg = String.format("%.2f", program.getCumulativeAverage()*100);
		lblCumAvg.setText("Cumulative Average: "+ cumAvg +" %");
		String mjrAvg = String.format("%.2f", program.getMajorAverage()*100);
		lblMjrAvg.setText("Major Average: " + mjrAvg + " %");
		completionBar.setValue(program.getCreditsEarned());
		
		// Update deadline & course pane
		if (program.getNumCourses() > 0){
			CourseTableModel courseTableModel = new CourseTableModel(coursesInView);
			courseTable = new CourseTable(courseTableModel);
			scrollPaneCourses.setViewportView(courseTable);
			DeadlineTableModel deadlineTableModel = new DeadlineTableModel(program.getCourseList(), deadlineLimit);
			deadlineTable = new DeadlineTable(deadlineTableModel);
			scrollPaneDeadlines.setViewportView(deadlineTable); 
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
