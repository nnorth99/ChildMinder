package childminder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class AccountEntry implements Serializable {
	//
	// Static variables 
	//
	private static final long serialVersionUID = 1L;
	private static final String PATH = "accountFile.sav";
	public static int maxId = 0;

	int carerID;
	float amount;
	LocalDate txnDate;
	int invoiceNumber;
	int attendanceID;
	String reference;
	
	public static void serializeAccountEntryList(ArrayList<AccountEntry> accountsIn ){

		try{

			if (accountsIn.size()==0) {	System.out.println("No account entries in list to serialize");
			}
			else {
				FileOutputStream fout = new FileOutputStream(PATH);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(accountsIn);
				oos.close();
				System.out.println("Closed file in serializeAccountEntryList");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

	public static ArrayList<AccountEntry> deserializeAccountEntryList( ){

		ArrayList<AccountEntry> returnList = new ArrayList<AccountEntry>();
		try{

			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);   

			ArrayList<AccountEntry> getList = (ArrayList<AccountEntry>) ois.readObject();
			returnList.addAll(getList);
			ois.close();

			if (returnList.size() == 0) {
				System.out.println("No Account Entries returned in the list");
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

	public static void clearCarerListFile(){
		ArrayList<AccountEntry> blankList = new ArrayList<AccountEntry>();
		serializeAccountEntryList(blankList);
	}


}
