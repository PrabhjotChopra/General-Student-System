package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;

import javax.swing.*;
import java.awt.*;

import java.util.Enumeration;
import java.util.Hashtable;
import com.formdev.flatlaf.FlatDarkLaf;

public class ClassCourse extends Course {
	private int courseNum;
	private LinkedList<Student> students = new LinkedList<Student>();
	private Hashtable<Assessment, Hashtable<Student, Double>> grades = new  Hashtable<Assessment, Hashtable<Student, Double>>();
	private Hashtable<Student, Double> midterms = new Hashtable<Student, Double>();
	private Hashtable<Student, Double> finals = new Hashtable<Student, Double>();
	private Hashtable<Student, Double> current = new Hashtable<Student, Double>();
	private Hashtable<Student, Attend[]> attendance = new Hashtable<Student, Attend[]>();
	private Teacher prof;
	private String room;
	private int period;
	private double totalWeight=0.0;
	private int courseDay;
	
	private JButton baseDisplay;
	private Hashtable<Student, JPanel> longUI = new Hashtable<Student, JPanel>();
	private Hashtable<Student, JPanel> fullUI = new Hashtable<Student, JPanel>();
	private Hashtable<Student, JPanel> shortUI = new Hashtable<Student, JPanel>();
	
	private JTextArea dashboard;
	private JPanel tab;
	
	JComboBox<String> dayChoice;
	JButton submitter;
	
	// various menu jbuttons
	private JButton main;
	private JButton daily;
	private JButton marks;
	private JButton overallAtt;
	private JButton addAss;
	private JButton removeAss;
	private JButton editAss;
	
	
	
	public ClassCourse(int n, Teacher t, String s, int p, Course c) {
		super(c);
		courseNum=n;
		prof=t;
		room=s;
		period=p;
		courseDay = 0;
		FlatDarkLaf.setup();
		
		baseDisplay = new JButton(this.getCode() + " - 0" + courseNum);
		
		baseDisplay.setSize(School.rect.width - 200, 70);
		baseDisplay.setFont(new Font("Arial", 1, 100));
		baseDisplay.setAlignmentY(0);
		baseDisplay.addActionListener(new School());
		
		tab = new JPanel();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		
		tab.setBounds(100, 255, School.rect.width - 200, School.rect.height - 355);
		tab.setBorder(BorderFactory.createLineBorder(Color.black));
		
		dashboard = new JTextArea("Name: " + this.getName() + "\nCode: " + this.getCode() + "- 0" + courseNum + "\nType: " + this.getType());
		dashboard.setFont(School.buttonFont);
		dashboard.setEditable(false);
		tab.add(dashboard);
		
		main = new JButton("Dashboard");
		main.setBounds(100, 180, 200, 75);
		main.setFont(School.buttonFont);
		main.addActionListener(new School());
		
		daily = new JButton("Daily Attendance");
		daily.setBounds(350, 170, 200, 75);
		daily.setFont(School.buttonFont);
		daily.addActionListener(new School());
		
		
		String[] days;
		if(period<5) {
			days = new String[School.sem1days];
		}
		else {
			days  = new String[School.sem2days];
		}
		
		for(int i=0;i<days.length; i++) {
			days[i] = String.valueOf(i+1);
		}
		dayChoice = new JComboBox<String>(days);
		dayChoice.setBounds(600, 170, 200, 75);
		dayChoice.setFont(School.buttonFont);
		
		submitter = new JButton("Select day");
		submitter.addActionListener(new School());
		submitter.setBounds(900, 170, 200, 50);
		submitter.setFont(School.buttonFont);
	}
	
	
	public void addStudent(Student s) {
		students.add(s);
		Attend[] temp;
		
		if (period<5) {
			temp = new Attend[School.sem1days];
		}
		else {
			temp = new Attend[School.sem2days];
		}
		
		
		for(int i=0;i<courseDay;i++) {
			temp[i]=(new Attend(false,false,0,""));
		}
		attendance.put(s, temp);
		
		// add to grades 
		// add to all gui hashtables (remember to initialize each value)
		
		
		// add all the ui's that aren't student fields
		
		
		
	}
	public void removeStudent(Student s) {
		students.remove(s);
		attendance.remove(s);
		
		// remove from attendance
		// remove from grades 
	}
	public void setGrade(Student s, Assessment assign, Double marksEarned){
		Hashtable<Student, Double> marks = grades.get(assign);
		marks.replace(s, marksEarned/ (double) assign.getTotalMarks());
		
		current.replace(s, gradeCalc(s));
		grades.replace(assign, marks);
	}
	
	
	public void addAssessment(String name, double weight, int totalMarks) {
		Assessment assess = new Assessment(name, weight, totalMarks);
		Hashtable<Student, Double> marks = new Hashtable<Student, Double>();
		totalWeight+=weight;
		for(Student s: getStudents()) {
			marks.put(s, 0.0);
		}
		
		grades.put(assess, marks);
	}
	
