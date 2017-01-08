package assets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class CourseFrame extends JFrame implements Observer{
	private static final long serialVersionUID = -7230387799739827608L;
	private Course c_model;
	JLabel courseL, creditsL, semesterL, roomL; 
	JButton addTask;
	JScrollPane taskPane;
	JTable taskTable;
	boolean visible;
	
	public CourseFrame(Course c){
		c_model = c;
		this.setSize(600, 600);
		this.setTitle(c.getName());
		this.setLayout(new BorderLayout());
		// Display general course information
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
		//  Table of tasks in middle of screen
		if (c_model.getNumTasks() == 0){
			taskPane = new JScrollPane();
			taskPane.add(new JLabel("No tasks yet..."));
		} else {
			taskPane = createTaskPane();
		}
		this.add(taskPane, BorderLayout.CENTER);			
		// Bottom of Course Panel - buttons and stuff for interacting with table
		JPanel bottom = new JPanel(new FlowLayout());
		// Add task button
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
		// Delete task button
		JButton deleteTask = new JButton("Delete Task");
		deleteTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (taskTable.getSelectedRow() >= 0){
					c_model.removeTask(taskTable.getSelectedRow());
				}
			}
		});
		bottom.add(deleteTask);
		this.add(bottom, BorderLayout.PAGE_END);
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		super.setVisible(visible);
	}
	
	// returns scroll pane containing task table
		public JScrollPane createTaskPane(){
			TableModel dataModel = new AbstractTableModel() {
				private static final long serialVersionUID = 3400114293267312129L;
				
				String[] columnNames = {"Task",
															"Due Date",
															"Grade",
															"Weight",
															"Submitted"};
		          public int getRowCount() { 
		        	  return c_model.getNumTasks();
		          }
		          public int getColumnCount() {
		        	  return columnNames.length;
		          }
		          public String getColumnName(int index){
		        	  return columnNames[index];
		          }
		          public Class<?> getColumnClass(int columnIndex){
		        	  switch (columnIndex){
		        	  	case 2:
		        	  		return Double.class;
		        	  	case 3:
		        	  		return Double.class;
		        	  	case 4:
		        	  		return Boolean.class;
		        	  	default:
		        	  		return String.class;
		        	  }
		          }
		          public boolean isCellEditable(int row, int column){
		        	  return true;
		          }
		          public void setValueAt(Object value, int row, int col) {
		        	  System.out.println("Setting value");
		        	  Task t = c_model.getTask(row);
		        	  switch (col){
			        	  case 0: 
			        		  t.setName((String)value);
			        		  break;
			        	  case 1: 
			        		  t.setDueDate(new Date((String)value)); // fix later
			        		  break;
			        	  case 2: 
			        		  // percentage view to  fractional 
			        		  t.setGrade((Double)value * 0.01);
			        		  break;
			        	  case 3: 
			        		  t.setWeight((Double)value);
			        		  break;
			        	  case 4: 
			        		  t.setComplete((Boolean)value);
			        		  break;
		        	  }
		              fireTableCellUpdated(row, col);
		              updateScreen();
		          }
		          public Object getValueAt(int row, int col) { 
		        	  Task t = c_model.getTask(row);
		        	  switch (col){
		        	  case 0: return t.getName();
		        	  case 1: 
		        		  Date d = t.getDueDate();
		        		  DateFormat df = new SimpleDateFormat("EEE, MMM dd");
		        		  return df.format(d);
		        	  case 2: return t.getGrade() * 100; // fraction to percentage view
		        	  case 3: return t.getWeight();
		        	  case 4: return t.isComplete();
		        	  }
		        	  return null;
		          }
		      };
		      taskTable = new JTable(dataModel);
		      return new JScrollPane(taskTable);
		}
	
	// Updates everything on screen - inefficient but amount of information to update
	// shouldn't be that much -> should refactor to only listen for specific task events
	public void updateScreen(){
		this.remove(taskPane);
		taskPane = createTaskPane();
		this.add(taskPane, BorderLayout.CENTER);
		this.setVisible(true);
	}

	// Called by model when model is updated, delegates to updateScreen()
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Course Frame has received update");
		// only update the frame if it is currently open
		if (visible)
			updateScreen();
	}
}
