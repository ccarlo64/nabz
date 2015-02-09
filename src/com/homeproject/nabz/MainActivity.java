package com.homeproject.nabz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
//import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
//public class MainActivity extends ActionBarActivity {
{
	private ListView lv;
	final Context context = this;  
//	String urlPlugin = null;
	DatabaseHelper databaseHelper = new DatabaseHelper(this);
    EditText etRisposta;
  //  EditText etRispostaPlugin;
    TextView tvSonoConnesso;
    TextView tvToken;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
    //    etRispostaPlugin = (EditText) findViewById(R.id.etRispostaPlugin);
        etRisposta = (EditText) findViewById(R.id.etRisposta);
        tvSonoConnesso = (TextView) findViewById(R.id.tvSonoConnesso);
        tvToken = (TextView) findViewById(R.id.textToken);
        
        lv = (ListView) findViewById(R.id.listView1);
        //String[] pi = new String[] {"Ambient (move ears)","Send text"};
       
        String[] pi = getResources().getStringArray(R.array.array_plugin); 
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pi);        
        lv.setAdapter(adapter);
        
        
        if(isConnected()){
        	//tvSonoConnesso.setBackgroundColor(0xFF00CC00);
        	tvSonoConnesso.setText(R.string.net_ok);//"connessione avvenuta...");
        }
        else{
        	tvSonoConnesso.setText(R.string.net_ko);//"problemi di connessione...");
        }

     // componi 
        String[] tmp=null;
		tmp = databaseHelper.getC();
        String urlo=null;
        //Log.d("mmm",tmp[2]);
        urlo = tmp[2] + "/ojn_api/accounts/auth?login=" + tmp[0] + "&pass=" + tmp[1];
        new HttpAsyncTask().execute( urlo );
        //Log.d("url",urlo);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
              
             // ListView Clicked item index
             final int itemPosition     = position;
             
             // ListView Clicked item value
             String  itemValue    = (String) lv.getItemAtPosition(position);
                
              // Show Alert 
             /*
              Toast.makeText(getApplicationContext(),
                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                .show();
                */              
    			Button dialogButton;
                //final String urlPlugin=null;
    			final Dialog dialog = new Dialog(context);
              
              switch (itemPosition) {
              case 0:
				// *************************************
				// ORECCHIE
				// *************************************
				
				dialog.setContentView(R.layout.plugin_ambient_ear);
				dialog.setTitle(R.string.plugin_title1);//"Ambient ears");
				dialogButton = (Button) dialog.findViewById(R.id.bntAmbientEar);
				SeekBar seekBarLeft = (SeekBar)dialog.findViewById(R.id.sbLeft); 
				final TextView seekBarValueLeft = (TextView)dialog.findViewById(R.id.textSeekBarSx); 
				
				seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
				   @Override 
				   public void onProgressChanged(SeekBar seekBarLeft, int progress, 
				     boolean fromUser) { 
				    // TODO Auto-generated method stub 
				    seekBarValueLeft.setText(String.valueOf(progress)); 
				   } 
				
				   @Override 
				   public void onStartTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				
				   @Override 
				   public void onStopTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				   });
				
				SeekBar seekBarRight = (SeekBar)dialog.findViewById(R.id.sbRight); 
				final TextView seekBarValueRight = (TextView)dialog.findViewById(R.id.textSeekBarDx); 
				seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
				   @Override 
				   public void onProgressChanged(SeekBar seekBarRight, int progress, 
				     boolean fromUser) { 
				    // TODO Auto-generated method stub 
				    seekBarValueRight.setText(String.valueOf(progress)); 
				   } 
				
				   @Override 
				   public void onStartTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				
				   @Override 
				   public void onStopTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				   }); 


				dialogButton.setOnClickListener(new OnClickListener() {
          		    @Override
          		    public void onClick(View v) {
          			   
					String[] tmp = databaseHelper.getC();
					String urlPlugin = tmp[2]; //server
					urlPlugin = urlPlugin + "/ojn_api/bunny/";
					urlPlugin = urlPlugin + tmp[3]; //mac
					urlPlugin = urlPlugin + "/packet/sendPacket?data=";
										
					String token = (String) tvToken.getText().toString();//null;
					TextView tv;
					tv = (TextView) dialog.findViewById(R.id.textSeekBarSx);
					String sbSx = (String) tv.getText().toString();
					tv = (TextView) dialog.findViewById(R.id.textSeekBarDx);
					String sbDx = (String) tv.getText().toString();
					sbSx = String.format( "%02X", Integer.parseInt(sbSx));
					//Integer.toHexString( Integer.parseInt(sbSx));
					sbDx = String.format( "%02X", Integer.parseInt(sbDx));
                      /*
                       * String.format("#%x", number)
                	  $pacchetto  = pack("C",0x7F);
                	  $pacchetto .= pack("C",0x04); //ambient
                	  $pacchetto .= pack("C",0x00);
                	  $pacchetto .= pack("C",0x00);
                	  $pacchetto .= pack("C",0x08);//lunghezza dati
                	  $pacchetto .= pack("C",0x7F);
                	  $pacchetto .= pack("C",0xFF);
                	  $pacchetto .= pack("C",0xFF);
                	  $pacchetto .= pack("C",0xFE);
                	  $pacchetto .= pack("C",0x04); //orecchio dx
                	  $pacchetto .= pack("C",$dx);
                	  $pacchetto .= pack("C",0x05); //orecchio sx
                	  $pacchetto .= pack("C",$sx);
                	  $pacchetto .= pack("C",0xFF);
                      */
					urlPlugin = urlPlugin + "7F040000087FFFFFFE04" + sbDx + "05" + sbSx + "FF";
					urlPlugin = urlPlugin + "&token=";
					urlPlugin = urlPlugin + token;
					urlPlugin = urlPlugin.replace(" ", "%20");
					new HttpAsyncTask().execute(urlPlugin);		       

					//Log.d("ear",urlPlugin);
//Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
          		}
				});
				dialog.show();              
				break;
