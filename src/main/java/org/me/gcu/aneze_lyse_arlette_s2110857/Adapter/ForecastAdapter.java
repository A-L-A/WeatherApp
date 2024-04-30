package org.me.gcu.aneze_lyse_arlette_s2110857.Adapter;

// Name: Lyse Arlette Aneze
// Student ID: s2110857

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.me.gcu.aneze_lyse_arlette_s2110857.Model.WeatherDataItem;
import org.me.gcu.aneze_lyse_arlette_s2110857.R;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private List<WeatherDataItem> weatherDataList;

    public ForecastAdapter(List<WeatherDataItem> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        WeatherDataItem weatherDataItem = weatherDataList.get(position);

        // Set text for each view dynamically based on WeatherDataItem
        holder.locationTextView.setText(weatherDataItem.getLocation());
        holder.dayOfWeekTextView.setText(weatherDataItem.getDayOfWeek());
        holder.dateTextView.setText(weatherDataItem.getDate());
        holder.minMaxTemperatureTextView.setText("Min Temp :" + weatherDataItem.getMinTemperature()  + "     Max Temp: " + weatherDataItem.getMaxTemperature());
        holder.weatherClassificationTextView.setText(weatherDataItem.getWeatherClassification());



        // Set click listener for the details button if needed
        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        TextView dayOfWeekTextView;
        TextView dateTextView;
        TextView minMaxTemperatureTextView;
        TextView weatherClassificationTextView;
        Button detailsButton;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            minMaxTemperatureTextView = itemView.findViewById(R.id.minMaxTemperatureTextView);
            weatherClassificationTextView = itemView.findViewById(R.id.weatherClassificationTextView);
            detailsButton = itemView.findViewById(R.id.detailsButton);
        }
    }
}
