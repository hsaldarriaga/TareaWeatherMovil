package com.movil.hsaldarriaga.weather;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrador on 23/04/2015.
 */
public class WeatherLector {

    public WeatherLector(Activity c)
    {
        this.c = c;
        URLName = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Barranquilla&units=metric&cnt=6";
    }

    public void Connect(final FinishCallBack callback) {
        if (hasInternet()) {
            Thread  t = new Thread( new Runnable() {
                @Override
                public void run() {
                    String content = "";
                    resultado = false;
                    HttpURLConnection con = null;
                    try {
                        URL rute = new URL(URLName);
                        con = (HttpURLConnection) rute.openConnection();
                        InputStream in = new BufferedInputStream(con.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line = reader.readLine();
                        while (line != null) {
                            content += line;
                            line = reader.readLine();
                        }
                        reader.close();
                        obj = new JSONObject(content);
                        resultado = true;

                    } catch (IOException e) {
                    } catch (JSONException e) {
                    } finally {
                        if (con != null)
                            con.disconnect();
                        c.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.OnFinish(resultado);
                            }
                        });
                    }
                }
            });
            t.start();
        } else {
            Toast.makeText(c, "There isn't Internet Connection,", Toast.LENGTH_LONG).show();
            callback.OnFinish(false);
        }
    }

    private boolean hasInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Weather getCurrentWeather()
    {
        if (obj!= null)
        {
            try {
                JSONArray list = obj.getJSONArray("list");
                JSONObject temp = list.getJSONObject(0).getJSONObject("temp");
                Weather w = new Weather(temp.getDouble("day"), temp.getDouble("min"), temp.getDouble("max"), temp.getDouble("night"), temp.getDouble("eve"), temp.getDouble("morn"));
                return w;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public List<Weather> getFiveDaysForecast()
    {
        List<Weather> lista = new ArrayList<Weather>();
        if (obj!= null)
        {
            try {
                JSONArray list = obj.getJSONArray("list");
                for (int i = 1; i < list.length(); i++)
                {
                    JSONObject temp = list.getJSONObject(i).getJSONObject("temp");
                    Weather w = new Weather(temp.getDouble("day"), temp.getDouble("min"), temp.getDouble("max"), temp.getDouble("night"), temp.getDouble("eve"), temp.getDouble("morn"));
                    lista.add(w);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    public final String URLName;
    public JSONObject obj = null;
    private boolean resultado = false;
    private final Activity c;
}
