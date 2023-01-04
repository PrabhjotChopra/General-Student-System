package ToolsQA.generalStudentSystemGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
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
	private Font studentStandard = new Font("Arial", 1, 20);

	private JTextField absentReason;
	private JTextField minsLate;
	private JButton present;
	private JButton absent;
	private JButton late;

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

		String padName = first + " " + last + "\t";
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		double width = (studentStandard.getStringBounds(padName, frc).getWidth());
		
		if (width<134.5) {
			padName += "\t";
		}
		
		
		
		JTextArea name = new JTextArea(padName);

		name.setEditable(false);
		name.setFont(studentStandard);
		
		
		
		present = new JButton("Present");
		present.setFont(studentStandard);
		present.addActionListener(new School());

		absent = new JButton("Absent");
		absent.setFont(studentStandard);
		absent.addActionListener(new School());

		late = new JButton("Late");
		late.setFont(studentStandard);
		late.addActionListener(new School());

		minsLate = new JTextField("Time arrived");
		minsLate.setFont(studentStandard);
		minsLate.addFocusListener(new School());
		minsLate.setFocusable(false);

		absentReason = new JTextField("Reason for absence");
		absentReason.setFont(studentStandard);
		absentReason.addFocusListener(new School());
		absentReason.setFocusable(false);

		daily.add(Box.createRigidArea(new Dimension(0, 5)));
		daily.add(name);
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

	}

	public void setNull() {
		present.setBackground(null);
		absent.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		absentReason.setFocusable(false);
	}

	public void gPresent() {
		present.setBackground(Color.decode("#40826d"));
		absent.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		absentReason.setFocusable(false);
	}

	public void rAbsent() {
		absent.setBackground(Color.RED);
		present.setBackground(null);
		late.setBackground(null);

		minsLate.setFocusable(false);
		absentReason.setFocusable(true);
	}

	public void bLate() {
		late.setBackground(Color.BLUE);
		absent.setBackground(null);
		present.setBackground(null);

		minsLate.setFocusable(true);
		absentReason.setFocusable(false);

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

	public JTextField getAbsentReason() {
		return absentReason;
	}

	public JTextField getMinsLate() {
		return minsLate;
	}

	public void setAbsentReason(String s) {
		absentReason.setText(s);

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
}
