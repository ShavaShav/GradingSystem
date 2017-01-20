package assets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

public class DeadlineTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -8909820456560576412L;
	ArrayList<CourseTask> deadlines;
	Date limit;
	String[] columnNames = {"Task",
			"Course",
			"Due Date",
			"Weight",
			"Submitted"};
	
	public DeadlineTableModel(ArrayList<Course> courseList, Date limit){
		deadlines = new ArrayList<CourseTask>();
		for (Course c : courseList) {
			for (Task t : c.getUpcomingDeadlines(limit)) {
				deadlines.add(new CourseTask(c.getName(), t));
			}
		}
	}
	
	public int getRowCount() { 
  	  return deadlines.size();
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
			return Date.class;
  	  	case 4:
  	  		return Boolean.class;
  	  	default:
  	  		return String.class;
  	  }
    }
    
    public boolean isCellEditable(int row, int column){
  	  if (column == 4)
  		return true; // submitted checkbox
  	  else
    	return false;
    }
    
    // updates to table model need to be passed to task model
    public void setValueAt(Object value, int row, int col) {
    	if (col == 4){ // submitted checkbox
    		deadlines.get(row).task.setComplete(true);
    	} else {
    		System.out.println("Editing not allowed on deadlines panel");    		
    	}
    }
    
    // values of model
    public Object getValueAt(int row, int col) { 
  	  CourseTask ct = deadlines.get(row);
  	  Task t = ct.task;
  	  switch (col){
  	  case 0: return t.getName();
  	  case 1: return ct.courseName;
  	  case 2: return t.getDueDate(); // fraction to percentage view  			    				  
  	  case 3: return String.format("%.2f", t.getWeight() * 100) + " %";
  	  case 4: return t.isComplete();
  	  }
  	  return null;
    }

    // helper class for setting up table with course names
    private class CourseTask implements Comparable<CourseTask>{
    	String courseName;
    	Task task;
    	public CourseTask(String courseName, Task task){
    		this.courseName = courseName;
    		this.task = task;
    	}

		@Override
		public int compareTo(CourseTask o) {
			return this.task.compareTo(o.task);
		}
  	
    }
}
