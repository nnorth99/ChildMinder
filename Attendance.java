package childminder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Attendance  implements Serializable {
	public static int maxId = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm"); 
	private int childID;
	private Date dateIn;
	private Date dateOut;
	private int attendanceID;
	private int minutes;
	private Double rate;
	private Double cost;
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
		this.dateOut = rounded(new Date());
		this.minutes = (int) ((this.dateOut.getTime() - this.dateIn.getTime()) / 1000 / 60);
		this.minutes = (int) ((this.dateOut.getTime() - this.dateIn.getTime()) / 1000 / 60);
		this.rate = RateCard.getApplicableRate(ChildRegister.rateCardList, this.childID, this.dateIn).hourRate;
		this.cost = this.rate * (minutes/60);
		if (ChildRegister.debug){System.out.println("minutes = "+this.minutes);}
		if (ChildRegister.debug){System.out.println("seconds = "+((this.dateOut.getTime() - this.dateIn.getTime())));}
	}

	@SuppressWarnings("deprecation")
	public Date rounded (Date date){
		int mins = date.getMinutes();
		int hours = date.getHours();
		int roundMins = 0;
		int roundHours = 0;
		Date roundDate = null;
		roundHours = hours;
		if (mins >= 0 && mins <= 7) {roundMins = 0;}  
		if (mins >= 8 && mins <= 22) {roundMins = 15;}  
		if (mins >= 23 && mins <= 37) {roundMins = 30;}  
		if (mins >= 38 && mins <= 52) {roundMins = 45;} 
		if (mins >= 53 ) {roundMins = 45; roundHours = hours + 1;} 
		
		try{
			roundDate = sdf.parse(dateOut.getYear()+"-"+dateOut.getMonth()+"-"+dateOut.getDay()+" "+roundHours+":"+roundMins);
		}
		catch (Exception e){System.out.println("failed to convert date "+dateOut.getYear()+"-"+dateOut.getMonth()+"-"+dateOut.getDay()+" "+roundHours+":"+roundMins);
		}
		return roundDate;
		
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
			if (ChildRegister.debug){System.out.println("Closed file in serializeChildList");}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void ListAttendances (ArrayList<Attendance> list){
		System.out.println("");
		System.out.println("===============");
		System.out.println("= Attendances =");
		System.out.println("===============");
		for (Attendance att : list){
			System.out.println(att);
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
				if (ChildRegister.debug){System.out.println("first record from deserialized list : "+returnList.get(0));}
				if (ChildRegister.debug){System.out.println("total records read= "+returnList.size());}
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

