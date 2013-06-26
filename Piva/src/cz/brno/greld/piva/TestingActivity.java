package cz.brno.greld.piva;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestingActivity extends Activity {
	
	private Event event;
	private int choosenBeer;
	private int choosenTester;
    private int mark;
    private String comment;
	private MyApplication app;
	private static final int LOADED_MSG = 1;
	private static final int NO_NETWORK_MSG = 101;
	private static final int STARTTURN_MSG = 2;
	private Thread threadLoading;
	public Handler loadingHandler;
	
	  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		app = ((MyApplication)getApplicationContext());
		event = app.getEvent();
		
		Runnable loading = new Runnable(){
            public void run() {
            	Looper.prepare();
            	startTurn();
            	
            	loadingHandler = new Handler() {
                    @Override
            		public void handleMessage(Message msg) {
                    	switch(msg.what)
                  	  {
                  	   case STARTTURN_MSG:
                  		   startTurn();
                  		   break;
                  	   
                  	  }
                    }
            	};
            	Looper.loop();
            }
        };
        threadLoading =  new Thread(null, loading, "loading");
        threadLoading.start();
	}
	
	public Handler testingActivityHandler = new Handler() {
        @Override
		public void handleMessage(Message msg) {
        	switch(msg.what)
      	  {
      	   case LOADED_MSG:
      		   showVoting();
      		   break;
	   	   case NO_NETWORK_MSG:
	   		   	setContentView(R.layout.no_network);
	   	   
      	  }
        }
    };
    
    
    /**
     * if there is Internet connectivity available
     * @return true if there is connectivity, false if not
     */
    private boolean checkConnectivity(){
    	ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (connectivityManager.getActiveNetworkInfo() == null){

    		testingActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);
            
            return false;
        }
        return true;
    }
    
    /**
     * if there is Internet connectivity available
     * @return true if there is connectivity, false if not
     */
    private void startTurn(){
    	if (checkConnectivity()){
        	try {
        		
        		if (choosenBeer != 0 && event.getState()==2){
        			event.saveVote(choosenTester,choosenBeer, mark, comment);
        			choosenBeer = 0;
        			mark = 0;
        		}
        		
        		if (choosenBeer != 0 && event.getState()==1){
        			event.saveNewRound(choosenBeer);
        			choosenBeer = 0;
        			mark = 0;
        		}
        		
				event.loadTurn();
				testingActivityHandler.sendEmptyMessage(LOADED_MSG);
            	
			} catch (ConnectivityExeption e) {
				System.err.println("loading - ConnectivityExeption: " + e.getMessage());
				testingActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);
			} catch (MistakeInJSONException e) {
				System.err.println("loading - MistakeInJSONException: " + e.getMessage());
				testingActivityHandler.sendEmptyMessage(NO_NETWORK_MSG);
			}
    	}
    }
    
    
    /**
     * fill the form
     * @return nothing
     */
    private void showVoting(){
    	if (event.getState()==3){
       	 	setContentView(R.layout.end);
    	}
       	else {
	    	setContentView(R.layout.next_vote); 
	    	TextView textViewX = (TextView) findViewById(R.id.textView2);
			textViewX.setText(event.getPosOfTestingBeer() + ". pivo");
	    	
	    	
			Button sendButtonHodnot = (Button) findViewById(R.id.hodnot);
			sendButtonHodnot.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						event.loadTurn();
					} catch (ConnectivityExeption e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MistakeInJSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (event.getState() != 2)
						Toast.makeText(getApplicationContext(), "Toto pivo je již odhlasovane!", Toast.LENGTH_SHORT).show();
					else {
						setContentView(R.layout.vote); 
	
		    			
		    			SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
		    			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
							
							public void onProgressChanged(SeekBar seekBar, int progress,
									boolean fromUser) {
								TextView textView2 = (TextView) findViewById(R.id.mark);
								textView2.setText(String.valueOf(progress));
								mark = progress;
							}
	
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
	
							public void onStopTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub
								
							}
						});
		    			
		    			
		    			final CharSequence[] items3 = event.getTestersNames();
		   		   			
		    			Button testersButton2 = (Button) findViewById(R.id.chooseTester);
		    			testersButton2.setOnClickListener(new OnClickListener() {
		    				public void onClick(View v) {
		    					AlertDialog.Builder builder = new AlertDialog.Builder(TestingActivity.this);
		    	    			builder.setTitle("Zvol testera");
		    	    			builder.setItems(items3, new DialogInterface.OnClickListener() {
	
									public void onClick(DialogInterface dialog, int item) {
		    	    			        choosenTester = event.getTesters().get(item).getId();
		    	    			        TextView textView = (TextView) findViewById(R.id.jmeno);
		    	    	    			textView.setText(event.getTesters().get(item).getName());
		    	    			    }
		    	    			});
		    	    			builder.show();
		    				}
		    			});
		    			
		    			final CharSequence[] items2 = event.getBeersNames();
	
		    			Button beersButton2 = (Button) findViewById(R.id.choose);
		    			beersButton2.setOnClickListener(new OnClickListener() {
		    				public void onClick(View v) {
		    					AlertDialog.Builder builder = new AlertDialog.Builder(TestingActivity.this);
		    	    			builder.setTitle("Zvol znaèku piva");
		    	    			builder.setItems(items2, new DialogInterface.OnClickListener() {
	
									public void onClick(DialogInterface dialog, int item) {
		    	    			        choosenBeer = event.getBeers().get(item).getId();
		    							TextView textView2 = (TextView) findViewById(R.id.beer);
		    							textView2.setText(event.getBeers().get(item).getName());
		    	    			    }
		    	    			});
		    	    			builder.show();
		    				}
		    			});
		    			
		    			Button sendButton2 = (Button) findViewById(R.id.send);
		    			sendButton2.setOnClickListener(new OnClickListener() {
		    				public void onClick(View v) {
		    					if (choosenBeer == 0)
		    						Toast.makeText(getApplicationContext(), "Nevybral jsi znaèku piva!", Toast.LENGTH_SHORT).show();
		    					else if (choosenTester == 0)
		    						Toast.makeText(getApplicationContext(), "Nevybral jsi testovaèe!", Toast.LENGTH_SHORT).show();
		    					else {
		    						EditText editText = (EditText) findViewById(R.id.comment);
		    						comment = editText.getText().toString();
		    						setContentView(R.layout.loading);
		    						loadingHandler.sendEmptyMessage(STARTTURN_MSG);
		    					}
		    				}
		    			});
		    			
					}
				}
			});
			Button sendButtonNalevac = (Button) findViewById(R.id.nalevac);
			sendButtonNalevac.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						event.loadTurn();
					} catch (ConnectivityExeption e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MistakeInJSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (event.getState() != 1)
						Toast.makeText(getApplicationContext(), "Poèkej až všeci odhlasuju!", Toast.LENGTH_SHORT).show();
					else {
						setContentView(R.layout.loop); 
		    			
				    	final CharSequence[] items = event.getBeersNames();
				
						Button beersButton = (Button) findViewById(R.id.choose);
						beersButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								AlertDialog.Builder builder = new AlertDialog.Builder(TestingActivity.this);
				    			builder.setTitle("Zvol znaèku piva");
				    			builder.setItems(items, new DialogInterface.OnClickListener() {
				
									public void onClick(DialogInterface dialog, int item) {
				    			        choosenBeer = event.getBeers().get(item).getId();
										TextView textView2 = (TextView) findViewById(R.id.beer);
										textView2.setText(event.getBeers().get(item).getName());
				    			    }
				    			});
				    			builder.show();
							}
						});
						
						Button sendButton = (Button) findViewById(R.id.send);
						sendButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (choosenBeer == 0)
									Toast.makeText(getApplicationContext(), "Nevybral jsi znaèku piva!", Toast.LENGTH_SHORT).show();
								else {
									setContentView(R.layout.loading);
									loadingHandler.sendEmptyMessage(STARTTURN_MSG);
								}
							}
						});
					}
				}
			});
			
	
       	 
    	}
    }
}
