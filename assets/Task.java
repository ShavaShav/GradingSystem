package assets;

import java.util.Date;
import java.util.Observable;

public class Task extends Observable implements Comparable<Task>, java.io.Serializable{
	private static final long serialVersionUID = 8581517057344177875L;
	protected String name;
	private Date dueDate;
	private double grade;
	private double weight;
	private boolean isComplete; // Task has been submitted
	private boolean isGraded; // Task has been graded
	
	public Task(String name){
		this.name = name;
		dueDate = new Date();
		grade = 0.0;
		weight = 0.0;
		isComplete=false;
		isGraded=false;
	}
	
	public void setName(String name){
		this.name = name;
		notifyChanged();
	}
	
	public void setDueDate(Date date){
		this.dueDate = date;
		notifyChanged();
	}
	
	public void setGrade(double grade){
		this.grade = grade;
		isGraded = true;
		isComplete = true;
		notifyChanged();
	}
	
	public void setWeight(double weight){
		this.weight = weight;
		notifyChanged();
	}
	
	public void setComplete(boolean isComplete){
		this.isComplete = isComplete;
		notifyChanged();
	}
	
	public String getName(){ return name; }
	public Date getDueDate(){ return dueDate; }
	public double getGrade(){ return grade; }
	public double getWeight(){ return weight; }
	public boolean isComplete(){ return isComplete; }
	public boolean isGraded(){ return isGraded; }
	
	// Let course know of change to task model
	public void notifyChanged(){
		System.out.println("Task model changed.");
		this.setChanged();
		this.notifyObservers();
	}
	
	public String toString(){
		return name + " " + grade + "% (" + weight + "%) " + isComplete; 
	}

	@Override
	public int compareTo(Task task) {
		if (this.dueDate.before(task.dueDate))
			return -1;
		else if (this.dueDate.after(task.dueDate))
			return 1;
		else
			return 0;
	}
}
