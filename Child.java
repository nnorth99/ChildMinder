package childminder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Child  implements Serializable{

	//
	// static variables
	//
	private static final String PATH = "childFile.sav";
	private static int maxId = 0;


	//
	// internal class Variables 
	//
	private static final long serialVersionUID = 1L;
	int childID;
	String lastName;
	String firstName;
	String contactDetails;

	public Child (String forename, String surname){
		childID = ++maxId;
		firstName = forename;
		lastName = surname;
		
	}

	//
	// getters and setters
	//
	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}


	public void setChild (int childID,
			String lastName,
			String firstName){
		this.firstName = firstName;
		this.lastName = lastName;
		this.childID = childID;
	}

	public Child getChild (){
		return this;
	}

	public void setFirstName (String name){
		this.firstName = name;
	}
	public String getFirstName (){
		return this.firstName;
	}

	public void setLastName (String name){
		this.lastName = name;
	}
	public String getLastName (){
		return this.lastName;
	}

	public void setChildID (int ID){
		this.childID = ID;
	}
	public int getChildID (){
		return this.childID;
	}

	@Override
	public String toString() {
		return new StringBuffer(" Name : ")
				.append(this.firstName)
				.append(" ")
				.append(this.lastName)
				.append(" ID : ")
				.append(this.childID).toString();
	}


	//
	// Methods
	//

	//serialise and deserialize children

	public static void serializeChildList(ArrayList<Child> kidsIn ){

		try{

			FileOutputStream fout = new FileOutputStream(PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(kidsIn);
			oos.close();
			if (ChildRegister.debug){System.out.println("Closed file in serializeChildList");}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	public static ArrayList<Child> deserializeChildList( ){

		ArrayList<Child> returnList = new ArrayList<Child>();
		try{
			if (ChildRegister.debug){System.out.println("deserialize children");}

			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);   

			ArrayList<Child> getList = (ArrayList<Child>) ois.readObject();
			returnList.addAll(getList);
			ois.close();

			if (ChildRegister.debug){System.out.println("total child records read= "+returnList.size());}
			maxId = returnList.size();
		}
		catch (FileNotFoundException e) {
			System.out.println("File doesn't exist");
			// file doesn't exist yet - first pass, ignore exception and allow empty list to be returned
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}
	
	public static void ListChildren (ArrayList<Child> list){
		System.out.println("");
		System.out.println("============");
		System.out.println("= Children =");
		System.out.println("============");
		for (Child kid : list){
			System.out.println(kid);
		}
	}

	public static void clearChildListFile(){
		ArrayList<Child> blankList = new ArrayList<Child>();
		serializeChildList(blankList);
	}

	public void checkIn (){
		// check if there is already an open attendance for this kid
		if (this.hasOpenAttendance()){
			System.out.println("can't check the child in - they already have an open attendance record. Check that one out first");
		}
		else {
			ChildRegister.attendanceList.add(new Attendance(this.childID));
		}
	}

	public void checkIn (Child kid, Date inDate){

	}

	public void checkOut (){
		if(!this.hasOpenAttendance()){
			System.out.println ("there is no open record of this child checking in - check in before you can check out");
		}
		else {
			Attendance att = getOpenAttendance();
			int attIndex = ChildRegister.attendanceList.indexOf(att);
			att.setDateOut();
			ChildRegister.attendanceList.set(attIndex, att);
		}

	}

	public void checkOut (Child kid, Date outDate){

	}


	public boolean hasOpenAttendance() {
		if (ChildRegister.attendanceList.size()>0){
			for (Attendance a : ChildRegister.attendanceList){
				if (a.getChildID() == this.childID && a.getDateOut() == null){
					return  true;
				}
			}
		}
		return false;
	}

	public Attendance getOpenAttendance() {
		if (ChildRegister.debug){System.out.println(ChildRegister.attendanceList.size());}
		if (ChildRegister.attendanceList.size()>0){
			for (Attendance a : ChildRegister.attendanceList){
				if (a.getChildID() == this.childID && a.getDateOut() == null){
					return  a;
				}
			}
		}
		return null;
	}

}

