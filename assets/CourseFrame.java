package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CourseFrame extends JFrame implements Observer{
	private static final long serialVersionUID = -7230387799739827608L;
	private Course c_model;
	
	JPanel header;				// will be removing and re-adding on update
	JScrollPane taskPane;
	JTable taskTable;
	
	public CourseFrame(Course c){
		c_model = c;
		this.setSize(600, 400);
		this.setTitle(c.getName());
		this.getContentPane().setBackground(SystemColor.inactiveCaption);
		this.setLayout(new BorderLayout());
		
		// Display general course information
		header = createHeader();
		this.add(header, BorderLayout.PAGE_START);
		
		//  Table of tasks in middle of screen
		taskPane = createTaskPane(c_model.getTaskList());
		this.add(taskPane, BorderLayout.CENTER);		
		
		// Bottom of Course Panel - buttons and stuff for interacting with table
		this.add(createFooter(), BorderLayout.PAGE_END);
	}
	
	private JPanel createHeader(){
		JPanel header = new JPanel(new GridLayout(0,1));
		header.setBackground(SystemColor.inactiveCaption);
		header.add(new JLabel("Course: " + c_model.getName() + " (" + c_model.getCode() + ")"));
		header.add(new JLabel("Worth " + c_model.getCreditHours() + " credit(s)"));
		header.add(new JLabel("Semester: " + c_model.getSemester().toString()));
		header.add(new JLabel("Room: " + c_model.getClassRoom()));
		header.add(new JLabel("Grade: " + String.format("%.2f", c_model.getGrade() * 100) + " %"));
		return header;
	}
	
	// returns scroll pane containing task table
	public JScrollPane createTaskPane(ArrayList<Task> taskList){
		if (c_model.getNumTasks() == 0){
			JScrollPane taskPane = new JScrollPane();
			taskPane.add(new JLabel("No tasks yet..."));
			return taskPane;
		} else {
			TaskTableModel dataModel = new TaskTableModel(taskList);
			taskTable = new TaskTable(dataModel);
			return new JScrollPane(taskTable);		
		}
	}
	
	private JPanel createFooter(){
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setBackground(SystemColor.inactiveCaption);
		JButton addTask  = new JButton("Add Task"); // Add task button
		bottom.add(addTask);
		addTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				c_model.addTask(new Task("New Task"));	
			}	
		});
		// Delete task button
		JButton deleteTask = new JButton("Delete Task");
		deleteTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int viewRow = taskTable.getSelectedRow();
				int modelRow = taskTable.convertRowIndexToModel(viewRow);
				if (modelRow >= 0){
					c_model.removeTask(modelRow);
				}
			}
		});
		bottom.add(deleteTask);
		return bottom;
	}
	
	// Updates everything on screen - inefficient but amount of information to update
	// shouldn't be that much -> should refactor to only listen for specific task events
	public void updateScreen(){
		this.remove(header);
		header = createHeader();
		this.add(header, BorderLayout.PAGE_START);
		this.remove(taskPane);
		taskPane = createTaskPane(c_model.getTaskList());
		this.add(taskPane, BorderLayout.CENTER);
		// don't need to update footer
		this.setVisible(true);
	}

	// Called by model when model is updated, delegates to updateScreen()
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Course Frame has received update");
		// only update the frame if it is currently open
		if (isVisible())
			updateScreen();
	}
}
