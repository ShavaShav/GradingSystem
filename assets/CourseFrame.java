package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CourseFrame extends JFrame implements Observer{
	private static final long serialVersionUID = -7230387799739827608L;
	private Course c_model;
	JLabel courseL, creditsL, semesterL, roomL; 
	JButton addTask;
	JPanel taskPanel;
	
	public CourseFrame(Course c){
		c_model = c;
		this.setSize(600, 600);
		this.setTitle(c.getName());
		this.setLayout(new BorderLayout());
		
		JPanel header = new JPanel(new GridLayout(0,1));
		courseL = new JLabel("Course: " + c.getName() + " (" + c.getCode() + ")");
		header.add(courseL);
		creditsL = new JLabel("Worth " + c.getCreditHours() + " credit(s)");
		header.add(creditsL);
		semesterL = new JLabel("Semester: " + c.getSemester().toString());
		header.add(semesterL);
		roomL = new JLabel("Room: " + c.getClassRoom());
		header.add(roomL);
		this.add(header, BorderLayout.PAGE_START);
		
		if (c_model.getNumTasks() == 0){
			taskPanel = new JPanel();
			taskPanel.add(new JLabel("No tasks yet..."));
		} else {
			taskPanel = createTaskPanel();
		}
		this.add(taskPanel, BorderLayout.CENTER);			
		
		JPanel bottom = new JPanel(new FlowLayout());
		addTask  = new JButton("Add Task");
		bottom.add(addTask);
		addTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AddTaskFrame at_view = new AddTaskFrame(c_model);
				at_view.setAlwaysOnTop(true);
				at_view.setVisible(true);
				at_view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);				
			}	
		});
		this.add(bottom, BorderLayout.PAGE_END);
	}
	
	public JPanel createTaskPanel(){
		taskPanel = new JPanel(new GridLayout(0,5));
		String[] columnNames = {"Task",
				"Due Date",
				"Grade",
				"Weight",
				"Complete?"};
		for (int i = 0; i < columnNames.length; i++){
			taskPanel.add(new JLabel(columnNames[i]));
		}
		ListIterator<Task> tasks = c_model.getIterator();
		while (tasks.hasNext()){
			Task t = tasks.next();
			taskPanel.add(new JLabel(t.getName()));
			taskPanel.add(new JLabel(t.getDueDate().toString()));
			taskPanel.add(new JLabel(Double.toString(t.getGrade())));
			taskPanel.add(new JLabel(Double.toString(t.getWeight())));
			taskPanel.add(new JCheckBox("", t.isComplete()));
		}
		return taskPanel;
	}
	
	public void updateScreen(){
		this.remove(taskPanel);
		taskPanel = createTaskPanel();
		this.add(taskPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	// Updates screen when model is updated
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Course Frame has received update");
		updateScreen();
	}
}
