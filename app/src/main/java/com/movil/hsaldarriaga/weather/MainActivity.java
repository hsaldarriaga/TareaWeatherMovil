package com.movil.hsaldarriaga.weather;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements FinishCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        lector = new WeatherLector(this);
        lector.Connect(this);
    }

    @Override
    public void OnFinish(boolean successful) {
        if (successful)
        {
            progress.setVisibility(View.INVISIBLE);
            SetCurrentWeather();
            Forecast();
        }
    }

    public void SetCurrentWeather()
    {
        Weather w = lector.getCurrentWeather();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        TextView tv = (TextView)findViewById(R.id.TxTempHeader);
        tv.setText(getResources().getString(R.string.TempHeader) + " " + formattedDate);

        tv = (TextView)findViewById(R.id.Txeven);
        tv.setText(getResources().getString(R.string.Evenlabel) + " " + w.getEve());

        tv = (TextView)findViewById(R.id.TxMax);
        tv.setText(getResources().getString(R.string.maxlabel) + " " + w.getMax());

        tv = (TextView)findViewById(R.id.TxMin);
        tv.setText(getResources().getString(R.string.minlabel) + " " + w.getMin());

        tv = (TextView)findViewById(R.id.TxDay);
        tv.setText(getResources().getString(R.string.DayLabel) + " " + w.getDay());

        tv = (TextView)findViewById(R.id.TxNight);
        tv.setText(getResources().getString(R.string.nightlabel) + " " + w.getNight());

        tv = (TextView)findViewById(R.id.TxTomw);
        tv.setText(getResources().getString(R.string.tomorrowlabel) + " " + w.getMorn());
    }
    public void Forecast()
    {
        List<Weather> lista = lector.getFiveDaysForecast();
        LinearLayout container = (LinearLayout)findViewById(R.id.Scrollcontainer);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        for (Weather w : lista)
        {
            View v = LayoutInflater.from(this).inflate(R.layout.item_weather, null);
            c.add(Calendar.DAY_OF_MONTH, 1);
            String formattedDate = df.format(c.getTime());

            TextView tv = (TextView)v.findViewById(R.id.TxTempHeaderI);
            tv.setText("Temperatura " + formattedDate);

            tv = (TextView)v.findViewById(R.id.TXEvenI);
            tv.setText(getResources().getString(R.string.Evenlabel) + " " + w.getEve());

            tv = (TextView)v.findViewById(R.id.TxMaxI);
            tv.setText(getResources().getString(R.string.maxlabel) + " " + w.getMax());

            tv = (TextView)v.findViewById(R.id.TxMinI);
            tv.setText(getResources().getString(R.string.minlabel) + " " + w.getMin());

            tv = (TextView)v.findViewById(R.id.TxDayI);
            tv.setText(getResources().getString(R.string.DayLabel) + " " + w.getDay());

            tv = (TextView)v.findViewById(R.id.TxNightI);
            tv.setText(getResources().getString(R.string.nightlabel) + " " + w.getNight());

            tv = (TextView)v.findViewById(R.id.TxTmwI);
            tv.setText(getResources().getString(R.string.tomorrowlabel) + " " + w.getMorn());
            container.addView(v);
        }
    }

    private ProgressBar progress;
    private WeatherLector lector;
}
