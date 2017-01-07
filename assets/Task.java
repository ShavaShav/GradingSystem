package assets;

import java.util.Date;
import java.util.Observable;

public class Task extends Observable implements java.io.Serializable{
	private static final long serialVersionUID = 8581517057344177875L;
	protected String name;
	private Date dueDate;
	private double grade;
	private double weight;
	private boolean isComplete;
	
	public Task(String name){
		this.name = name;
		dueDate = new Date();
		grade = 0.0;
		weight = 0.0;
		isComplete=false;
	}
	
	public void setDueDate(Date date){
		this.dueDate = date;
		notifyChanged();
	}
	
	public void setGrade(double grade){
		this.grade = grade;
		isComplete = true;
		notifyChanged();
	}
	
	public void setWeight(double weight){
		this.weight = weight;
		notifyChanged();
	}

	
	public String getName(){ return name; }
	public Date getDueDate(){ return dueDate; }
	public double getGrade(){ return grade; }
	public double getWeight(){ return weight; }
	public boolean isComplete(){ return isComplete; }
	
	public boolean before(Task task){
		return this.dueDate.before(task.dueDate);	
	}
	
	// Let course know of change to task model
	public void notifyChanged(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public String toString(){
		return name + " " + grade + "% (" + weight + "%) " + isComplete; 
	}
}
