package assets;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

public class Program extends Observable implements Observer,  java.io.Serializable {
	private static final long serialVersionUID = 6356005602353142426L;
	private String name;
	private int credits;
	private ArrayList<Course> courseList;
	private int numCourses;
	
	public Program(){
		this.courseList = new ArrayList<Course>();
		this.name = "";
		this.numCourses = 0;
		this.credits = 0;
		// will need to update view if program is changed
		notifyChanged();
	}

	public Program(String name, int credits){
		this.name = name;
		this.credits = credits;
		this.courseList = new ArrayList<Course>();
		notifyChanged();
	}
	
	public boolean addCourse(Course c){
		try { 
			c.addObserver(this); // track changes to new course
			courseList.add(c);
			numCourses++;
			notifyChanged();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean removeCourse(Course c){
		try { 
			c.deleteObservers();
			courseList.remove(c);
			numCourses--;
			notifyChanged();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setName(String name){ 
		this.name = name; 
		notifyChanged();
	}
	
	public void setCredits(int credits){
		this.credits = credits; 
		notifyChanged();
	}
	
	public String getName(){ return name; }
	public int getCredits(){ return credits; }
	public int getNumCourses(){ return numCourses; }
	
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
	
	public ListIterator<Course> getIterator(){
		return courseList.listIterator();
	}

	// When a course/task changes, notify the program view
	@Override
	public void update(Observable o, Object arg) {
		//System.out.println("Program model has received update");
		notifyChanged();
	}

	// Let view know of change to program model
	public void notifyChanged(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public String toString(){
		return name + "-" + credits;
	}
	
	public String obsString(){
		String progStr = this.toString() + " (obs: " + this.countObservers() + ")\n";
		for (Course c : courseList){
			progStr += "\t" + c.toString() + " (obs: " + c.countObservers() + ")\n";
			for (Task t : c.taskList){
				progStr += "\t\t" + t.toString() + " (obs: " + t.countObservers() + ")\n";
			}
		}
		return progStr;
	}
}