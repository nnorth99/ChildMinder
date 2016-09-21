package childminder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Attendance  implements Serializable {
	public static int maxId = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int childID;
	private Date dateIn;
	private Date dateOut;
	private int attendanceID;
	private int minutes;
	private static final String PATH = "attendanceFile.sav";

	public void setChildID (int ID){
		this.childID = ID;
	}
	public int getChildID (){
		return this.childID;
	}

	public void setMinutes (int minutes){
		this.minutes = minutes;
	}
	public int getMinutes (){
		return this.minutes;
	}

	public void setDateIn (Date date){
		this.dateIn = date;
	}
	public Date getDateIn (){
		return this.dateIn;
	}

	public void setDateOut (Date date){
		this.dateOut = date;
		this.minutes = (int) ((this.dateOut.getTime() - this.dateIn.getTime()) / 1000 / 60);
	}
	public void setDateOut (){
		this.dateOut = new Date();
		this.minutes = (int) ((this.dateOut.getTime() - this.dateIn.getTime()) / 1000 / 60);
		System.out.println("minutes = "+this.minutes);
		System.out.println("seconds = "+((this.dateOut.getTime() - this.dateIn.getTime())));

	}
	public Date getDateOut (){
		return this.dateOut;
	}
	public int getAttendanceID() {
		return attendanceID;
	}
	public void setAttendanceID(int attendanceID) {
		this.attendanceID = attendanceID;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(" Attendance ID : ");
		ret.append(this.attendanceID);
		ret.append(" Child ID : ");
		ret.append(this.childID);
		ret.append(" Date In : ");
		ret.append(this.dateIn);
		ret.append(" Date Out : ");
		ret.append(this.dateOut);
		ret.append(" Minutes elapsed : ");
		ret.append(this.minutes);

		return ret.toString();
	}


	//
	// Constructors
	//
	public Attendance(){
		maxId++;
		this.setAttendanceID(maxId);		
	}
	public Attendance(int childID){
		this();		
		this.setChildID(childID);
		this.setDateIn(new Date());
	}
	public Attendance(int childID, Date dateIn){
		this(childID);		
		this.setDateIn(dateIn);
	}
	public Attendance(int childID, Date dateIn, Date dateOut){
		this(childID,dateIn);		
		this.setDateOut(dateOut);
	}


	public static void serializeAttendanceList(ArrayList<Attendance> attendanceIn ){

		try{

			FileOutputStream fout = new FileOutputStream(PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(attendanceIn);
			oos.close();
			System.out.println("Closed file in serializeChildList");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static ArrayList<Attendance> deserializeAttendanceList( ){

		ArrayList<Attendance> returnList = new ArrayList<Attendance>();
		try{

			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);   

			ArrayList<Attendance> getList = (ArrayList<Attendance>) ois.readObject();
			returnList.addAll(getList);
			ois.close();

			if (returnList.size() == 0) {
				System.out.println("No Attendances returned in the list");
			}
			else {
				System.out.println("first record from deserialized list : "+returnList.get(0));
				System.out.println("total records read= "+returnList.size());
			}

			maxId = returnList.size();
		} 
		catch (FileNotFoundException e) {
			// file doesn't exist yet - first pass, ignore exception and allow empty list to be returned
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}
	public static void clearAttendanceListFile(){
		ArrayList<Attendance> blankList = new ArrayList<Attendance>();
		serializeAttendanceList(blankList);
	}

}

