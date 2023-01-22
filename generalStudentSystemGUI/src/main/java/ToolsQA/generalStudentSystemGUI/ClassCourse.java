package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.Hashtable;
import com.formdev.flatlaf.FlatDarkLaf;

public class ClassCourse extends Course {
	private int courseNum;
	private LinkedList<Student> students = new LinkedList<Student>();
	private LinkedList<Assessment> assignments = new LinkedList<Assessment>();
	private Hashtable<Assessment, Hashtable<Student, Double>> grades = new Hashtable<Assessment, Hashtable<Student, Double>>();
	private Hashtable<Student, Double> midterms = new Hashtable<Student, Double>();
	
	private Hashtable<Student, Double> current = new Hashtable<Student, Double>();
	private Hashtable<Student, Attend[]> attendance = new Hashtable<Student, Attend[]>();
	private Teacher prof;
	private String room;
	private int period;
	private double totalWeight = 0.0;
	private int courseDay;
	private LocalTime start;
	private int days;

	private int currKid;
	private JButton baseDisplay;

	

	private JTextArea dashboard;
	private JPanel tab;
	private JScrollPane pane;

	private JComboBox<String> dayChoice;
	private JTextField midterming;
	private LinkedList<JTextField> assMarks;
	
	// various menu jbuttons
	private JButton main;
	private JButton daily;
	private JButton marks;
	private JButton overallAtt;
	private JButton subAtt;
	
	private JButton marksDash;
	private JButton assDash;
	
	private JButton addAss;
	private JButton removeAss;
	
	private JButton goBack;
	

	public ClassCourse(int n, Teacher t, String s, int p, Course c) {
		super(c);
		courseNum = n;
		prof = t;
		room = s;
		period = p;
		courseDay = 0;

		if (period > 4) {
			start = School.periodStarts[period - 5];
			days = School.sem1days;
		} else {
			start = School.periodStarts[period - 1];
			days = School.sem2days;
		}

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

		dashboard = new JTextArea("Name: " + this.getName() + "\nCode: " + this.getCode() + "- 0" + courseNum
				+ "\nType: " + this.getType() + "\nRoom: " + room + "\nPeriod: " + period);
		dashboard.setFont(School.buttonFont);
		dashboard.setEditable(false);
		tab.add(dashboard);

		main = new JButton("Dashboard");
		main.setBounds(100, 180, 200, 75);
		main.setFont(School.buttonFont);
		main.addActionListener(new School());

		daily = new JButton("Daily Attendance");
		daily.setBounds(325, 170, 200, 75);
		daily.setFont(School.buttonFont);
		daily.addActionListener(new School());

		overallAtt = new JButton("Overall Attendance");
		overallAtt.setBounds(545, 170, 250, 75);
		overallAtt.setFont(School.buttonFont);
		overallAtt.addActionListener(new School());

		String[] dayChoices = new String[days];

		for (int i = 0; i < days; i++) {
			dayChoices[i] = "Day " + String.valueOf(i + 1);
		}
		dayChoice = new JComboBox<String>(dayChoices);
		dayChoice.setBounds(805, 170, 150, 75);
		dayChoice.setFont(School.buttonFont);
		dayChoice.setActionCommand("Day Choice");
		dayChoice.addActionListener(new School());

		subAtt = new JButton("Submit Attendance");
		subAtt.setBounds(965, 170, 300, 75);
		subAtt.setFont(School.buttonFont);
		subAtt.addActionListener(new School());
		
		marks = new JButton("Grades and Assessments");
		marks.setActionCommand("dmarks");
		marks.setBounds(965, 170, 280, 75);
		marks.setFont(School.buttonFont);
		marks.addActionListener(new School());
		
		
		
		marksDash = new JButton("Student marks");
		marksDash.setBounds(100, 180, 200, 75);
		marksDash.setFont(School.buttonFont);
		marksDash.addActionListener(new School());
		marksDash.setActionCommand("marksDash");

		assDash = new JButton("Assessments");
		assDash.setBounds(320, 170, 200, 75);
		assDash.setFont(School.buttonFont);
		assDash.addActionListener(new School());
		assDash.setActionCommand("assDash");

		addAss = new JButton("Add assessment");
		addAss.setBounds(540, 170, 200, 75);
		addAss.setFont(School.buttonFont);
		addAss.addActionListener(new School());
		addAss.setActionCommand("addAss");
		
		removeAss = new JButton("Remove assessment");
		removeAss.setBounds(760, 170, 240, 75);
		removeAss.setFont(School.buttonFont);
		removeAss.addActionListener(new School());
		removeAss.setActionCommand("removeAss");
		
		goBack = new JButton("Back to dashboard");
		goBack.setBounds(1020, 170, 220, 75);
		goBack.setFont(School.buttonFont);
		goBack.addActionListener(new School());
		goBack.setActionCommand("back to dash");
		
	}