// *************************************** ENDORECCHIE
  

              case 1:
        		// *************************************
         		// TESTO
        		// *************************************
        		dialog.setContentView(R.layout.plugin_send_text);
        		dialog.setTitle(R.string.plugin_title2);
        		dialogButton = (Button) dialog.findViewById(R.id.bntSendText);

             	dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
				String[] tmp = databaseHelper.getC();
				String urlPlugin = tmp[2]; //server
				urlPlugin = urlPlugin + "/ojn_api/bunny/";
				urlPlugin = urlPlugin + tmp[3]; //mac
				urlPlugin = urlPlugin + "/tts/say?text=";
				EditText et = (EditText) dialog.findViewById(R.id.edSendText);
				String tmpx = et.getText().toString();
				urlPlugin = urlPlugin + tmpx;
				
				String token = (String) tvToken.getText().toString();//null;
/*
	 		               if (token.length()<=1) {
				               String result = etRisposta.getText().toString();
				               Pattern pattern = Pattern.compile("<value>(.*?)</value>");
				               Matcher matcher = pattern.matcher(result);
				               while (matcher.find()) {
				               	token = matcher.group(1);
				               }
				               tvToken.setText(token);

						   }
*/
	
				urlPlugin = urlPlugin + "&token=";
				urlPlugin = urlPlugin + token;
				urlPlugin = urlPlugin.replace(" ", "%20");
				new HttpAsyncTask().execute(urlPlugin);		       
				
				//Log.d("text",urlPlugin);
//	              		        Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
	            dialog.dismiss();
	            }
	            });
	      		dialog.show();              
        		break;
            	  // ***************************************ENDTESTO
              case 2:
        		// *************************************
        		// Led respirazione
        		// *************************************

        		dialog.setContentView(R.layout.plugin_led_resipro);
        		dialog.setTitle(R.string.plugin_title3);
        		Spinner spinner = (Spinner) dialog.findViewById(R.id.spColori);
         	    //String[] colori = getResources().getStringArray(R.array.array_colori); 
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
        		        R.array.array_colori, android.R.layout.simple_spinner_item);
        		// Specify the layout to use when the list of choices appears
        		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		// Apply the adapter to the spinner
        		spinner.setAdapter(adapter);
        		  
        		dialogButton = (Button) dialog.findViewById(R.id.bntSendLedRespiro);

	         	dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
									
					String[] tmp = databaseHelper.getC();
					String urlPlugin = tmp[2]; //server
					urlPlugin = urlPlugin + "/ojn_api/bunny/";
					urlPlugin = urlPlugin + tmp[3]; //mac
								
					urlPlugin = urlPlugin + "/colorbreathing/setColor?name=";
										 
					Spinner spinner = (Spinner) dialog.findViewById(R.id.spColori);
					String tmpx = String.valueOf(spinner.getSelectedItem());
					String token = (String) tvToken.getText().toString();//null;
					
					urlPlugin = urlPlugin + tmpx;
					urlPlugin = urlPlugin + "&token=";
					urlPlugin = urlPlugin + token;
					urlPlugin = urlPlugin.replace(" ", "%20");
									   
					new HttpAsyncTask().execute(urlPlugin);		       
					
					//Log.d("text",urlPlugin);
				   
