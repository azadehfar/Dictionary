package com.adel.dictionary;

import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import cz.msebera.android.httpclient.Header;

import com.orm.SugarRecord;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button Fetch;
    Button History;
    EditText Word;
    TextView Result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind();
        Fetch.setOnClickListener((View.OnClickListener) this);
        History.setOnClickListener((View.OnClickListener) this);
    }

    public void bind() {
        Fetch = (Button) findViewById(R.id.Fetch);
        History = (Button) findViewById(R.id.History);
        Word = (EditText) findViewById(R.id.Word);
        Result = (TextView) findViewById(R.id.Result);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId() ) {
            case R.id.Fetch:

                CallDictionary(Word.getText().toString());

                break;
            case R.id.History:


                break;
            default:
                break;
        }


    }



    private void CallDictionary(String s) {


   AsyncTask<String, Integer, String> Async=  new CallbackTask().execute(dictionaryEntries(Word.getText().toString()));
   try {
     //  Toast.makeText(MainActivity.this, Async.get(), Toast.LENGTH_SHORT).show();
       Result.setText(onPostExecute(Async.get()));

       Date c = Calendar.getInstance().getTime();
       SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

       DBDictionary developer = new DBDictionary(Word.getText().toString(),df.format(c));
       developer.save();


   }
   catch (Exception e)
   {

   }


    }







    protected String onPostExecute(String result) {


        String def = "";
        try {
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject lentries = results.getJSONObject(i);
                JSONArray la = lentries.getJSONArray("lexicalEntries");
                for (int j = 0; j < la.length(); j++) {
                    JSONObject entries = la.getJSONObject(j);
                    JSONArray e = entries.getJSONArray("entries");
                    for (int k= 0; k < e.length(); k++) {
                        JSONObject senses = e.getJSONObject(k);
                        JSONArray s = senses.getJSONArray("senses");
                        JSONObject d = s.getJSONObject(0);
                        JSONArray de = d.getJSONArray("definitions");

                        JSONArray ex = d.getJSONArray("examples");

                        def  = def + "\n" +de.getString(0);

                     //   if (ex.getString(1).length()>0 )
                       //     def  = def + "\n" + "example:" + "\n" + ex.getString(1);

                    }
                }
            }
            Log.e("def", def);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("This will be my result",result);
        return def;
    }

    private String dictionaryEntries(String word) {
    final String language = "en";
    final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
    return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
}



}