	public void addStudent(Student s) {
		students.add(s);
		Attend[] temp;

		if (period < 5) {
			temp = new Attend[School.sem1days];
		} else {
			temp = new Attend[School.sem2days];
		}

		for (int i = 0; i < courseDay; i++) {
			temp[i] = (new Attend(false, false, 0, "Not in course"));
		}

		attendance.put(s, temp);
		current.put(s, 0.0);
		midterms.put(s, 0.0);
		
		
		for(int i=0;i<assignments.size();i++) { 
			// adds the new student to the assessment tables if they joined late
			Hashtable<Student, Double> thisGrade = grades.get(assignments.get(i));
			thisGrade.put(s, 0.0);
			grades.replace(assignments.get(i), thisGrade);
		}
		

	

	}

	public void removeStudent(Student s) {
		students.remove(s);
		attendance.remove(s);
		
		// remove from grades
		// remove from midterms
		// remove from current grades
	}

	public void setGrade(Student s, String assignmentName, Double marksEarned) {
		
		Assessment assign=null;
		
		for(int i=0;i<assignments.size();i++) {
			if (assignments.get(i).getName().trim().equals(assignmentName)) {
				assign = assignments.get(i);
				
			}
		}
		
		
		Hashtable<Student, Double> marks = grades.get(assign);
		marks.replace(s, marksEarned);
		grades.replace(assign, marks);
		
		current.replace(s, gradeCalc(s));
		
	}

	public void addAssessment(String name, double weight, int totalMarks) {
		Assessment assess = new Assessment(name, weight, totalMarks);
		Hashtable<Student, Double> marks = new Hashtable<Student, Double>();
		totalWeight += weight;
		for (Student s : students) {
			marks.put(s, 0.0);
		}

		grades.put(assess, marks);
		assignments.add(assess);
	}

	public void removeAssessment(Assessment assess) {
		totalWeight -= assess.getWeight();
		grades.remove(assess);
		assignments.remove(assess);
		
		Enumeration<Student> e = current.keys();
		while (e.hasMoreElements()) {
			Student kid = e.nextElement();
			current.replace(kid, gradeCalc(kid));
		}
		

	}

	public double gradeCalc(Student s) {
		double grade = 0.0;
		
		double thisWeight = totalWeight;
		for(int i=0;i<assignments.size();i++) {
			if(grades.get(assignments.get(i)).get(s)<0) {
				thisWeight-=assignments.get(i).getWeight();
			}
		}
		
		
		for(int i=0;i<assignments.size();i++) {
			
			double mark = grades.get(assignments.get(i)).get(s) / (double) assignments.get(i).getTotalMarks(); 
			// student marks/total marks for that assessment
			mark*=100;
			if(mark<0) {
				continue;
			}
			
			
			double weight = assignments.get(i).getWeight() / thisWeight; // how much that assessment is currently worth
			
			
			
			grade += mark * weight;

		}
		
		grade = ((double) Math.round(grade*10)) /10; // rounds grade to 1 decimal place
		
		return grade;
	}
	
	
	public void goMarks() {
		
		
		School.dashboard.remove(daily);
		School.dashboard.remove(main);
		School.dashboard.remove(overallAtt);
		School.dashboard.remove(dayChoice);
		School.dashboard.remove(marks);
		
		School.dashboard.add(marksDash);
		School.dashboard.add(assDash);
		School.dashboard.add(addAss);
		School.dashboard.add(removeAss);
		School.dashboard.add(goBack);
		
		studentMarks();
		
		
	}
	
