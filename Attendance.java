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
import java.util.Calendar;
import java.util.Date;

public class Attendance  implements Serializable {
	public static int maxId = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int childID;
	private Calendar dateIn;
	private Calendar dateOut;
	private int attendanceID;
	private int minutes;
	private RateCard rate;
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

	public void setDateIn (Calendar date){
		this.dateIn = rounded(date);
	}
	public Calendar getDateIn (){
		return this.dateIn;
	}

	public void setDateOut (Calendar date){
		this.dateOut = date; 
		this.minutes = (int) ((this.dateOut.getTimeInMillis() - this.dateIn.getTimeInMillis()) / 1000 / 60);
	}

	public void setDateOut (){
		try{
			this.rate = RateCard.getApplicableRate(ChildRegister.rateCardList, this.childID, this.dateIn);
		}
		catch (Exception e){
			System.out.println ("No rate found for this child");
			return;
		}
		this.dateOut = rounded(Calendar.getInstance());
		this.minutes = (int) ((this.dateOut.getTimeInMillis() - this.dateIn.getTimeInMillis()) / 1000 / 60);
		this.minutes = (int) ((this.dateOut.getTimeInMillis() - this.dateIn.getTimeInMillis()) / 1000 / 60);
		this.cost = this.rate.hourRate * (minutes/60);
		if (ChildRegister.debug){System.out.println("minutes = "+this.minutes);}
		if (ChildRegister.debug){System.out.println("milliseconds = "+((this.dateOut.getTimeInMillis() - this.dateIn.getTimeInMillis())));}
	}

	public Calendar rounded (Calendar inCal){
		int mins = inCal.get(Calendar.MINUTE);
		int hours = inCal.get(Calendar.HOUR_OF_DAY);
		if (ChildRegister.debug){
			System.out.println("time for rounding (hh:mm) = "+hours+":"+mins);
		}
		int roundMins = 0;
		int roundHours = 0;
		Calendar roundDate = Calendar.getInstance();
		roundHours = hours;
		if (mins >= 0 && mins <= 7) {roundMins = 0;}  
		if (mins >= 8 && mins <= 22) {roundMins = 15;}  
		if (mins >= 23 && mins <= 37) {roundMins = 30;}  
		if (mins >= 38 && mins <= 52) {roundMins = 45;} 
		if (mins >= 53 ) {roundMins = 0; roundHours = hours + 1;} 

		try{
			if (ChildRegister.debug){
				System.out.println("year "+inCal.get(Calendar.YEAR));
				System.out.println("month "+inCal.get(Calendar.MONTH));
				System.out.println("day "+inCal.get(Calendar.DAY_OF_MONTH));
				System.out.println("rounded hours "+roundHours);
				System.out.println("rounded minutes "+roundMins); 
			}
			roundDate.set(inCal.get(Calendar.YEAR), inCal.get(Calendar.MONTH), inCal.get(Calendar.DAY_OF_MONTH), roundHours, roundMins);
		}
		catch (Exception e){
			System.out.println("failed to convert date "+inCal.get(Calendar.YEAR)+"|"+ inCal.get(Calendar.MONTH)+"|"+ inCal.get(Calendar.DAY_OF_MONTH)+"|"+ roundHours+"|"+ roundMins);
			System.out.println(e);
		}
		return roundDate;

	}

	public Calendar getDateOut (){
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
		ret.append(this.dateIn.get(Calendar.HOUR_OF_DAY));
		ret.append(":");
		ret.append(this.dateIn.get(Calendar.MINUTE));
		ret.append(", ");
		ret.append(this.dateIn.get(Calendar.DAY_OF_MONTH));
		ret.append("/");
		ret.append(this.dateIn.get(Calendar.MONTH)+1);
		ret.append("/");
		ret.append(this.dateIn.get(Calendar.YEAR));
		ret.append(" Date Out : ");
		if (dateOut == null){
			ret.append(" NULL ");
		}
		else {
			ret.append(this.dateOut.get(Calendar.HOUR_OF_DAY));
			ret.append(":");
			ret.append(this.dateOut.get(Calendar.MINUTE));
			ret.append(", ");
			ret.append(this.dateOut.get(Calendar.DAY_OF_MONTH));
			ret.append("/");
			ret.append(this.dateOut.get(Calendar.MONTH)+1);
			ret.append("/");
			ret.append(this.dateOut.get(Calendar.YEAR));
			ret.append(" Minutes elapsed : ");
			ret.append(this.minutes);
		}

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
		this.setDateIn(Calendar.getInstance());
	}

	public Attendance(int childID, Calendar dateIn){
		this(childID);		
		this.setDateIn(rounded(dateIn));
	}
	public Attendance(int childID, Calendar dateIn, Calendar dateOut){
		this(childID,dateIn);		
		this.setDateOut(rounded(dateOut));
	}


	public static void serializeAttendanceList(ArrayList<Attendance> attendanceIn ){

		try{

			FileOutputStream fout = new FileOutputStream(PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(attendanceIn);
			oos.close();
			if (ChildRegister.debug){System.out.println("Closed file in serializeAttendanceList");}
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

