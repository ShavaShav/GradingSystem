package assets;

import java.util.Date;
import java.util.Observable;

public class Task extends Observable{
	protected String name;
	private TaskType type;
	private int number;
	private Date dueDate;
	private double grade;
	private double weight;
	private boolean isComplete;
	
	
	public Task(int type, int number) throws Exception{
		try{
			this.type = new TaskType(type);
			this.number = number;
			dueDate = null;
			grade = 0.0;
			weight = 0.0;
			isComplete=false;
		} catch (Exception e) {
			throw new Exception("Creating Task: " + e.getMessage());
		}
	}
	
	public void setDueDate(Date date){
		this.dueDate = date;
	}
	
	public void setGrade(double grade){
		this.grade = grade;
		isComplete = true;
		setChanged();
		notifyObservers();	// let course know of change
	}
	
	public void setWeight(double weight){
		this.weight = weight;
		setChanged();
		notifyObservers();	// let course know of change
	}
	
	public int getType(){ return type.getType(); }
	public int getNumber(){ return number; }
	public Date getDueDate(){ return dueDate; }
	public double getGrade(){ return grade; }
	public double getWeight(){ return weight; }
	public boolean isComplete(){ return isComplete; }
	
	public boolean before(Task task){
		return this.dueDate.before(task.dueDate);	
	}
}
