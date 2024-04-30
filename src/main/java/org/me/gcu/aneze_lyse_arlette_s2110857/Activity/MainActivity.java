package org.me.gcu.aneze_lyse_arlette_s2110857.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.me.gcu.aneze_lyse_arlette_s2110857.Model.BbcWeatherXmlParser;
import org.me.gcu.aneze_lyse_arlette_s2110857.Model.WeatherDataItem;
import org.me.gcu.aneze_lyse_arlette_s2110857.R;
import org.me.gcu.aneze_lyse_arlette_s2110857.Adapter.ForecastAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String glasgowUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
    private String londonUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743";
    private String newYorkUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581";
    private String omanUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286";
    private String mauritiusUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154";
    private String bangladeshUrl = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up title
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("3-Day Weather Forecast for GCU Campus Locations Globally");

        // Initialize forecast container
        LinearLayout forecastContainer = findViewById(R.id.forecastContainer);

        // Start fetching data for all locations
        startProgress(forecastContainer, glasgowUrl, "Glasgow");
        startProgress(forecastContainer, londonUrl, "London");
        startProgress(forecastContainer, newYorkUrl, "New York");
        startProgress(forecastContainer, omanUrl, "Oman");
        startProgress(forecastContainer, mauritiusUrl, "Mauritius");
        startProgress(forecastContainer, bangladeshUrl, "Bangladesh");
    }

    private void startProgress(final LinearLayout forecastContainer, final String url, final String locationName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    inputStream = new URL(url).openConnection().getInputStream();
                    final BbcWeatherXmlParser parser = new BbcWeatherXmlParser();
                    final List<WeatherDataItem> weatherData = parser.parseXml(inputStream);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Add location name
                            TextView locationTextView = new TextView(MainActivity.this);
                            locationTextView.setText(locationName);
                            forecastContainer.addView(locationTextView);

                            // Set up RecyclerView and adapter
                            RecyclerView recyclerView = new RecyclerView(MainActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            ForecastAdapter forecastAdapter = new ForecastAdapter(weatherData);
                            recyclerView.setAdapter(forecastAdapter);
                            forecastContainer.addView(recyclerView);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("Error fetching data for " + locationName + ": " + e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
