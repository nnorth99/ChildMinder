package childminder;

import java.time.LocalDate;
import java.util.ArrayList;

public class ChildRegister
{
	static ArrayList<Child> childList = new ArrayList<Child>();
	static ArrayList<Attendance> attendanceList  = new ArrayList<Attendance>();
	static ArrayList<RateCard> rateCardList  = new ArrayList<RateCard>();


	public static void main( String[] args ) {
		menu();
	}

	public static void menu(){
		// Local variable
		int swValue = -1;

		while (swValue != 0){
			// Display menu graphics
			System.out.println("=========================================");
			System.out.println("|         MENU SELECTION DEMO           |");
			System.out.println("=========================================");
			System.out.println("| Options:                              |");
			System.out.println("|        0. Exit                        |");
			System.out.println("|        1. Load                        |");
			System.out.println("|        2. Add Default Rate            |");
			System.out.println("|        3. Add individual Rate child 1 |");
			System.out.println("|        4. Add Child                   |");
			System.out.println("|        5. Check in child 1            |");  
			System.out.println("|        6. Check out child 1           |");
			System.out.println("|        7. Show data                   |");
			System.out.println("|        8.                             |");
			System.out.println("|        9. Save                        |");
			System.out.println("=========================================");
			swValue = KeyInput.inInt(" Select option: ");

			// Switch construct
			switch (swValue) {
			case 1:
				load();
				break;
			case 2:
				System.out.println("Add Default Rate");

				LocalDate defaultDate = LocalDate.of(2000, 01, 01);
				RateCard.createNewRate(3.5f, defaultDate, rateCardList);
				
				System.out.println("Number of Rates = "+rateCardList.size());
				for (RateCard rate: rateCardList){
					System.out.println(rate);
				}

				break;
			case 3:
				System.out.println("New Rate for child 1");
				RateCard.createNewRate(1, 4.5f, LocalDate.now(), rateCardList);
				
				for (RateCard rate: rateCardList){
					System.out.println(rate);
				}
				break;
			case 4:
				System.out.println("Add Child");
				// Adding child
				Child newChild = new Child();
				newChild.setChild(++Child.maxId, "New", "Child "+Child.maxId);
				childList.add(newChild);

				System.out.println("size of array : "+ childList.size());
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
				System.out.println("Number of Children = "+childList.size());
				for (Child kid : childList){
					System.out.println(kid);
				}
				System.out.println("Number of Attendances = "+attendanceList.size());
				for (Attendance att : attendanceList){
					System.out.println(att);
				}
				System.out.println("Number of Rate Cards = "+rateCardList.size());
				for (RateCard rate : rateCardList){
					System.out.println(rate);
				}
				break;
			case 8:
				System.out.println("Option not yet enabled");
				break;
			case 9:
				System.out.println("Save");
				save();
				break;
			case 0:
				System.out.println("Exit selected");
				break;
			default:
				System.out.println("Invalid selection");
				break; // This break is not really necessary
			}}
	}

	public static void save (){
		// serialise
		System.out.println("save children - elements = "+ childList.size());
		Child.serializeChildList(childList);
		System.out.println("save attendances - elements = "+ attendanceList.size());
		Attendance.serializeAttendanceList(attendanceList);
		System.out.println("save Rates - elements = "+ rateCardList.size());
		RateCard.serializeRateCardList(rateCardList);
	}

	public static void load (){
		// deserialise
		System.out.println("Load Children");
		childList = initialiseChildren();
		System.out.println("Load Attendances");
		attendanceList = initialiseAttendance();
		System.out.println("Load Rates");
		rateCardList = initialiseRates();
	}


	public static ArrayList<RateCard> initialiseRates (){
		// deserialise
		ArrayList<RateCard> totallyNewList = RateCard.deserializeRateCardList();
		System.out.println("initialiseRates");
		for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      
			System.out.println("counter : "+counter);
			System.out.println(totallyNewList.get(counter));
		} 
		Child.maxId = totallyNewList.size();
		return totallyNewList;
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
	}

}
