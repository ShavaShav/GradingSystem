package assets;

import java.util.ArrayList;
import java.util.Date;
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
	private boolean isMajor;
	private ArrayList<Task> taskList;
	
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
	
	// Accessors
	public String getName(){ return name; }
	public String getCode(){ return code; }
	public int getCreditHours(){ return creditHours; }
	public Semester getSemester(){ return semester; }
	public String getClassRoom(){ return classRoom; }	
	public double getGrade(){ return grade; }
	public Task getTask(int index){ return taskList.get(index); }
	public boolean isComplete(){ return isComplete; }
	public int getNumTasks(){ return taskList.size(); }
	public boolean isMajor(){ return isMajor; }
	public ListIterator<Task> getIterator(){ return taskList.listIterator(); }
	public ArrayList<Task> getTaskList(){ return taskList; }
	
	// Mutators
	public void setName(String name){ this.name = name; notifyChanged(); }
	public void setCode(String code){ this.code = code; notifyChanged(); }
	public void setCredits(int creditHours){ this.creditHours = creditHours; notifyChanged(); }
	public void setSemester(String season, int year){ 
		try {
			this.semester = new Semester(season, year);
			 notifyChanged();
		} catch (Exception e) {
			e.printStackTrace();
		} }
	public void setClassRoom(String classRoom){ this.classRoom = classRoom;  notifyChanged();}
	public void setGrade(double grade){ this.grade = grade;  notifyChanged();}
	public void setComplete(boolean isComplete){ this.isComplete = isComplete;  notifyChanged();}
	public void setMajor(boolean isMajor){ this.isMajor = isMajor; notifyChanged(); }
	
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
	
	public boolean removeTask(int index){
		return removeTask(taskList.get(index));
	}
	
	// returns list of tasks with due dates before "limit" that are not yet complete
	public ArrayList<Task> getUpcomingDeadlines(Date limit){
		ArrayList<Task> deadlines = new ArrayList<Task>();
		for (Task t: taskList){
			if (!t.isComplete() && t.getDueDate().before(limit))
				deadlines.add(t);
		}
		return deadlines;
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
			if (task.isGraded()){
				totalWeight += task.getWeight();
				totalGrade += task.getGrade() * task.getWeight();				
			}
		}
		if (totalWeight >= 1){ // if weight exceeds, grade counts for more
			totalWeight = 1;
		}
		// if valid weight and grade, return result, else 0.00
		return totalWeight > 0 && totalGrade > 0 ? totalGrade/totalWeight : 0.00;
	}
	
	// when task model is updated, update course variables and notify observers (like course view)
	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("Course model has received update");
		grade = calculateGrade();
		// let program and course view know of change
		notifyChanged();
	}
	
	// Let view know of change to course model
	public void notifyChanged(){
		System.out.println("Course model changed.");
		this.setChanged();
		this.notifyObservers();
	}
	
	public String toString(){
		return code + " " + name + " (" + semester + ") in room " + classRoom +
				" --> " + grade + "% (" + isComplete + ")";
	}
}
