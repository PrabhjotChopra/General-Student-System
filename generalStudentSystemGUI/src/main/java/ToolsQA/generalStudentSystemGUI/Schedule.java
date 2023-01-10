package ToolsQA.generalStudentSystemGUI;

import java.util.LinkedList;

public class Schedule {
	
	private LinkedList<ClassCourse>[] madeSchedule = new LinkedList[8];
	
	public Schedule(LinkedList<Student> students, LinkedList<Teacher> teachers, LinkedList<Course> courses) {
		for(int i=0;i<madeSchedule.length;i++) {
			madeSchedule[i] = new LinkedList<ClassCourse>();
		}
		
		for(int i=0; i<courses.size();i++) {
			
			
			double requests = 0;
			for(int j = 0;j<students.size();j++) {
				if (students.get(i).wantsCourse(courses.get(i))) {
					requests+=1;
				}
			}
			if(requests < courses.get(i).getMinKids()) {
				continue;
			}
			int margin = courses.get(i).getMaxKids() - courses.get(i).getMinKids();
			
			courses.get(i).setNumClasses((int) Math.ceil(requests/courses.get(i).getMaxKids()));
			int leftoverClass = 0;
			int drawFromAnother = 0;
			
			if (requests % courses.get(i).getMaxKids() !=0) {
				leftoverClass = (int) (requests % courses.get(i).getMaxKids());
			}
			if(leftoverClass<courses.get(i).getMinKids()) {
				drawFromAnother = courses.get(i).getMinKids() - leftoverClass;
				
				if(drawFromAnother> margin * courses.get(i).getNumClasses()) {
					courses.get(i).setNumClasses(courses.get(i).getNumClasses() - 1);
					drawFromAnother = 0;
					leftoverClass = 0;
				}
			}
			// ok now we've got the number of students in each class
			int sem1 = 1;
			int currNum = 1;
			String currRoom = courses.get(i).getUsableRooms().get(0);
			
			for(int j=0;j<courses.get(i).getNumClasses(); j++) {
				
				
				if(sem1==1) {
					madeSchedule[(j % 4)].add(new ClassCourse(currNum, new Teacher(), currRoom, (j % 4)+1, courses.get(i)));
							
							
				}
				else {
					madeSchedule[(j % 4)+4].add(new ClassCourse(currNum, new Teacher(), currRoom, (j % 4)+5, courses.get(i)));
				}
				
				currNum++;
				sem1*=-1;
			}
			
			
			
		}
		
	}
}
