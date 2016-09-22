package childminder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class RateCard  implements Serializable {


	//
	// Static variables 
	//
	private static final long serialVersionUID = 1L;
	private static final String PATH = "rateFile.sav";

	//
	// Local class variables 
	//
	int childID;
	float hourRate;
	LocalDate startDate;
	LocalDate endDate;

	//
	// constructors
	//
	RateCard (){
	}

	RateCard (float rate, LocalDate startDate){
		this.setChildID(0);
		this.setHourRate(rate);
		this.setStartDate(startDate);
	}

	RateCard (int childID, float rate, LocalDate startDate){
		this(rate, startDate);
		this.setChildID(childID);
	}

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

	public void setStartDate (LocalDate startDate){
		this.startDate = startDate;
	}
	public LocalDate getStartDate (){
		return this.startDate;
	}

	public void setEndDate (LocalDate endDate){
		this.endDate = endDate;
	}
	public LocalDate getEndDate (){
		return this.endDate;
	}

	@Override
	public String toString() {
		return new StringBuffer(" ID : ")
				.append(this.childID)
				.append(" rate: ")
				.append(this.hourRate)
				.append(" start date :")
				.append(this.startDate)
				.append(" end date :")
				.append(this.endDate).toString();
	}

	public static void serializeRateCardList(ArrayList<RateCard> rateIn ){

		try{

			String path = "C:" + File.separator + "ChildMinderSaveFiles" + File.separator + "rateFile.sav";
			path = "rateFile.sav";

			if (rateIn.size()==0) {		System.out.println("No rates in list to serialize");
			}
			else {
				FileOutputStream fout = new FileOutputStream(path);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(rateIn);
				oos.close();
				System.out.println("Closed file in serializeRateCardList");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void createNewRate  (int childID, float rate, LocalDate startDate, ArrayList<RateCard> rateArray){

		// close off old default rate where present
		for (RateCard rateCard : rateArray ){
			if (rateCard.childID == childID && rateCard.endDate == null){
				// found blank one - close it off
				int rateIndex = rateArray.indexOf(rateCard);
				rateCard.setEndDate(startDate.minusDays(1));
				rateArray.set(rateIndex, rateCard);
			}
		}

		// add new default rate
		RateCard defaultRate = new RateCard (childID,rate, startDate); 
		rateArray.add(defaultRate);

	}

	public static void createNewRate  (float rate, LocalDate startDate, ArrayList<RateCard> rateArray){
		// create the default rate if no child supplied
		createNewRate (0, rate, startDate, rateArray);
	}

	public static void setDefaultRate (float rate, LocalDate startDate, ArrayList<RateCard> rateArray){
		createNewRate (0, rate, startDate, rateArray); 

	}

	public static ArrayList<RateCard> deserializeRateCardList( ){

		ArrayList<RateCard> returnList = new ArrayList<RateCard>();
		try{
			System.out.println("deserialize rates");

			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);   

			ArrayList<RateCard> getList = (ArrayList<RateCard>) ois.readObject();
			returnList.addAll(getList);
			ois.close();

			System.out.println("total rate records read= "+returnList.size());
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

}
