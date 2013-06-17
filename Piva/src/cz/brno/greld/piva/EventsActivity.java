package cz.brno.greld.piva;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Added options for player
 * @author Jan Kucera
 *
 */
public class EventsActivity extends ListActivity {
	
	private Events events;
	private MyApplication app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  app = ((MyApplication)getApplicationContext());
	  events = app.getEvents();

	  setListAdapter(new ArrayAdapter<String>(this, R.layout.option_item, events.getStrings()));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

		
		
	  lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			app.setEvent(events.getEvents().get(position));
			
			Intent intent = new Intent();
	        intent.setClass(getApplicationContext(), TestingActivity.class);
	        startActivity(intent);
		}
	  });
	}
	
    
}
