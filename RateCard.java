package childminder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class RateCard  implements Serializable {

	//
	// Static variables 
	//
	private static final long serialVersionUID = 1L;

	//
	// Local class variables 
	//
	int childID;
	float hourRate;
	Date startDate;
	Date endDate;


	//
	// Getters and setters 
	//
	public void setChildID (int ID){
		this.childID = ID;
	}
	public int getChildID (){
		return this.childID;
	}

	public void setHourRate (float hourRate){
		this.hourRate = hourRate;
	}
	public float getHourRate (){
		return this.hourRate;
	}

	public void setStartDate (Date startDate){
		this.startDate = startDate;
	}
	public Date getStartDate (){
		return this.startDate;
	}

	public void setEndDate (Date endDate){
		this.endDate = endDate;
	}
	public Date getEndDate (){
		return this.endDate;
	}
	public static void serializeRateCardList(ArrayList<RateCard> rateIn ){

		try{

			String path = "C:" + File.separator + "ChildMinderSaveFiles" + File.separator + "rateFile.sav";
			path = "rateFile.sav";

			if (rateIn.size()==0) {		System.out.println("No attendance records in list to serialize");
			}
			else {
				FileOutputStream fout = new FileOutputStream(path);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(rateIn);
				oos.close();
				System.out.println("Closed file in serializeChildList");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
