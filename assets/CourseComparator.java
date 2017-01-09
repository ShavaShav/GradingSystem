package assets;

import java.util.Comparator;

public class CourseComparator implements Comparator<Course>{
	protected final static int CODE  = 1;
	protected final static int NAME = 2;
	protected final static int SEMESTER = 3;
	protected final static int  MAJOR = 4;
	protected final static int  GRADE = 5;
	protected final static int  CREDITS = 6;
	protected final static int  COMPLETE = 7;
	
	private boolean ascending;
	private int attribute; 
	
	public CourseComparator(boolean ascending, int attribute){
		this.ascending = ascending;
		this.attribute = attribute;
	}

	@Override
	public int compare(Course c1, Course c2) {
		int result = 0;
		
		switch (attribute){
			case CODE:
				result = c1.getCode().compareTo(c2.getCode());
				break;
			case NAME:
				result = c1.getName().compareTo(c2.getName());
				break;
			case SEMESTER:
				result = c1.getSemester().compareTo(c2.getSemester());
				break;
			case MAJOR:
				result = Boolean.compare(c1.isMajor(),  c2.isMajor());
				break;
			case GRADE:
				result = Double.compare(c1.getGrade(), c2.getGrade());
				break;
			case CREDITS:
				result = c1.getCreditHours() - c2.getCreditHours();
				break;
			case COMPLETE:
				result = Boolean.compare(c1.isComplete(),  c2.isComplete());
				break;
		}
		if (!ascending)
			result = -(result);
		return result;
	}

}
