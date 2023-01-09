package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;

public class Course {
	private String name;
	private String code;
	private String type;
	private LinkedList<String> usableRooms;
	private int priority;
	private int maxKids;
	private int minKids;
	private int numClasses;
	
	public Course(String name, String code, String type, LinkedList<String> rooms, int priority, int kids, int numclass, int minKids) {
		this.name = name;
		this.code = code;
		this.type = type;
		usableRooms = rooms;
		this.priority = priority;
		maxKids = kids;
		numClasses = numclass;
		this.minKids = minKids;
	}
	
	public Course(Course c) { // super constructor for ClassCourse object
		this.name = c.name;
		this.code = c.code;
		this.type = c.type;
		this.usableRooms = new LinkedList<String>(c.usableRooms);
		this.priority = c.priority;
	}
	
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public void setNumClasses(int n) {
		numClasses = n;
	}
	public int getMaxKids() {
		return maxKids;
		
	}
	public int getMinKids() {
		return minKids;
	}
	public int getNumClasses() {
		return numClasses;
	}
	public LinkedList<String> getUsableRooms(){
		return usableRooms;
	}
}
