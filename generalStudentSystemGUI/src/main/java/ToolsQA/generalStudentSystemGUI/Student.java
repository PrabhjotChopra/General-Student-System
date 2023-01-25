package ToolsQA.generalStudentSystemGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Enumeration;
import java.util.Hashtable;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;


public class Student {
	private ClassCourse[] classes = new ClassCourse[8];
	private Hashtable<Course, Boolean> courseReqs;
	private int year;
	private String firstName;
	private String lastName;
	private int studentNumber;

	private JPanel daily;
	public static Font studentStandard = new Font("Arial", 1, 20);

	private JComboBox<String> absentReason;
	private JTextField minsLate;
	private JButton present;
	private JButton absent;
	private JButton late;
	private String[] reasons;
	
	private JButton studentAttB;
	private JButton studentMarkB;
	
	
	private JPanel header;
	private JButton sem1;
	private JButton sem2;
	private JPanel courses;
	
	public Student() {}
	public Student(Hashtable<Course, Boolean> requests, int grade, String first, String last, int id) {
		courseReqs = requests;
		year = grade;
		firstName = first;
		lastName = last;
		studentNumber = id;
		FlatDarkLaf.setup();
		
		
		sem1 = new JButton("Semester 1");
		sem1.setBounds(100, 180, 200, 75);
		sem1.setFont(School.buttonFont);
		sem1.addActionListener(new School());
		sem1.setActionCommand("studsem1");
		
		sem2 = new JButton("Semester 2");
		sem2.setBounds(500, 160, 200, 75);
		sem2.setFont(School.buttonFont);
		sem2.addActionListener(new School());
		sem2.setActionCommand("studsem2");
		
		header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

		header.setBounds(100, 0, School.rect.width - 200, 150);
		header.setBorder(BorderFactory.createLineBorder(Color.black));

		JTextArea welcome = new JTextArea("Welcome, " + firstName +  lastName);

		welcome.setFont(new Font("Arial", 1, 50));
		header.add(welcome);
		welcome.setAlignmentX(0);
		welcome.setEditable(false);

		JButton classes = new JButton("Classes");
		classes.addActionListener(new School());

		classes.setFont(new Font("Arial", 1, 40));
		header.add(classes);
		classes.setAlignmentX(Component.RIGHT_ALIGNMENT);
		header.add(Box.createRigidArea(new Dimension(20, 0)));

		JButton exit = new JButton("Close app");
		exit.addActionListener(new School());

		exit.setFont(new Font("Arial", 1, 40));
		header.add(exit);
		exit.setAlignmentX(Component.RIGHT_ALIGNMENT);

		courses = new JPanel();
		courses.setLayout(new BoxLayout(courses, BoxLayout.Y_AXIS));
		courses.setBounds(100, 255, School.rect.width - 200, School.rect.height - 355);
		courses.setBorder(BorderFactory.createLineBorder(Color.black));

		
		daily = new JPanel();

		daily.setLayout(new BoxLayout(daily, BoxLayout.X_AXIS));
		daily.setSize(School.rect.width - 200, 20);
		
		present = new JButton("Present");
		present.setFont(studentStandard);
		present.addActionListener(new School());

		absent = new JButton("Absent");
		absent.setFont(studentStandard);
		absent.addActionListener(new School());

		late = new JButton("Late");
		late.setFont(studentStandard);
		late.addActionListener(new School());

		minsLate = new JTextField(7);
		minsLate.setText("Time arrived");
		minsLate.setFont(studentStandard);
		minsLate.addFocusListener(new School());
		minsLate.setFocusable(false);
		
		
		reasons = new String[7];
		reasons[1] = "Illness or injury";
		reasons[2] = "Appointment";
		reasons[3] = "Religious day";
		reasons[4] = "Bereavement";
		reasons[5] = "School transportation cancellation";
		reasons[6] = "Parent-approved absence";
		reasons[0] = "Not approved by parent";
		
		
		absentReason = new JComboBox<String>(reasons);
		absentReason.setFont(studentStandard);
		absentReason.setEnabled(false);
			
		
		
		daily.add(Box.createRigidArea(new Dimension(0, 5)));
		
		daily.add(tabbedName());
		daily.add(Box.createRigidArea(new Dimension(20, 0)));
		daily.add(present);
		daily.add(Box.createRigidArea(new Dimension(20, 0)));
		daily.add(absent);
		daily.add(Box.createRigidArea(new Dimension(20, 0)));
		daily.add(absentReason);
		daily.add(Box.createRigidArea(new Dimension(20, 0)));
		daily.add(late);
		daily.add(Box.createRigidArea(new Dimension(20, 0)));
		daily.add(minsLate);

		daily.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		
		
		studentAttB = new JButton();
		studentAttB.addActionListener(new School());
		studentAttB.setActionCommand("overAtt " + studentNumber);
		studentAttB.setFont(studentStandard);
		
		
		studentMarkB = new JButton();
		studentMarkB.addActionListener(new School());
		studentMarkB.setActionCommand("studentGrades " + studentNumber);
		studentMarkB.setFont(studentStandard);

	}

