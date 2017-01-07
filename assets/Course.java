package assets;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

public class Course extends Observable implements Observer, java.io.Serializable{
	private static final long serialVersionUID = 4653190257966057382L;
	private String name;
	private String code;
	private int creditHours;
	private Semester semester;
	private String classRoom;
	private double grade;
	private boolean isComplete;
	ArrayList<Task> taskList;
	
	public String toString(){
		return code + " " + name + " (" + semester + ") in room " + classRoom +
				" --> " + grade + "% (" + isComplete + ")";
	}
	
	public Course(String name, String code, int creditHours, Semester semester, String classRoom){
		this.name = name;
		this.code = code;
		this.creditHours = creditHours;
		this.semester = semester;
		this.classRoom = classRoom;
		this.grade = 0.0;
		this.taskList = new ArrayList<Task>();
		this.isComplete = false;
	}
	
	public String getName(){ return name; }
	public String getCode(){ return code; }
	public int getCreditHours(){ return creditHours; }
	public Semester getSemester(){ return semester; }
	public String getClassRoom(){ return classRoom; }	
	public double getGrade(){ return grade; }
	public boolean isComplete(){ return isComplete; }
	
	// true if added task
	public boolean addTask(Task task){
		try {
			task.addObserver(this);
			taskList.add(task);
			notifyChanged();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// true if removed task
	public boolean removeTask(Task task){
		try {
			task.deleteObservers();
			taskList.remove(task);
			notifyChanged();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	// TODO returns NaN currently when no tasks complete
	// to be invoked when task updates grade
	private double calculateGrade(){
		double totalWeight = 0.0;
		double totalGrade = 0.0;
		for (Task task : taskList){
			if (task.isComplete()){
				totalWeight += task.getWeight();
				totalGrade += task.getGrade();				
			}
		}
		if (totalWeight >= 100){ // if weight exceeds, grade counts for more
			totalWeight = 100;
			isComplete = true;
		}
		return totalGrade/totalWeight;
	}

	public int getNumTasks(){
		return taskList.size();
	}
	
	public ListIterator<Task> getIterator(){
		return taskList.listIterator();
	}

	// when task model is updated, update course variables and notify observers (like course view)
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Course model has received update");
		grade = calculateGrade();
		// let program and course view know of change
		notifyChanged();
	}
	
	// Let view know of change to program model
	public void notifyChanged(){
		this.setChanged();
		this.notifyObservers();
	}
}
