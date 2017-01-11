package assets;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class CourseTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 7969005972953219033L;
	private ArrayList<Course> courseList;
	private String[] columnNames = {"Course #",
			"Name",
			"Semester",
			"Major",
			"Grade",
			"Credits",
			"Complete?"};
	public CourseTableModel(ArrayList<Course> courseList){
		super();
		this.courseList = courseList;
	      /*
	      addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel)e.getSource();
				String columnName = model.getColumnName(column);
				Object data = model.getValueAt(row, column);
				System.out.println(columnName);
			}  	  
	      });
	      */
	}
	public int getRowCount() { 
		return courseList.size();
	}
	public int getColumnCount() {
		return columnNames.length;
	}
	public String getColumnName(int index){
		return columnNames[index];
	}
	public Class<?> getColumnClass(int columnIndex){
		switch (columnIndex){
		case 2: // semester
			return  Semester.class;
		case 3: // complete?
			return  Boolean.class;
		case 5:  // credits
			return Integer.class;
		case 6: // complete?
			return  Boolean.class;
		default:
			return String.class;
		}
	}
	public boolean isCellEditable(int row, int column){
		return true;
	}
	public void setValueAt(Object value, int row, int col) {
		System.out.println("Setting value");
		Course c = courseList.get(row);
		switch (col){
		case 0: 
			c.setCode((String)value);
			break;
		case 1: 
			c.setName((String)value);
			break;
		case 2: 
			String s = (String) value;
			String[] sem = s.split(" ");
			c.setSemester((String)sem[0], Integer.parseInt(sem[1]));
			break;
		case 3: 
			c.setMajor((Boolean)value);
			break;
		case 4: 
			// percentage view to fractional
			Double grade = Double.parseDouble(((String)value).replace('%', '\0'));
			c.setGrade(grade * 0.01);
			break;
		case 5: 
			c.setCredits((Integer)value);
			break;
		case 6: 
			c.setComplete((Boolean)value);
			break;
		}
		fireTableCellUpdated(row, col);
	}
	public Object getValueAt(int row, int col) { 
		Course c = courseList.get(row);
		switch (col){
			case 0: return c.getCode();
			case 1: return c.getName();
			case 2: return c.getSemester();
			case 3: return c.isMajor();
			case 4: return String.format("%.2f", c.getGrade() * 100) + " %"; // fractional to percentage view
			case 5: return c.getCreditHours();
			case 6: return c.isComplete();
		}
		return null;
	}
	public ArrayList<Course> getCourseList(){
		return courseList;
	}
}
