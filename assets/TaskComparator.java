package assets;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task>{
	protected final static int NAME  = 1;
	protected final static int DUEDATE= 2;
	protected final static int GRADE = 3;
	protected final static int  WEIGHT = 4;
	protected final static int  SUBMITTED = 5;
	
	private boolean ascending;
	private int attribute; 
	
	public TaskComparator(boolean ascending, int attribute){
		this.ascending = ascending;
		this.attribute = attribute;
	}
	
	@Override
	public int compare(Task t1, Task t2) {
		int result = 0;
		
		switch (attribute){
			case NAME:
				result = t1.getName().compareTo(t2.getName());
				break;
			case DUEDATE:
				result = t1.getDueDate().compareTo(t2.getDueDate());
				break;
			case GRADE:
				result = Double.compare(t1.getGrade(), t2.getGrade());
				break;
			case WEIGHT:
				result = Double.compare(t1.getWeight(), t2.getWeight());
				break;
			case SUBMITTED:
				result = Boolean.compare(t1.isComplete(), t2.isComplete());
				break;
			default: System.out.println("Bad compare");
		}
		if (!ascending)
			result = -(result);
		return result;
	}
}
