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
	
	public Student() {}
	public Student(Hashtable<Course, Boolean> requests, int grade, String first, String last, int id) {
		courseReqs = requests;
		year = grade;
		firstName = first;
		lastName = last;
		studentNumber = id;
		FlatDarkLaf.setup();

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
		String padName = lastName + ", " + firstName + "\t";
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		double width = (studentStandard.getStringBounds(padName, frc).getWidth());
		
		if (width<138.91) {
			padName += "\t";
		}
		
		JTextArea name = new JTextArea(padName);
		name.setEditable(false);
		name.setFont(studentStandard);
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
}
