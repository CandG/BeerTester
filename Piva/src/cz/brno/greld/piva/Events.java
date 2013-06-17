package cz.brno.greld.piva;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Events {
	private ArrayList<Event> events = new ArrayList<Event>();
	private static final String VALUE_OF_CHECK = "krucifix";

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void loadEvents() throws ConnectivityExeption, MistakeInJSONException{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		JSONArray jArray = DBWorker.dbQuery(nameValuePairs, "loadEvents.php", VALUE_OF_CHECK);
		if (jArray == null)
			throw new NullPointerException("loadEvents: jArray je null");

		JSONObject json_data;
		events.clear();
		
		if (jArray.optJSONObject(0) != null && jArray.optJSONObject(0).optBoolean("empty") == true)
			return;
		
		try {
			for(int i=0;i<jArray.length();i++){
				json_data = jArray.getJSONObject(i);
				events.add(new Event(json_data.getInt("id"), json_data.getString("name"), 
						json_data.getString("place"), json_data.getString("time")));
				
			}
		
		} catch (JSONException e) {
			throw new MistakeInJSONException("loadEvents: Chyba pri parsovani", e);
		}
		
	}
	
	public String[] getStrings(){
		String [] result = new String [events.size()];
		int i = 0;
		for (Event event: events){
			result[i] = event.getName();
			i++;
		}
		return result;
	}
}
