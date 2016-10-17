package childminder;

import java.time.LocalDate;
import java.util.ArrayList;

public class ChildRegister
{
	static boolean debug = false;
	static ArrayList<Child> childList = new ArrayList<Child>();
	static ArrayList<Attendance> attendanceList  = new ArrayList<Attendance>();
	static ArrayList<RateCard> rateCardList  = new ArrayList<RateCard>();


	public static void main( String[] args ) {
		for (String arg : args){
			if (arg.equals("-d")){
				debug = true;
			}
		}
		menu();
	}

	public static void menu(){
		// Local variable
		int swValue = -1;
		int inInt;
		Double inDouble; 

		while (swValue != 0){
			// Display menu graphics
			if (debug){System.out.println("** debug mode **");}
			System.out.println("=========================================");
			System.out.println("|         MENU SELECTION                |");
			System.out.println("=========================================");
			System.out.println("| Options:                              |");
			System.out.println("|        0. Exit                        |");
			System.out.println("|        1. Load                        |");
			System.out.println("|        2. Add Default Rate            |");
			System.out.println("|        3. Add individual Rate         |");
			System.out.println("|        4. Add Child                   |");
			System.out.println("|        5. Check in child              |");  
			System.out.println("|        6. Check out child             |");
			System.out.println("|        7. Show data                   |");
			System.out.println("|        8. Clear ALL saved data        |");
			System.out.println("|        9. Save                        |");
			System.out.println("=========================================");
			swValue = KeyInput.inInt(" Select option: ");

			// Switch construct
			switch (swValue) {
			case 1:
				load();
				break;
			case 2:
				if (debug){System.out.println("Add Default Rate");}

				LocalDate defaultDate = LocalDate.of(2000, 01, 01);
				inDouble = KeyInput.inDouble("Enter default rate");
				RateCard.createNewRate(inDouble, defaultDate, rateCardList);

				if (debug){System.out.println("Number of Rates = "+rateCardList.size());}

				RateCard.ListRateCards(rateCardList);
				break;
			case 3:
				System.out.println("IDIVIDUAL RATES ONLY (for default rate, use default rate option)");
				System.out.println("Children available:");
				for (Child kid : childList){
					System.out.println(kid);
				}

				inInt = KeyInput.inInt("Enter Child ID number: ",1,childList.size());
				inDouble = KeyInput.inDouble("Enter rate: ", 0d, 20d);
				RateCard.createNewRate(inInt, inDouble, LocalDate.now(), rateCardList);

				RateCard.ListRateCards(rateCardList);
				break;
			case 4:
				if (debug){System.out.println("Add Child");}

				// get details
				String firstName =  KeyInput.inString("Enter First Name :");
				String lastName =  KeyInput.inString("Enter Last Name :");

				// Adding child
				Child newChild = new Child(firstName, lastName);
				if (debug){System.out.println("child set "+newChild);}
				childList.add(newChild);

				Child.ListChildren(childList);

				break;
			case 5:
				if (childList.size()== 0){
					System.out.println("no children exist, so can't check in child 1");
				}
				else {
					System.out.println("Check in child");

					Child.ListChildren(childList);
					swValue = KeyInput.inInt("Select Child ID number: ", 1, childList.size() );
					try{
						childList.get(swValue - 1).checkIn();
					}
					catch (IndexOutOfBoundsException e) {
						System.out.println("Invalid Child ID entered, try again");
					}
				}
				break;
			case 6:
				if (childList.size()== 0){
					System.out.println("no children exist, so can't check out child");
				}
				else {
					System.out.println("Check out child: children currently in:");
					boolean kidsIn = false;
					for (Child kid : childList){
						// check if there is already an open attendance for this kid
						if (kid.hasOpenAttendance()){
							kidsIn = true;
							System.out.println(kid);
						}
					}
					if (kidsIn){
						swValue = KeyInput.inInt("Select Child ID number: ");
						try{
							childList.get(swValue - 1).checkOut();}
						catch (IndexOutOfBoundsException e){
							System.out.println("No child with that ID");
						}
					}
					else {
						System.out.println("None");
					}
				}
				break;
			case 7:
				Child.ListChildren(childList);
				Attendance.ListAttendances(attendanceList);
				RateCard.ListRateCards(rateCardList);
				break;
			case 8:
				System.out.println("Clear ALL saved data");
				char confirm = KeyInput.inChar("Are you sure - this will clear ALL data and cannot be reversed? (y/n)");
				if (confirm == 'y'){
					Child.clearChildListFile();
					Attendance.clearAttendanceListFile();
					RateCard.clearRateCardListFile();
					load();  // reload the empty files, i.e. empty out the arrays
				}
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
			}
			save();
		}

	}

	public static void save (){
		// serialise
		if (debug){System.out.println("save children - elements = "+ childList.size());}
		Child.serializeChildList(childList);
		if (debug){System.out.println("save attendances - elements = "+ attendanceList.size());}
		Attendance.serializeAttendanceList(attendanceList);
		if (debug){System.out.println("save Rates - elements = "+ rateCardList.size());}
		RateCard.serializeRateCardList(rateCardList);
	}

	public static void load (){
		// deserialise
		if (debug){System.out.println("Load Children");}
		childList = initialiseChildren();
		if (debug){System.out.println("Load Attendances");}
		attendanceList = initialiseAttendance();
		if (debug){System.out.println("Load Rates");}
		rateCardList = initialiseRates();
	}

	public static void initialise() {
		load();
	}

	public static ArrayList<RateCard> initialiseRates (){
		// deserialise
		ArrayList<RateCard> totallyNewList = RateCard.deserializeRateCardList();
		if (debug){System.out.println("initialiseRates");}
		if (debug){	
			for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      
				System.out.println("counter : "+counter);
				System.out.println(totallyNewList.get(counter));
			} 
		}
		return totallyNewList;
	}

	public static ArrayList<Child> initialiseChildren (){
		// deserialise
		ArrayList<Child> totallyNewList = Child.deserializeChildList();
		if (debug){
			System.out.println("initialiseChildren");
			for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      

				System.out.println("counter : "+counter);
				System.out.println(totallyNewList.get(counter));
			} 
			System.out.println("number of children loaded = "+  totallyNewList.size());
		}
		return totallyNewList;
	}

	public static ArrayList<Attendance> initialiseAttendance (){
		// deserialise
		ArrayList<Attendance> totallyNewList = Attendance.deserializeAttendanceList();
		if (debug){
			System.out.println("initialiseAttendance");
			for (int counter = 0; counter < totallyNewList.size(); counter++) { 		      

				System.out.println("counter : "+counter);
				System.out.println(totallyNewList.get(counter));
			}
		} 
		Attendance.maxId = totallyNewList.size();
		return totallyNewList;
	}

}