	public void removeAssessment(Assessment assess) {
		totalWeight-= assess.getWeight();
		grades.remove(assess);
		
		Enumeration<Student> e = current.keys();
		while (e.hasMoreElements()) {
			Student kid = e.nextElement();
			current.replace(kid, gradeCalc(kid));	
		}
		
	}
	
	public double gradeCalc(Student s) {
		double grade= 0.0;
		Enumeration<Assessment> e = grades.keys();
		while (e.hasMoreElements()) {
			Assessment assign = e.nextElement();
			double mark = grades.get(assign).get(s) / (double) assign.getTotalMarks(); // student marks/total marks for that assessment
			double weight = assign.getWeight() / totalWeight; // how much that assessment is currently worth
			grade += mark*weight;
			
		}
		return grade;
	}
	
	
	public void addDailys() {
		tab.removeAll();
		for(int i=0; i<getStudents().size();i++) {
			tab.add(getStudents().get(i).getDaily());
		}
		tab.revalidate();
		tab.repaint();
		
		if (daily.getY()==170) {
			daily.setLocation(daily.getX(), 180);
			main.setLocation(main.getX(), 170);
			
			
			//not initialized so commented out for now
			//marks.setLocation(marks.getX(), 170);
			//overallAtt.setLocation(overallAtt.getX(), 170);
			
		}
	}
	public void goDash() {
		tab.removeAll();
		tab.add(dashboard);
		tab.revalidate();
		tab.repaint();
		if (main.getY()==170) {
			daily.setLocation(daily.getX(), 170);
			main.setLocation(main.getX(), 180);
			
			
			//not initialized so commented out for now
			//marks.setLocation(marks.getX(), 170);
			//overallAtt.setLocation(overallAtt.getX(), 170);
			
		}
		
	}
	public void setPresent(Student s) {
		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] =  new Attend(true, false, 0, "");
		attendance.replace(s, thisAttendance);
		s.gPresent();
		
		tab.revalidate();
		tab.repaint();
		
		
	}
	public void setAbsent(Student s) {
		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] =  new Attend(false, false, 0, s.getAbsentReason().getText());
		attendance.replace(s, thisAttendance);
		s.rAbsent();
		
		tab.revalidate();
		tab.repaint();
	}
	public void setLate(Student s) {
		Attend[] thisAttendance = attendance.get(s);
		
		// todo calculate mins late from entered time
		
		
		int mins=0;
		try {
			mins = Integer.parseInt(s.getMinsLate().getText());
		}
		catch (NumberFormatException e){}
		
		thisAttendance[courseDay] = new Attend(true, true, mins, "");
		attendance.replace(s, thisAttendance);
		s.bLate();
		
		tab.revalidate();
		tab.repaint();
		
		
	}
	
	public void setLate(Student s, int time) {
		s.setMinsLate(String.valueOf(time));
		s.setAbsentReason("");
		s.bLate();
		
		tab.revalidate();
		tab.repaint();
	}
	
	public void setAbsent(Student s, String reason) {
		s.setMinsLate("");
		s.setAbsentReason(reason);
		s.rAbsent();
		
		tab.revalidate();
		tab.repaint();
	}
	public void setNull(Student s) {
		s.setMinsLate("Time arrived");
		s.setAbsentReason("Reason for absence");
		s.setNull();
		
		tab.revalidate();
		tab.repaint();
	}
	
	public void changeAttDay() {
		// change this into date conversions later
		int day = Integer.parseInt((String) dayChoice.getSelectedItem()) - 1;
		
		for (int i=0;i<students.size();i++) {
			Attend temp = attendance.get(students.get(i))[day];
			
			if (temp!=null) {
				if (temp.getPresent()) {
					if(attendance.get(students.get(i))[day].getLate()) {
						setLate(students.get(i), temp.getMinutesLate());
					}
					else {
						setPresent(students.get(i));
					}
					
					}
					else {
						setAbsent(students.get(i), temp.getReason());
					}
			}
			else {
				setNull(students.get(i));
			}
			
		}
	}
	
	public int getPeriod() {
		return period;
	}

	public JButton getBaseDisplay() {
		return baseDisplay;
	}


	public JPanel getTab() {
		return tab;
	}
	public JButton getMain() {
		return main;
	}
	public JButton getDaily() {
		return daily;
	}


	public LinkedList<Student> getStudents() {
		return students;
	}


	public JComboBox<String> getDayChoice() {
		return dayChoice;
	}


	public JButton getSubmitter() {
		return submitter;
	}
}
