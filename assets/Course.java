package assets;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Course implements Observer {
	private String name;
	private String code;
	private int creditHours;
	private Semester semester;
	private String classRoom;
	private double grade;
	private boolean isComplete;
	ArrayList<Task> taskList;
	
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
		task.addObserver(this);
		return taskList.add(task);
	}
	
	// true if removed task
	public boolean removeTask(Task task){
		task.deleteObservers();
		return taskList.remove(task);
		
	}
	
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

	@Override
	public void update(Observable arg0, Object arg1) {
		grade = calculateGrade();
	}
	
}