	public void setNull() {
		present.setBackground(null);
		absent.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		minsLate.setBackground(null);
		absentReason.setEnabled(false);
		absentReason.setBackground(null);
	}
	public JTextArea tabbedName() {
		JTextArea name = new JTextArea(lastName + ", " + firstName);
		
		name.setEditable(false);
		name.setFont(studentStandard);
		name.setMaximumSize(new Dimension(250,25));
		name.setPreferredSize(new Dimension(250,25));
		
		return name;
	}

	public void gPresent() {
		present.setBackground(Color.decode("#006d0c"));
		absent.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		minsLate.setBackground(null);
		absentReason.setEnabled(false);
		absentReason.setBackground(null);
	}

	public void rAbsent() {
		absent.setBackground(Color.decode("#cc473d"));
		present.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		minsLate.setBackground(null);
		absentReason.setEnabled(true);
		absentReason.setBackground(Color.decode("#cc473d"));
	}

	public void bLate() {
		late.setBackground(Color.decode("#104E8B"));
		absent.setBackground(null);
		present.setBackground(null);

		minsLate.setFocusable(true);
		minsLate.setBackground(Color.decode("#104E8B"));
		absentReason.setEnabled(false);
		absentReason.setBackground(null);

	}

	public void addClass(ClassCourse c) {
		classes[c.getPeriod() - 1] = c;
	}

	public void removeClass(int period) {
		classes[period - 1] = null;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public JPanel getDaily() {
		return daily;
	}

	public JComboBox<String> getAbsentReason() {
		return absentReason;
	}

	public JTextField getMinsLate() {
		return minsLate;
	}

	public void setAbsentReason(int n) {
		absentReason.setSelectedIndex(n);

	}
	public void setAbsentReason(String s) {
		for(int i=0; i<reasons.length;i++) {
			if(s.equals(reasons[i])) {
				absentReason.setSelectedIndex(i);
			}
		}
	}

	public void setMinsLate(String s) {
		minsLate.setText(s);

	}

	public JButton getPresent() {
		return present;
	}

	public JButton getLate() {
		return late;
	}

	public JButton getAbsent() {
		return absent;
	}
	public int getStudentNumber() {
		return studentNumber;
	}
	public JButton getStudentAttB() {
		return studentAttB;
	}
	public JButton getStudentMarkB() {
		return studentMarkB;
	}
	public boolean wantsCourse(Course c) {
		Enumeration<Course> courses = courseReqs.keys();
		
		while(courses.hasMoreElements()) {
			if (c.equals(courses.nextElement())) {
				return true;
			}
		}
		return false;
	}
	public JPanel getHeader() {
		return header;
	}

	public JButton getSem1() {
		return sem1;
	}

	public JButton getSem2() {
		return sem2;
	}
	public JPanel getCourses(int sem) {
		sem--;
		sem *= 4;
		for (int i = sem; i < sem + 4; i++) {
			if (classes[i] != null) {
				courses.add(classes[i].getBaseDisplay());
			}

		}
		return courses;
	}
	public void switchSem(int sem) {
		if (sem == 1 && sem2.getY() - sem1.getY() == 20) {
			sem1.setLocation(sem1.getX(), sem1.getY() + 20);
			sem2.setLocation(sem2.getX(), sem2.getY() - 20);

			courses.removeAll();
			for (int i = 0; i < 4; i++) {
				if (classes[i] != null) {
					courses.add(classes[i].getBaseDisplay());
				}

			}
			sem1.setBackground(Color.decode("#42a1e1"));
			sem2.setBackground(null);
			courses.revalidate();
			courses.repaint();
			

		} else if (sem == 2 && sem1.getY() - sem2.getY() == 20) {
			sem1.setLocation(sem1.getX(), sem1.getY() - 20);
			sem2.setLocation(sem2.getX(), sem2.getY() + 20);
			courses.removeAll();
			for (int i = 4; i < 8; i++) {
				if (classes[i] != null) {
					courses.add(classes[i].getBaseDisplay());
				}

			}
			sem2.setBackground(Color.decode("#42a1e1"));
			sem1.setBackground(null);
			courses.revalidate();
			courses.repaint();
		}
		

	}
}
