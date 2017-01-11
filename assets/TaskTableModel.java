package assets;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4738006637709993449L;
	ArrayList<Task> taskList;
	String[] columnNames = {"Task",
			"Due Date",
			"Grade",
			"Weight",
			"Submitted"};
	
	public TaskTableModel(ArrayList<Task> taskList){
		this.taskList = taskList;
	}
	
	public int getRowCount() { 
  	  return taskList.size();
    }
	
    public int getColumnCount() {
  	  return columnNames.length;
    }
    
    public String getColumnName(int index){
  	  return columnNames[index];
    }
    
    public Class<?> getColumnClass(int columnIndex){
  	  switch (columnIndex){
  		case 1:
  	  		return Date.class;
  	  	case 4:
  	  		return Boolean.class;
  	  	default:
  	  		return String.class;
  	  }
    }
    
    public boolean isCellEditable(int row, int column){
  	  return true;
    }
    
    // updates to table model need to be passed to task model
    public void setValueAt(Object value, int row, int col) {
  	  System.out.println("Setting value");
  	  Task t = taskList.get(row);
  	  switch (col){
      	  case 0: 
      		  t.setName((String)value);
      		  break;
      	  case 1: 
      		  t.setDueDate((Date)value); // fix later
      		  break;
      	  case 2: 
      		  // percentage view to fractional
      		  Double grade = Double.parseDouble(((String)value).replace('%', '\0'));
      		  t.setGrade(grade * 0.01);
      		  break;
      	  case 3: 
      		  Double weight = Double.parseDouble(((String)value).replace('%', '\0'));
      		  t.setWeight(weight * 0.01);
      		  break;
      	  case 4: 
      		  t.setComplete((Boolean)value);
      		  break;
  	  }
        fireTableCellUpdated(row, col);
    }
    
    // values of model
    public Object getValueAt(int row, int col) { 
  	  Task t = taskList.get(row);
  	  switch (col){
  	  case 0: return t.getName();
  	  case 1: return t.getDueDate();
  	  case 2: return String.format("%.2f", t.getGrade() * 100) + " %"; // fraction to percentage view
  	  case 3: return String.format("%.2f", t.getWeight() * 100) + " %";
  	  case 4: return t.isComplete();
  	  }
  	  return null;
    }
    
    public ArrayList<Task> getTaskList(){
    	return taskList;
    }
}
