package childminder;

import java.util.ArrayList;

public class ChildRegister
{
	static ArrayList<Child> childList = new ArrayList<Child>();
	static ArrayList<Attendance> attendanceList  = new ArrayList<Attendance>();
	

	public static void main( String[] args ) {

		menu();

		int testMode = 1;

		if (testMode ==1) {
			System.out.println(" *** RUNNING IN TESTING MODE ***");
			testChild();
			testAttendance();
		}
		else {

			System.out.println("Children - "+childList.size()+" max ID - "+Child.maxId);
			System.out.println("Attendances - "+attendanceList.size()+" max ID - "+Attendance.maxId);


			if(childList.isEmpty()) {
				System.out.println("No children present");
			}	    
			else
			{System.out.println("We have children present");}

			// save data files away
			/*      for (int counter = 0; counter < childList.size(); counter++) { 		      
	    	  System.out.println(childList.get(counter));
	    	  Child.serializeChild(childList.get(counter));
	      } 
			 */	      
			//			Child.serializeChildList(childList);

			// reset file
			//			Child.clearChildListFile();

			// add a child

			// check a child in

			// check a child out
		}

	}

	public static void menu(){
		// Local variable
		int swValue = -1;

		while (swValue != 0){
			// Display menu graphics
			System.out.println("================================");
			System.out.println("|     MENU SELECTION DEMO      |");
			System.out.println("================================");
			System.out.println("| Options:                     |");
			System.out.println("|        0. Exit               |");
			System.out.println("|        1. Load Children      |");
			System.out.println("|        2. Load Attendances   |");
			System.out.println("|        3. Load Rates         |");
			System.out.println("|        4. Add Child          |");
			System.out.println("|        5. Check in child 1   |");  
			System.out.println("|        6. Check out child 1  |");
			System.out.println("|        7. Show Children      |");
			System.out.println("|        8. Show Attendances   |");
			System.out.println("|        9. ?                  |");
			System.out.println("================================");
			swValue = KeyInput.inInt(" Select option: ");

			// Switch construct
			switch (swValue) {
			case 1:
				System.out.println("Load Children");
				childList = initialiseChildren();
				break;
			case 2:
				System.out.println("Load Attendances");
				attendanceList = initialiseAttendance();
				break;
			case 3:
				System.out.println("Load Rates - not yet enabled");
				break;
			case 4:
				System.out.println("Add Child");
				break;
			case 5:
				System.out.println("Check in child 1");

				if (childList.size()== 0){
					System.out.println("no children exist, so can't check in child 1");
				}
				else {
				childList.get(0).checkIn();
				}
				break;
			case 6:
				System.out.println("Check out child 1");
				if (childList.size()== 0){
					System.out.println("no children exist, so can't check out child 1");
				}
				else {
				childList.get(0).checkOut();
				}
				break;
			case 7:
				System.out.println("Show Children");
				System.out.println("Number of Children = "+childList.size());
				for (Child kid : childList){
					System.out.println(kid);
				}
				break;
			case 8:
				System.out.println("Show Attendances");
				System.out.println("Number of Attendances = "+attendanceList.size());
				for (Attendance att : attendanceList){
					System.out.println(att);
				}
				break;
			case 9:
				System.out.println("Save");
				Child.serializeChildList(childList);
				Attendance.serializeAttendanceList(attendanceList);
				break;
			case 0:
				System.out.println("Exit selected");
				break;
			default:
				System.out.println("Invalid selection");
				break; // This break is not really necessary
			}}
	}

	public static ArrayList<Child> initialiseChildren (){
		// deserialise
		ArrayList<Child> totallyNewList = Child.deserializeChildList();
		System.out.println("initialiseChildren");
		for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      
			System.out.println("counter : "+counter);
			System.out.println(totallyNewList.get(counter));
		} 
		Child.maxId = totallyNewList.size();
		return totallyNewList;


		// set max ID code
	}
	public static ArrayList<Attendance> initialiseAttendance (){
		// deserialise
		ArrayList<Attendance> totallyNewList = Attendance.deserializeAttendanceList();
		System.out.println("initialiseAttendance");
		for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      
			System.out.println("counter : "+counter);
			System.out.println(totallyNewList.get(counter));
		} 
		Attendance.maxId = totallyNewList.size();
		return totallyNewList;


		// set max ID code
	}

	private static void testChild(){
		System.out.println("testing Child processing");

		System.out.println("getting Children from file:");
		ArrayList<Child> testChildArray = initialiseChildren();
		System.out.println("Array now holds "+testChildArray.size());

		// trial of adding children method #1
		Child testChild = new Child();
		testChild.setChildID(91);
		testChild.setFirstName("Test");
		testChild.setLastName("Child");
		testChildArray.add(testChild);


		// Adding children - method #2
		Child testChild2 = new Child();
		testChild2.setChild(92, "Child-Two", "Test");
		testChildArray.add(testChild2);

		System.out.println("size of array : "+ testChildArray.size());



	}

	private static void testAttendance(){
		System.out.println("testing Attendance processing");

		System.out.println("getting Attendances from file:");
		ArrayList<Attendance> testAttendanceArray = initialiseAttendance();
		System.out.println("Array now holds "+testAttendanceArray.size());

		// trial adding attendances -  checking child in

		// trial adding attendances -  checking child out default

		// trial adding attendances -  checking child in


	}




}
