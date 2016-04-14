package com.example.m14x.weatherappvolley;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.m14x.weatherappvolley.Model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText cityName;
    TextView weatherInformation;
    Button weatherButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = (EditText) findViewById(R.id.cityName);
        weatherButton = (Button) findViewById(R.id.weatherButton);
        weatherInformation = (TextView) findViewById(R.id.weatherInformation);
        weatherButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.weatherButton:
                String city = null;
                String url = null;
                try {
                    city = URLEncoder.encode(cityName.getText().toString(),"UTF-8");
                    url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=2a72b61ea7a95fbb9d480ce2fdb50c02";
                    RequestQueue queue = Volley.newRequestQueue(this);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Weather weather = new Weather();
                                    try {

                                        JSONArray jsonArray = response.getJSONArray("weather");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            weather.setMain(jsonObject.getString("main"));
                                            weather.setDescription(jsonObject.getString("description"));
                                            String main = weather.getMain();
                                            String description = weather.getDescription();
                                            weatherInformation.setText(main + ": " + description);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    queue.add(jsonRequest);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("weather", weatherInformation.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        weatherInformation.setText(savedInstanceState.getString("weather"));
    }


}