	public void studentMarks() {
		sortKids();

		tab.removeAll();

		Box container = Box.createVerticalBox();

		for (int i = 0; i < students.size(); i++) { 
			JPanel thisStudentMark = new JPanel(new FlowLayout(FlowLayout.LEADING));
			thisStudentMark.setSize(School.rect.width - 200, 20);
			thisStudentMark.setBorder(BorderFactory.createLineBorder(Color.black));

			
			double midgrade = 0.0;
			if(midterms.get(students.get(i))!=null) {
				midgrade = midterms.get(students.get(i));
			}
			
			JTextArea midterm = new JTextArea("Midterm grade: " + midgrade + "%\t");
			midterm.setEditable(false);
			midterm.setFont(Student.studentStandard);
			
			JTextArea currentGrade = new JTextArea("Current Grade: " + gradeCalc(students.get(i)) + "%\t");
			currentGrade.setEditable(false);
			currentGrade.setFont(Student.studentStandard);
			
			
			thisStudentMark.add(Box.createRigidArea(new Dimension(0, 5)));
			thisStudentMark.add(students.get(i).tabbedName());
			thisStudentMark.add(Box.createRigidArea(new Dimension(20, 0)));
			thisStudentMark.add(currentGrade);
			thisStudentMark.add(Box.createRigidArea(new Dimension(20, 0)));
			thisStudentMark.add(midterm);
			thisStudentMark.add(Box.createRigidArea(new Dimension(20, 0)));
			
			JButton b = students.get(i).getStudentMarkB(); 
			b.setText("View Individual");
			b.setFont(Student.studentStandard);
			thisStudentMark.add(b);

			container.add(thisStudentMark);
			container.add(Box.createRigidArea(new Dimension(0, 10)));

		}

		

		pane = new JScrollPane(container);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
			       pane.getVerticalScrollBar().setValue(0);      
			   }
		});
		
		JTextField header = new JTextField(getCode() + "-0" + courseNum + " Grades");
		header.setEditable(false);
		header.setFont(new Font("Arial", 1, 50));
		header.setHorizontalAlignment(JTextField.CENTER);
		
		
		double average = 0.0;
		double stdev = 0.0;
		for(int i=0;i<students.size();i++) {
			average+=gradeCalc(students.get(i));
		}
		average /= students.size();
		
		
		for(int i=0; i<students.size();i++) {
			stdev += Math.pow(gradeCalc(students.get(i)) - average, 2);
		}
		stdev = Math.sqrt(stdev / students.size());
		
		average = ((double)Math.round(average*10)) / 10; // rounds to 1 decimal place
		stdev = ((double)Math.round(stdev*10)) / 10; // rounds to 1 decimal place
		
		JTextArea stats = new JTextArea("Class Average: " + average + "%\t\t Standard Deviation " + stdev+"%");
		stats.setEditable(false);
		stats.setFont(new Font("Arial", 1, 30));
		
		
		
		tab.setLayout(new BoxLayout(tab, BoxLayout.PAGE_AXIS));
	
		
		tab.add(header);
		tab.add(stats);
		tab.add(pane);
		tab.revalidate();
		tab.repaint();

		if (marksDash.getY() == 170) {
			addAss.setLocation(addAss.getX(), 170);
			removeAss.setLocation(removeAss.getX(), 170);	
			assDash.setLocation(assDash.getX(), 170);
			marksDash.setLocation(marksDash.getX(), 180);

		}

		
	}
	
	public void indStudentMarks(int id) {
		
		Student s = new Student();
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getStudentNumber() == id) {
				s = students.get(i);
				currKid = id;

			}
		}

		tab.removeAll();

		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		for (int i = 0; i < students.size(); i++) {
			if (!(s.equals(students.get(i)))) {
				JButton b = students.get(i).getStudentMarkB();
				b.setText(students.get(i).tabbedName().getText());
				b.setFont(new Font("Arial", 1, 25));

				JPanel temp = new JPanel(new GridLayout(1, 1));
				temp.add(b);
				container.add(temp);

				container.add(Box.createRigidArea(new Dimension(0, 10)));
			}
		}
		pane = new JScrollPane(container);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16);

		
		

		JPanel thisKid = new JPanel();
		thisKid.setLayout(new BoxLayout(thisKid, BoxLayout.PAGE_AXIS));
		
		
		JPanel overview = new JPanel();
		overview.setLayout(new BoxLayout(overview, BoxLayout.PAGE_AXIS));
		overview.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// add the stats here
		JTextArea title = new JTextArea(s.getLastName() + ", " + s.getFirstName());
		title.setEditable(false);
		title.setFont(new Font("Arial", 1, 40));
		
		overview.add(title);
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		JTextArea grade = new JTextArea("Current average: " + gradeCalc(s) + "%");
		grade.setEditable(false);
		grade.setFont(Student.studentStandard);
		
		double midterm = 0.0;
		if (midterms.get(s)!=null) {
			midterm = midterms.get(s);
		}
		
		JTextArea thismid = new JTextArea("Midterm mark: ");
		thismid.setEditable(false);
		thismid.setFont(Student.studentStandard);
		thismid.setMaximumSize(new Dimension(80, 25));
		
		midterming = new JTextField(String.valueOf(midterm));
		midterming.setFont(Student.studentStandard);
		midterming.setMaximumSize(new Dimension(midterming.getWidth(), 25));
		
		JTextArea percent = new JTextArea("%");
		percent.setEditable(false);
		percent.setFont(Student.studentStandard);
		
		
		
		
		top.add(thismid);
		top.add(midterming);
		top.add(percent);
		
		overview.add(grade);
		overview.add(top);
		overview.setMaximumSize(new Dimension(School.rect.width - 740, 300));
		
		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.LINE_AXIS));
		info.add(overview);
		
		JButton submitMarks = new JButton("Submit marks");
		submitMarks.setFont(School.buttonFont);
		submitMarks.addActionListener(new School());
		submitMarks.setMaximumSize(new Dimension(200, 300));
		submitMarks.setActionCommand("submitMarks " + s.getStudentNumber());
		
		
		
		info.add(submitMarks);
		
		
		JPanel specificGrades = new JPanel();
		
		specificGrades.setLayout(new BoxLayout(specificGrades, BoxLayout.PAGE_AXIS));
		specificGrades.setBorder(BorderFactory.createLineBorder(Color.black));
		// fill in the specific grades for assignments with the text field here
		JTextArea headers = new JTextArea("Assignment name\t         Student Mark\t      Current Weight");
		headers.setEditable(false);
		headers.setFont(Student.studentStandard);
		specificGrades.add(headers);
		specificGrades.add(Box.createRigidArea(new Dimension(0,20)));
		
		
		assMarks = new LinkedList<JTextField>();
		for(int i=0;i<assignments.size();i++) {
			JPanel thisGrade = new JPanel();
			thisGrade.setLayout(new BoxLayout(thisGrade, BoxLayout.LINE_AXIS));
			
			JTextArea name = new JTextArea(assignments.get(i).getName());
			name.setEditable(false);
			name.setFont(Student.studentStandard);
			name.setPreferredSize(new Dimension(330,30));
			name.setMaximumSize(new Dimension(330,30));
			
			double studentValue = grades.get(assignments.get(i)).get(s);
			
			JTextField studentMark = new JTextField(String.valueOf(studentValue));
			
			studentMark.setFont(Student.studentStandard);
			studentMark.setMaximumSize(new Dimension(80,25));
			assMarks.add(studentMark);
			
			double thismark = ((double) Math.round(studentValue*1000/assignments.get(i).getTotalMarks())) /10;
			double thisweight = ((double) Math.round(assignments.get(i).getWeight() *1000/ totalWeight)) / 10;
			
			if(thismark<0) {
				thismark=0;
			}
			
			JTextArea totalMark = new JTextArea("/" + assignments.get(i).getTotalMarks() + " (" + thismark + 
					"%)");
		
			totalMark.setEditable(false);
			totalMark.setFont(Student.studentStandard);
			totalMark.setPreferredSize(new Dimension(184,25));
			totalMark.setMaximumSize(new Dimension(184,25));
			
			JTextArea weight = new JTextArea(thisweight + "%");
			weight.setEditable(false);
			weight.setFont(Student.studentStandard);
			
			thisGrade.add(name);
			thisGrade.add(studentMark);
			thisGrade.add(totalMark);
			thisGrade.add(weight);
			
			thisGrade.setBorder(BorderFactory.createLineBorder(Color.black));
			specificGrades.add(thisGrade);
			specificGrades.add(Box.createRigidArea(new Dimension(0,10)));
		}
		
		
		
		
		
		
		
		final JScrollPane gradePanel = new JScrollPane(specificGrades);

		gradePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gradePanel.getVerticalScrollBar().setUnitIncrement(10);
		
		
		
		gradePanel.setMaximumSize(new Dimension(School.rect.width - 540, 800));
		
		// adjust the max sizes so the overview is small and the assessment stuff is big
		
	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
				   gradePanel.getVerticalScrollBar().setValue(0);
			   }
		});
		
		
		thisKid.add(info);
		thisKid.add(Box.createRigidArea(new Dimension(0, 20)));
		thisKid.add(gradePanel);

		tab.setLayout(new BoxLayout(tab, BoxLayout.LINE_AXIS));
		tab.add(pane);
		tab.add(Box.createRigidArea(new Dimension(20,0)));
		tab.add(thisKid);
		
		pane.setMaximumSize(new Dimension(320,School.rect.height-355));
		

		

		thisKid.setBorder(BorderFactory.createLineBorder(Color.black));
		
		

		tab.revalidate();
		tab.repaint();
	}
	
	public void indAss() {
		
	}
	
	public void assMarks() {
		
	}
	
	public void addAss() {
		
	}
	
	public void removeAss() {
		
	}
	
	public void submitGrades(int id) {
		Student s = new Student();
		for(int i=0;i<students.size();i++) {
			if(students.get(i).getStudentNumber() == id) {
				s = students.get(i);
				break;
			}
		}
		
		try {
			midterms.replace(s, Double.parseDouble(midterming.getText()));
			
			for(int i=0;i<assignments.size();i++) {
				grades.get(assignments.get(i)).replace(s, Double.parseDouble(assMarks.get(i).getText()));
			}
			indStudentMarks(id);
		}
		catch(NumberFormatException e) {}
	}
	

	public void submitAttendance() {

		for (int i = 0; i < students.size(); i++) {
			if(attendance.get(students.get(i))[courseDay] !=null) {
				if (!(attendance.get(students.get(i))[courseDay].getPresent())) {
					setAbsent(students.get(i), (String) students.get(i).getAbsentReason().getSelectedItem());
				} else if (attendance.get(students.get(i))[courseDay].getLate()) {
					String time = students.get(i).getMinsLate().getText();
					if (time.length() > 2 && time.contains(":")) {
						int pivot = time.indexOf(":");
						int hour = Integer.parseInt(time.substring(0, pivot));
						int minute = Integer.parseInt(time.substring(pivot + 1, pivot + 3));

						if (hour<8) {
							hour += 12;
						}
						
						int diff;
						LocalTime lateness;
						try {
							lateness = LocalTime.of(hour, minute);
						}
						catch(DateTimeException e) {
							hour = start.getHour();
							minute=start.getMinute()+1;
							
							lateness = LocalTime.of(hour, minute);
						}
						

						diff = (int) start.until(lateness, ChronoUnit.MINUTES);
						if (diff > 74) {
							diff = 1;
						}
						setLate(students.get(i), diff, lateness);
					}

				}
			}
			

		}
		goDash();
		// checks if all attendance is done
		// if not, you can't submit attendance
		for (int i = 0; i < students.size(); i++) {
			if (attendance.get(students.get(i))[courseDay] == null) {
				subAtt.setText("Day " + (courseDay+1) + " attendance not done");
				subAtt.setBackground(Color.decode("#cc473d"));
				
				return;
			}
			
		}
		subAtt.setText("Day " + (courseDay + 1) + " attendance done");
		subAtt.setBackground(Color.decode("#336b4e"));
		

	}

	public JPanel addDailys() {
		sortKids();
		dayChoice.setActionCommand("Day Choice");

		tab.removeAll();

		Box container = Box.createVerticalBox();
		for (int i = 0; i < getStudents().size(); i++) {
			container.add(getStudents().get(i).getDaily());
			container.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		pane = new JScrollPane(container);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16);

		tab.setLayout(new BorderLayout());
		tab.add(pane);

		changeAttDay(1);
		tab.revalidate();
		tab.repaint();

		if (daily.getY() == 170) {
			daily.setLocation(daily.getX(), 180);
			main.setLocation(main.getX(), 170);
			overallAtt.setLocation(overallAtt.getX(), 170);

		}

		return tab;
	}

	public JPanel overallAtt() {
		School.dashboard.remove(subAtt);
		School.dashboard.add(marks);
		
		
		sortKids();

		tab.removeAll();
		
		Box container = Box.createVerticalBox();

		for (int i = 0; i < students.size(); i++) { // thank god for java's auto garbage collection
			// almost makes up for how bad swing is
			JPanel thisStudentAtt = new JPanel(new FlowLayout(FlowLayout.LEADING));
			thisStudentAtt.setSize(School.rect.width - 200, 20);
			thisStudentAtt.setBorder(BorderFactory.createLineBorder(Color.black));

			Attend[] studentData = attendance.get(students.get(i));

			int lates = 0;
			int absents = 0;

			for (int j = 0; j <= courseDay; j++) {
				if (studentData[j] != null) {
					if (studentData[j].getPresent()) {
						if (studentData[j].getLate()) {
							lates++;
						}
					} else {
						absents++;
					}
				}
			}

			lates = (int) Math.round(((double) lates / (courseDay + 1)) * 100); // percent of days late

			absents = (int) Math.round(((double) absents / (courseDay + 1)) * 100); // percent of days absent

			JTextArea latePercent = new JTextArea("late " + String.valueOf(lates) + "% of days so far\t");
			latePercent.setEditable(false);
			latePercent.setFont(Student.studentStandard);
			JTextArea abPercent = new JTextArea("absent " + String.valueOf(absents) + "% of days so far\t");
			abPercent.setEditable(false);
			abPercent.setFont(Student.studentStandard);

			thisStudentAtt.add(Box.createRigidArea(new Dimension(0, 5)));
			thisStudentAtt.add(students.get(i).tabbedName());
			thisStudentAtt.add(Box.createRigidArea(new Dimension(20, 0)));
			thisStudentAtt.add(latePercent);
			thisStudentAtt.add(Box.createRigidArea(new Dimension(20, 0)));
			thisStudentAtt.add(abPercent);
			thisStudentAtt.add(Box.createRigidArea(new Dimension(20, 0)));
			JButton b = students.get(i).getStudentAttB();
			b.setText("View Individual");
			b.setFont(Student.studentStandard);
			thisStudentAtt.add(b);

			container.add(thisStudentAtt);
			container.add(Box.createRigidArea(new Dimension(0, 10)));

		}

		dayChoice.setActionCommand("Until day x");

		pane = new JScrollPane(container);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.getVerticalScrollBar().setUnitIncrement(16);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
			       pane.getVerticalScrollBar().setValue(0);      
			   }
		});
		
		tab.setLayout(new BorderLayout());
		tab.add(pane);
		tab.revalidate();
		tab.repaint();

		if (overallAtt.getY() == 170) {
			daily.setLocation(daily.getX(), 170);
			main.setLocation(main.getX(), 170);
			
			
			marks.setLocation(marks.getX(), 170);
			overallAtt.setLocation(overallAtt.getX(), 180);

		}

		return tab;
	}

	public void indAtt(int id) {

		dayChoice.setActionCommand("indAtt");

		Student s = new Student();
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getStudentNumber() == id) {
				s = students.get(i);
				currKid = id;

			}
		}

		tab.removeAll();

		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		for (int i = 0; i < students.size(); i++) {
			if (!(s.equals(students.get(i)))) {
				JButton b = students.get(i).getStudentAttB();
				b.setText(students.get(i).tabbedName().getText());
				b.setFont(new Font("Arial", 1, 25));

				JPanel temp = new JPanel(new GridLayout(1, 1));
				temp.add(b);
				container.add(temp);

				container.add(Box.createRigidArea(new Dimension(0, 10)));
			}
		}
		pane = new JScrollPane(container);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(16);

		
		

		JPanel thisKid = new JPanel();
		thisKid.setLayout(new BoxLayout(thisKid, BoxLayout.PAGE_AXIS));
		
		// individual attendance portion
		JPanel stats = new JPanel();
		stats.setLayout(new BoxLayout(stats, BoxLayout.PAGE_AXIS));
		// add the stats here
		JTextField title = new JTextField(s.getLastName() + ", " + s.getFirstName());
		title.setEditable(false);
		title.setFont(new Font("Arial", 1, 40));
		
		
		
		stats.add(title);
		
		
		Attend[] studentData = attendance.get(s);
		int lates = 0;
		int absents = 0;
		int minsLost = 0;
		
		
		for (int j = 0; j <= courseDay; j++) {
			if (studentData[j] != null) {
				if (studentData[j].getPresent()) {
					if (studentData[j].getLate()) {
						lates++;
						minsLost+=studentData[j].getMinutesLate();
					}
				} else {
					absents++;
					minsLost+=75;
				}
			}

		}

		lates = (int) Math.round(((double) lates / (courseDay + 1)) * 100); // percent of days late

		absents = (int) Math.round(((double) absents / (courseDay + 1)) * 100); // percent of days absent

		JTextArea latePercent = new JTextArea("Late " + String.valueOf(lates) + "% of days so far");
		latePercent.setEditable(false);
		latePercent.setFont(Student.studentStandard);
		JTextArea abPercent = new JTextArea("Absent " + String.valueOf(absents) + "% of days so far");
		abPercent.setEditable(false);
		abPercent.setFont(Student.studentStandard);
		
		int percentLost = (int) Math.round(((double) minsLost / ((courseDay + 1)*75)) * 100);
		JTextArea totalMins = new JTextArea("Not present for " + minsLost + " minutes of class so far "
				+ "(" + percentLost + "% of the class)");
		totalMins.setEditable(false);
		totalMins.setFont(Student.studentStandard);
		
		stats.add(latePercent);
		stats.add(abPercent);
		stats.add(totalMins);
		
		
		int specificlates = 0;
		int specificabsents = 0;
		int specificmins = 0;
		
		if (courseDay+1>=5) {
			for(int i=courseDay-4; i<=courseDay; i++) {
				if(studentData[i]!=null) {
					if (studentData[i].getPresent()) {
						if (studentData[i].getLate()) {
							specificlates++;
							specificmins+=studentData[i].getMinutesLate();
						}
					} else {
						specificabsents++;
						specificmins+=75;
					}
				}
				
			}
			
			int percentmins = (int) Math.round(((double) specificmins / ((5*75)) * 100));
			
			JTextArea lastweek = new JTextArea("\nLate " + specificlates + " time(s) and absent " + specificabsents + 
					" time(s) in the past week, \nmissing " + percentmins + "% of classtime over that period");
			lastweek.setEditable(false);
			lastweek.setFont(Student.studentStandard);
			
			stats.add(lastweek);
		}
		if (courseDay+1>=10) {
			specificlates = 0;
			specificabsents = 0;
			specificmins = 0;
			
			for(int i=courseDay-9; i<=courseDay; i++) {
				if(studentData[i]!=null) {
					if (studentData[i].getPresent()) {
						if (studentData[i].getLate()) {
							specificlates++;
							specificmins+=studentData[i].getMinutesLate();
						}
					} else {
						specificabsents++;
						specificmins+=75;
					}
				}
				
			}
			
			int percentmins = (int) Math.round(((double) specificmins / ((10*75)) * 100));
			
			JTextArea twoweeks = new JTextArea("\nLate " + specificlates + " time(s) and absent " + specificabsents + 
					" time(s) in the past two weeks, \nmissing " + percentmins + "% of classtime over that period");
			twoweeks.setEditable(false);
			twoweeks.setFont(Student.studentStandard);
			stats.add(twoweeks);
		}
		
		if (courseDay+1>=20) {
			specificlates = 0;
			specificabsents = 0;
			specificmins = 0;
			for(int i=courseDay-19; i<=courseDay; i++) {
				if(studentData[i]!=null) {
					if (studentData[i].getPresent()) {
						if (studentData[i].getLate()) {
							specificlates++;
							specificmins+=studentData[i].getMinutesLate();
						}
					} else {
						specificabsents++;
						specificmins+=75;
					}
				}
				
			}
			
			int percentmins = (int) Math.round(((double) specificmins / ((20*75)) * 100));
			
			JTextArea lastmonth = new JTextArea("\nLate " + specificlates + " time(s) and absent " + specificabsents + 
					" time(s) in the past month, \nmissing " + percentmins + "% of classtime over that period");
			lastmonth.setEditable(false);
			lastmonth.setFont(Student.studentStandard);
			stats.add(lastmonth);
		}
		final JScrollPane statistics = new JScrollPane(stats);
		statistics.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		statistics.getVerticalScrollBar().setUnitIncrement(10);
		
		JPanel attending = new JPanel();

		attending.setLayout(new BoxLayout(attending, BoxLayout.PAGE_AXIS));
		
		
		for (int i = 0; i <= courseDay; i++) {
			Attend temp = studentData[i];
			if (temp == null) {
				continue;
			}

			JTextArea thisday = new JTextArea("Attendance on day " + (i + 1) + ":\t");
			if (temp.getPresent()) {
				if (temp.getLate()) {
					thisday.setText(thisday.getText() + "Late: " + temp.getMinutesLate() + " minutes");
					thisday.setBackground((Color.decode("#104E8B")));
				} else {
					thisday.setText(thisday.getText() + "Present");
					thisday.setBackground((Color.decode("#006d0c")));
				}
			} else {
				thisday.setText(thisday.getText() + "Absent: " + temp.getReason());
				thisday.setBackground((Color.decode("#cc473d")));

			}
			thisday.setFont(new Font("Arial", 1, 25));	
			thisday.setBorder(BorderFactory.createLineBorder(Color.black));

			thisday.setEditable(false);
			attending.add(thisday);

		}
		final JScrollPane attPanel = new JScrollPane(attending);

		attPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		attPanel.getVerticalScrollBar().setUnitIncrement(10);
		
		statistics.setMaximumSize(new Dimension(School.rect.width-540, statistics.getHeight()));
		attPanel.setMaximumSize(new Dimension(School.rect.width-540, attPanel.getHeight()));
		
	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
			       statistics.getVerticalScrollBar().setValue(0);
			       attPanel.getVerticalScrollBar().setValue(0);
			   }
		});
		
		
		thisKid.add(statistics);
		thisKid.add(Box.createRigidArea(new Dimension(0, 20)));
		thisKid.add(attPanel);

		tab.setLayout(null);
		tab.add(pane);
		tab.add(thisKid);

		pane.setSize(320, School.rect.height - 355);

		thisKid.setBounds(320, 0, School.rect.width - 500, School.rect.height - 355);

		thisKid.setBorder(BorderFactory.createLineBorder(Color.black));

		tab.revalidate();
		tab.repaint();

	}

	public void goDash() {
		
		School.dashboard.remove(marksDash);
		School.dashboard.remove(assDash);
		School.dashboard.remove(addAss);
		School.dashboard.remove(removeAss);
		School.dashboard.remove(goBack);
		
		
		School.dashboard.add(main);
		School.dashboard.add(daily);
		School.dashboard.add(overallAtt);
		School.dashboard.add(dayChoice);
		School.dashboard.add(marks);
		School.dashboard.remove(subAtt);
		
		
		changeAttDay(4);
		tab.removeAll();
		tab.setLayout(new BoxLayout(tab, BoxLayout.Y_AXIS));
		tab.add(dashboard);
		tab.revalidate();
		tab.repaint();
		
		if (main.getY() == 170) {
			daily.setLocation(daily.getX(), 170);
			main.setLocation(main.getX(), 180);

			
			marks.setLocation(marks.getX(), 170);
			overallAtt.setLocation(overallAtt.getX(), 170);

		}

	}

	public void setNull(Student s) {
		s.setMinsLate("Time arrived");
		s.setAbsentReason(0);
		s.setNull();

		tab.revalidate();
		tab.repaint();
	}

	public void setPresent(Student s) {
		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] = new Attend(true, false, 0, "");
		attendance.replace(s, thisAttendance);
		s.gPresent();
		s.setMinsLate("Time arrived");
		s.setAbsentReason(0);
		tab.revalidate();
		tab.repaint();

	}

	public void setAbsent(Student s) {
		Attend[] thisAttendance = attendance.get(s);

		thisAttendance[courseDay] = new Attend(false, false, 0, (String) s.getAbsentReason().getSelectedItem());

		attendance.replace(s, thisAttendance);
		s.rAbsent();
		s.setMinsLate("Time arrived");

		tab.revalidate();
		tab.repaint();
	}

	public void setLate(Student s) {
		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] = new Attend(true, true, 1, "");
		attendance.replace(s, thisAttendance);

		s.bLate();
		s.setAbsentReason(0);

		tab.revalidate();
		tab.repaint();

	}

	public void setLate(Student s, int mins, LocalTime time) {
		int h = time.getHour();
		String minute = String.valueOf(time.getMinute());
		if(minute.length()<2) {
			minute = "0"+minute;
		}
		if (h > 12) {
			h -= 12;
		}
		s.setMinsLate(h + ":" + minute);

		s.setAbsentReason(0);
		s.bLate();

		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] = new Attend(true, true, mins, "");
		attendance.replace(s, thisAttendance);

		tab.revalidate();
		tab.repaint();
	}

	public void setAbsent(Student s, String reason) {
		s.setMinsLate("Time arrived");
		s.setAbsentReason(reason);
		s.rAbsent();

		Attend[] thisAttendance = attendance.get(s);
		thisAttendance[courseDay] = new Attend(false, false, 0, reason);
		attendance.replace(s, thisAttendance);

		tab.revalidate();
		tab.repaint();
	}

	public void changeAttDay(int type) {
		String choice = String.valueOf(dayChoice.getSelectedItem()).split(" ")[1];
		int day = Integer.parseInt(choice) - 1;
		courseDay = day;
		subAtt.setText("Submit Attendance");
		subAtt.setBackground(null);

		if (type == 1) {
			for (int i = 0; i < students.size(); i++) {
				Attend temp = attendance.get(students.get(i))[day];

				if (temp != null) {
					if (temp.getPresent()) {
						if (attendance.get(students.get(i))[day].getLate()) {
							setLate(students.get(i), temp.getMinutesLate(), start.plusMinutes(temp.getMinutesLate()));
						} else {
							setPresent(students.get(i));
						}

					} else {
						setAbsent(students.get(i), temp.getReason());

					}
				} else {
					setNull(students.get(i));
				}

			}
		} else if (type == 2) {
			overallAtt();
		} else if (type == 3) {
			indAtt(currKid);
		}
		

	}

	/**
	 * Method Name: selectionSort
	 * 
	 * @Author Rajat Mishra, Prabhjot Chopra
	 * @Date 09/01/2022
	 * @Modified 01/05/2023
	 * @Description java implementation of selection sort for string linked lists
	 * @Parameters LinkedList<String> to be sorted
	 * @Returns time in milliseconds to sort the array Dependencies: N/A
	 * @Throws N/A
	 */
	public void sortKids() {

		int n = students.size();

		// One by one move boundary of unsorted subarray
		for (int i = 0; i < n - 1; i++) {
			// Find the minimum element in unsorted array
			int min_idx = i;
			for (int j = i + 1; j < n; j++)
				if (students.get(j).getLastName().compareTo(students.get(min_idx).getLastName()) < 0)
					min_idx = j;

				else if (students.get(j).getLastName().compareTo(students.get(min_idx).getLastName()) == 0) {
					if (students.get(j).getFirstName().compareTo(students.get(min_idx).getFirstName()) < 0) {
						min_idx = j;
					}

				}

			// Swap the found minimum element with the first
			// element
			Student temp = students.get(min_idx);
			students.set(min_idx, students.get(i));
			students.set(i, temp);
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

	public JScrollPane getPane() {
		return pane;
	}

	public LocalTime getStart() {
		return start;
	}

	public JButton getSubAtt() {
		return subAtt;
	}

	public JButton getOverallAtt() {
		return overallAtt;
	}
	public JButton getMarks() {
		return marks;
	}

	public void setProf(Teacher t) {
		prof = t;
	}

	public Attend[] getAttendance(Student s) {
		return attendance.get(s);
	}

	public void setAttendance(Attend[] a, Student s) {
		attendance.replace(s, a);
	}
}
