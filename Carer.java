package childminder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Carer  implements Serializable {

	int ID;
	String name;
	String homeTel;
	String mobileTel;
	String alternateTel;

	public static int maxId = 0;
	private static final long serialVersionUID = 1L;
	private static final String PATH = "carerFile.sav";
	

	public static void serializeCarerList(ArrayList<Carer> carerIn ){

		try{

			FileOutputStream fout = new FileOutputStream(PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(carerIn);
			oos.close();
			System.out.println("Closed file in serializeCarerList");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static ArrayList<Carer> deserializeCarerList(){

		ArrayList<Carer> returnList = new ArrayList<Carer>();
		try{

			FileInputStream fin = new FileInputStream(PATH);
			ObjectInputStream ois = new ObjectInputStream(fin);   

			ArrayList<Carer> getList = (ArrayList<Carer>) ois.readObject();
			returnList.addAll(getList);
			ois.close();

			if (returnList.size() == 0) {
				System.out.println("No Carers returned in the list");
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
		ArrayList<Carer> blankList = new ArrayList<Carer>();
		serializeCarerList(blankList);
	}

}