//				    Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				   }
				   });
    	  		  dialog.show();              
            	  break;
               default:
         			// *************************************
         			//
         			// *************************************


                    break;              
              }

            }
		});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
        	 // Create custom dialog object
            final Dialog dialog = new Dialog(context);
            // Include dialog.xml file
            dialog.setContentView(R.layout.about);
            // Set dialog title
            dialog.setTitle("About!");

            // set values for custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
            text.setText("Nabz version 0.1 :-) by carlo64");
            //ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
            //image.setImageResource(R.drawable.image0);

            dialog.show();
             
            Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
            // if decline button is clicked, close the custom dialog
            declineButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    dialog.dismiss();
                }
            });        	
        	
        }
        
        if (id == R.id.action_settings) {

        	
            //Toast.makeText(getApplicationContext(),"settaggi",Toast.LENGTH_LONG).show();  
    		// *************************************
    		//
    		// *************************************
    		final Dialog dialog = new Dialog(context);
    		dialog.setContentView(R.layout.custom);
    		dialog.setTitle(R.string.dialog_setting);

    		// set the custom dialog components - text, image and button
    		//TextView text = (TextView) dialog.findViewById(R.id.testo);
    		//text.setText("Android custom dialog example!");
    		//ImageView image = (ImageView) dialog.findViewById(R.id.image);
    		//image.setImageResource(R.drawable.ic_launcher);

    		String[] tmp=null;
    		tmp = databaseHelper.getC();
    		
    		EditText loginUtente= (EditText) dialog.findViewById(R.id.utente);
    		loginUtente.setText(tmp[0]);
    		
    		EditText loginPassword= (EditText) dialog.findViewById(R.id.password);
    		loginPassword.setText(tmp[1]);

    		EditText loginServer= (EditText) dialog.findViewById(R.id.server);
    		loginServer.setText(tmp[2]);

    		EditText loginMac= (EditText) dialog.findViewById(R.id.coniglio);
    		loginMac.setText(tmp[3]);
    		
    		
    		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
    		// if button is clicked, close the custom dialog
    		dialogButton.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				dialog.dismiss();
    			}
    		});

    		
    		Button dialogSalva = (Button) dialog.findViewById(R.id.salvaDati);
    		dialogSalva.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				//SQLiteDatabase db = databaseHelper.getWritableDatabase();
    				//databaseHelper.insertDb("carlo", "123456", "http://openznab.it");
    				String a1=null;
    				EditText loginUtente= (EditText) dialog.findViewById(R.id.utente);
    				a1 = loginUtente.getText().toString();
    				String a2=null;
    				EditText loginPassword= (EditText) dialog.findViewById(R.id.password);
    				a2 = loginPassword.getText().toString();
    				String a3=null;
    				EditText loginServer= (EditText) dialog.findViewById(R.id.server);
    				a3 = loginServer.getText().toString();
    				String a4=null;
    				EditText loginMac= (EditText) dialog.findViewById(R.id.coniglio);
    				a4 = loginMac.getText().toString();
    				databaseHelper.updateDati(a1,a2,a3,a4);

    				//Log.d("aggiornato","ok");
    				Toast.makeText(getApplicationContext(), R.string.update_ok, Toast.LENGTH_SHORT).show();						
    				//EditText etUtente = (EditText) dialog.findViewById(R.id.utente);
    				//String nomeUtente = (String) etUtente.getText().toString();
    				//Log.d("salvataggio",nomeUtente);
    				dialog.dismiss();
    			}
    		});
    		dialog.show();
            
            
            
            
            
            return true;     
        }
        return super.onOptionsItemSelected(item);
    }


  // connessione al web	
  //************************

      public static String GET(String url){
          InputStream inputStream = null;
          String result = "";
          try {
   
              // create HttpClient
              HttpClient httpclient = new DefaultHttpClient();
   
              // make GET request to the given URL
              HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
   
              // receive response as inputStream
              inputStream = httpResponse.getEntity().getContent();
   
              // convert inputstream to string
              if(inputStream != null)
                  result = convertInputStreamToString(inputStream);
              else
                  result = "Did not work!";
   
          } catch (Exception e) {
              Log.d("InputStream", e.getLocalizedMessage());
          }
   
          return result;
      }
      
      // convert inputstream to String
      private static String convertInputStreamToString(InputStream inputStream) throws IOException{
          BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
          String line = "";
          String result = "";
          while((line = bufferedReader.readLine()) != null)
              result += line;
   
          inputStream.close();
          return result;
   
      }
      
      // check network connection
      public boolean isConnected(){
          ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
              if (networkInfo != null && networkInfo.isConnected())
                  return true;
              else
                  return false;  
      }    
      
      private class HttpAsyncTask extends AsyncTask<String, Void, String> {
          @Override
          protected String doInBackground(String... urls) {
   
              return GET(urls[0]);
          }
          // onPostExecute displays the results of the AsyncTask.
          @Override
          protected void onPostExecute(String result) {
              //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
              etRisposta.setText(result);
              
//              Log.d("la risposta :",result);

              //Pattern pattern = Pattern.compile("<value>(.*?)</value>");
               String token = (String) tvToken.getText().toString();//null;
			   if (token.length()<=1) {

                  Pattern pattern = Pattern.compile("<value>(.*?)</value>");
                  Matcher matcher = pattern.matcher(result);

                  while (matcher.find()) {
//              	   Log.d("il token :",matcher.group(1));
	               tvToken.setText(matcher.group(1));


                  }
			   }
              
         }
      }    
  //*****************


}


