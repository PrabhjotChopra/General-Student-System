package ToolsQA.generalStudentSystemGUI;

import java.util.Hashtable;
import java.util.LinkedList;

public class Schedule {
	
	@SuppressWarnings("unchecked")
	private LinkedList<ClassCourse>[] madeSchedule = new LinkedList[8];
	private LinkedList<Student> kids = new LinkedList<Student>();
	private LinkedList<Student> teachers = new LinkedList<Student>();
	private LinkedList<Student> courses = new LinkedList<Student>();
	private LinkedList<Student> classes = new LinkedList<Student>();
	
	// courses should be added to the courses linkedlist based on absolute priority (i.e. gr12 english, then gr12 math, then gr12 science and so on)
	public Schedule(LinkedList<Student> students, LinkedList<Teacher> teachers, LinkedList<Course> courses, LinkedList<String> rooms) {
		for(int i=0;i<madeSchedule.length;i++) {
			madeSchedule[i] = new LinkedList<ClassCourse>();
		}
		
		Hashtable<String, Boolean[]> roomUsed = new Hashtable<String, Boolean[]>();
		
		for(int i=0;i<rooms.size();i++) {
			roomUsed.put(rooms.get(i), new Boolean[8]);
		}
		
		
		for(int i=0; i<courses.size();i++) {
			
			
			double requests = 0;
			for(int j = 0;j<students.size();j++) {
				if (students.get(j).wantsCourse(courses.get(i))) {
					requests+=1;
				}
			}
			if(requests < courses.get(i).getMinKids()) {
				continue;
			}
			int margin = courses.get(i).getMaxKids() - courses.get(i).getMinKids();
			
			int numClasses = (int) Math.ceil(requests/courses.get(i).getMaxKids());
			LinkedList<Teacher> teachersAvailable = new LinkedList<Teacher>();
			for(int j = 0; j<teachers.size();j++) {
				if (teachers.get(j).getTeachables().contains(courses.get(i).getType())){
					teachersAvailable.add(teachers.get(j));
					
				}
			}
			if(numClasses>teachersAvailable.size()*6) {
				numClasses = teachersAvailable.size()*6;
			}
			
			
			courses.get(i).setNumClasses(numClasses);
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
			int roomIndex = 0;
			String currRoom = courses.get(i).getUsableRooms().get(roomIndex);
			
			for(int j=0;j<courses.get(i).getNumClasses(); j++) {
				// checks if room is available (adds the course periods 1-4 alternating semesters)
				// add randomizing course placement as a stretch goal
				if(roomUsed.get(currRoom)[7]) { 
					currNum++;
					
					roomIndex++;
					if(roomIndex == courses.get(i).getUsableRooms().size()) { // ran out of usable rooms (realistically this will never happen)
						break;
					}
					currRoom = courses.get(i).getUsableRooms().get(roomIndex);
					continue;
				}
				Teacher thisProf = teachersAvailable.get((int) (Math.random() * teachersAvailable.size()));
				if(sem1==1) {
					
					
					ClassCourse thisClass = new ClassCourse(currNum, new Teacher(), currRoom, j%4+1, courses.get(i));
					while(!(thisProf.addClass(thisClass, 1))) { // if this teacher doesn't have room then we randomly pick another
						thisProf = teachersAvailable.get((int) (Math.random() * teachersAvailable.size()));
					}
					// we're gonna assume that there's always enough teachers and that the school isn't under staffed
					// this is a course scheduler, not a cost-of-living/wage/bureaucracy fixer
					
					thisClass.setProf(thisProf);
					madeSchedule[j%4].add(thisClass);
					roomUsed.get(currRoom)[j%4] = true;
						
				}
				else {
					ClassCourse thisClass = new ClassCourse(currNum, new Teacher(), currRoom, j%4+5, courses.get(i));
					
					
					while(!(thisProf.addClass(thisClass, 2))) { // if this teacher doesn't have room then we randomly pick another
						thisProf = teachersAvailable.get((int) (Math.random() * teachersAvailable.size()));
					}
					
					
					thisClass.setProf(thisProf);
					madeSchedule[(j%4)+4].add(thisClass);
					roomUsed.get(currRoom)[(j%4)+4] = true;
				}
				
				currNum++; // current number of classes for this course
				sem1*=-1; // used to alternate semesters
			} // end for loop to add individual classes
			
			
			
		} // end for loop for adding courses
		
		for(int i=0;i<students.size();i++) {
			
		}
		
		
		
	} // end schedule constructor
	
	public LinkedList<ClassCourse>[] getSchedule(){
		return madeSchedule;
	}
} // end class schedule
