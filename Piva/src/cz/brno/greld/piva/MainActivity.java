package cz.brno.greld.piva;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {

	private static final int LOADED_MSG = 1;
	private static final int NO_NETWORK_MSG = 101;
	private MyApplication app;
	private Events events;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Runnable loading = new Runnable() {
			public void run() {
				if (checkConnectivity()) {
					events = new Events();
					try {

						events.loadEvents();
						mainActivityHandler.sendEmptyMessage(LOADED_MSG);

					} catch (ConnectivityExeption e) {
						System.err.println("loading - ConnectivityExeption: "
								+ e.getMessage());
						mainActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);
					} catch (MistakeInJSONException e) {
						System.err.println("loading - MistakeInJSONException: "
								+ e.getMessage());
						mainActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);
					}
				}
			}
		};
		Thread threadLoading = new Thread(null, loading, "loading");
		threadLoading.start();

	}

	public Handler mainActivityHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADED_MSG:
				app = ((MyApplication) getApplicationContext());
				app.setEvents(events);
				showEvents();
				break;
			case NO_NETWORK_MSG:
				setContentView(R.layout.no_network);

			}
		}
	};

	/**
	 * if there is Internet connectivity available
	 * 
	 * @return true if there is connectivity, false if not
	 */
	private boolean checkConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getActiveNetworkInfo() == null) {

			mainActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);

			return false;
		}
		return true;
	}

	/**
	 * open EventsActivity
	 */
	private void showEvents() {
		Intent intent = new Intent();

		intent.setClass(this.getApplicationContext(), EventsActivity.class);
		startActivity(intent);
		MainActivity.this.finish();
	}
}