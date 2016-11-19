package assets;

import java.util.ArrayList;

public class Program {
	private String name;
	private int credits;
	private ArrayList<Course> courseList;

	public Program(String name, int credits){
		this.name = name;
		this.credits = credits;
		this.courseList = new ArrayList<Course>();
	}

	public double getCreditsEarned(){
		int creditsEarned = 0;
		for (Course course : courseList){
			if (course.isComplete())
				creditsEarned += course.getCreditHours();
		}
		return creditsEarned;
	}
	
	public double getPercentComplete(){
		double percent = getCreditsEarned()/credits;
		if (percent > 1.0)
			return 1.0;
		else
			return percent;
	}
	
	public ArrayList<Course> getCoursesBySemester(Semester semester){
		ArrayList<Course> semesterCourses =  new ArrayList<Course>();
		for (Course course : courseList){
			if (course.getSemester().equals(semester))
				semesterCourses.add(course);
		}
		return semesterCourses;
	}
}