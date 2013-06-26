package cz.brno.greld.piva;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	private int id;
	private String where;
	private String when;
	private String name;
	private int idOfTestingBeer;
	private int posOfTestingBeer;
	private int idOfTester;
	private String nameOfTester;
	/** state:
	 * 
	 * 1 - nalivac na tahu
	 * 2 - tester na tahu
	 * 3 - konec udalosti 
	 * 
	 **/
	private int state;
	private ArrayList<Beer> beers = new ArrayList<Beer>();
	private ArrayList<Tester> testers = new ArrayList<Tester>();
	private static final String VALUE_OF_CHECK = "krucifix";
	
	public Event(int id, String name, String where, String when) {
		super();
		this.id = id;
		this.name = name;
		this.where = where;
		this.when = when;
	
	}
	
	

	public int getPosOfTestingBeer() {
		return posOfTestingBeer;
	}



	public void setPosOfTestingBeer(int posOfTestingBeer) {
		this.posOfTestingBeer = posOfTestingBeer;
	}



	public int getIdOfTester() {
		return idOfTester;
	}
	
	public int getIdOfTestingBeer() {
		return idOfTestingBeer;
	}



	public String getNameOfTester() {
		return nameOfTester;
	}



	public int getState() {
		return state;
	}



	public void setBeers(ArrayList<Beer> beers) {
		this.beers = beers;
	}



	public void setTesters(ArrayList<Tester> testers) {
		this.testers = testers;
	}



	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getWhere() {
		return where;
	}

	public String getWhen() {
		return when;
	}

	public ArrayList<Beer> getBeers() {
		return beers;
	}

	public ArrayList<Tester> getTesters() {
		return testers;
	}
	
	
	public void loadBeers() throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "loadBeers.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("loadBeers: jArray je null");

		JSONObject json_data;
		beers.clear();
		
		if (jArray.optJSONObject(0) != null && jArray.optJSONObject(0).optBoolean("empty") == true)
			return;
		
		try {
			for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				beers.add(new Beer(json_data.getInt("id"), json_data.getString("name")));
			}
		
		} catch (JSONException e) {
			throw new MistakeInJSONException("loadBeers: Chyba pri parsovani", e);
		}
		
	}
	
	
	public void loadTesters() throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "loadTesters.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("loadTesters: jArray je null");

		JSONObject json_data;
		testers.clear();
		
		if (jArray.optJSONObject(0) != null && jArray.optJSONObject(0).optBoolean("empty") == true)
			return;
		
		try {
			for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				testers.add(new Tester(json_data.getInt("id"),json_data.getString("name")));
			}
		
		} catch (JSONException e) {
			throw new MistakeInJSONException("loadTesters: Chyba pri parsovani", e);
		}
		
	}
	
	public void loadTurn() throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "loadTurn.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("loadTurn: jArray je null");

		JSONObject json_data;
		beers.clear();
				
		try {
			for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				if (i==0){
					state = json_data.getInt("state");
					posOfTestingBeer =json_data.getInt("posOfTestingBeer");
					if (state == 2){						
						idOfTestingBeer = json_data.getInt("idOfTestingBeer");
						idOfTester = json_data.getInt("idOfTester");
						nameOfTester = json_data.getString("nameOfTester");
					}
				}
				if (state == 1 || state == 2)
					beers.add(new Beer(json_data.getInt("idBeerOption"), json_data.getString("nameBeerOption")));
			}
		
		} catch (JSONException e) {
			throw new MistakeInJSONException("loadTurn: Chyba pri parsovani", e);
		}
		
		try {
			loadTesters();
		} catch (ConnectivityExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MistakeInJSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	public CharSequence[] getBeersNames() {
		CharSequence[] result = new String[beers.size()];
		int i = 0;
		for (Beer beer: beers){
			result[i] = beer.getName();
			i++;
		}
		return result;
	}
	
	public CharSequence[] getTestersNames() {
		CharSequence[] result = new String[testers.size()];
		int i = 0;
		for (Tester tester: testers){
			result[i] = tester.getName();
			i++;
		}
		return result;
	}



	public void saveVote(int chosenTester,int choosenBeer, int mark, String comment) throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("idOfEvent", String.valueOf(id)));
		nameValuePairs.add(new BasicNameValuePair("idOfTestingBeer", String.valueOf(idOfTestingBeer)));
		nameValuePairs.add(new BasicNameValuePair("idOfTester", String.valueOf(chosenTester)));
		nameValuePairs.add(new BasicNameValuePair("choosenBeer", String.valueOf(choosenBeer)));
		nameValuePairs.add(new BasicNameValuePair("mark", String.valueOf(mark)));
		nameValuePairs.add(new BasicNameValuePair("comment", comment));

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "saveVote.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("saveVote: jArray je null");

				
		try {
			if (!jArray.getJSONObject(0).getBoolean("ok"))
				System.err.println("Nepodaøilo se uložit hlas.");
		} catch (JSONException e) {
			throw new MistakeInJSONException("saveVote: Chyba pri parsovani", e);
		}
		
	}



	public void saveNewRound(int choosenBeer) throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("idOfEvent", String.valueOf(id)));
		nameValuePairs.add(new BasicNameValuePair("choosenBeer", String.valueOf(choosenBeer)));

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "saveNewRound.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("saveNewRound: jArray je null");

				
		try {
			if (!jArray.getJSONObject(0).getBoolean("ok"))
				System.err.println("Nepodaøilo se uložit otevøení nového piva.");
		} catch (JSONException e) {
			throw new MistakeInJSONException("saveNewRound: Chyba pri parsovani", e);
		}
		
	}
	
	
}
