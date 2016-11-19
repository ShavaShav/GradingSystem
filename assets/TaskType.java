package assets;

public class TaskType {
	public static int ASSIGNMENT = 1;
	public static int LAB = 2;
	public static int PROJECT = 3;
	public static int QUIZ = 4;
	public static int TEST = 5;
	public static int EXAM = 6;
	public static int OTHER = 7;
	
	private int type;
	
	public TaskType(int type) throws Exception{
		if (type > 0 && type < 8)
			this.type = type;
		else 
			throw new Exception("Invalid Task Type");
	}
	
	public void setType(int type) throws Exception{
		if (type > 0 && type < 8)
			this.type = type;
		else 
			throw new Exception("Invalid Task Type");
	}
	
	public int getType(){
		return type;
	}
}
