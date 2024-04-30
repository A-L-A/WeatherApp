package org.me.gcu.aneze_lyse_arlette_s2110857.Model;// Name: Lyse Arlette Aneze
// Student ID: s2110857

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BbcWeatherXmlParser {
    private List<WeatherDataItem> weatherDataItemsList = new ArrayList<>();
    private WeatherDataItem currentWeatherDataItem;
    private String currentText;

    public List<WeatherDataItem> parseXml(InputStream inputStream) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            currentWeatherDataItem = new WeatherDataItem();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        currentText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            weatherDataItemsList.add(currentWeatherDataItem);
                            Log.d("BbcWeatherXmlParser", "Added new WeatherDataItem: " + currentWeatherDataItem.toString());
                        } else if (tagName.equalsIgnoreCase("description")) {
                            if (currentWeatherDataItem != null) {
                                currentWeatherDataItem.setDescription(currentText);
                                extractAndSetWeatherInfo(currentText);
                                Log.d("BbcWeatherXmlParser", "Description: " + currentText);
                            }
                        } else if (tagName.equalsIgnoreCase("guid")) {
                            if (currentWeatherDataItem != null) {
                                currentWeatherDataItem.setGuid(currentText);
                                Log.d("BbcWeatherXmlParser", "GUID: " + currentText);
                            }
                        } else if (tagName.equalsIgnoreCase("link")) {
                            if (currentWeatherDataItem != null) {
                                currentWeatherDataItem.setLink(currentText);
                                Log.d("BbcWeatherXmlParser", "Link: " + currentText);
                            }
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            if (currentWeatherDataItem != null) {
                                currentWeatherDataItem.setPubDate(currentText);
                                Log.d("BbcWeatherXmlParser", "PubDate: " + currentText);
                            }
                        } else if (tagName.equalsIgnoreCase("title")) {
                            if (currentWeatherDataItem != null) {
                                currentWeatherDataItem.setTitle(currentText);
                                Log.d("BbcWeatherXmlParser", "Title: " + currentText);
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("BbcWeatherXmlParser", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BbcWeatherXmlParser", e.toString());
        }

        return weatherDataItemsList;
    }

    // Method to extract and set weather information
    private void extractAndSetWeatherInfo(String description) {
        // Extract min/max temperature
        String minTemperature = extractTemperature(description, "Minimum Temperature");
        String maxTemperature = extractTemperature(description, "Maximum Temperature");
        currentWeatherDataItem.setMinTemperature(minTemperature);
        currentWeatherDataItem.setMaxTemperature(maxTemperature);
        Log.d("BbcWeatherXmlParser", "Min Temperature: " + minTemperature);
        Log.d("BbcWeatherXmlParser", "Max Temperature: " + maxTemperature);

        // Extract weather classification
        String weatherClassification = extractClassification(description);
        currentWeatherDataItem.setWeatherClassification(weatherClassification);
        Log.d("BbcWeatherXmlParser", "Weather Classification: " + weatherClassification);

        // Extract day of the week and date from title
        String title = currentWeatherDataItem.getTitle();
        String[] titleParts = title.split(":");
        if (titleParts.length > 0) {
            String dayOfWeek = titleParts[0];
            currentWeatherDataItem.setDayOfWeek(dayOfWeek);
            Log.d("BbcWeatherXmlParser", "Day of Week: " + dayOfWeek);
        }
        if (titleParts.length > 1) {
            String datePart = titleParts[1];
            String[] dateParts = datePart.split(",");
            if (dateParts.length > 0) {
                String date = dateParts[0];
                currentWeatherDataItem.setDate(date);
                Log.d("BbcWeatherXmlParser", "Date: " + date);
            }
        }

        // Extract location from description
        String location = extractLocation(description);
        currentWeatherDataItem.setLocation(location);
        Log.d("BbcWeatherXmlParser", "Location: " + location);
    }

    // Method to extract temperature from description
    private String extractTemperature(String description, String type) {
        Pattern pattern = Pattern.compile(type + ": (\\d+°[CF])");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Method to extract weather classification from description
    private String extractClassification(String description) {
        // Add your logic to extract weather classification here
        Pattern pattern = Pattern.compile(": (.+), Minimum Temperature");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }

    // Method to extract location from description
    private String extractLocation(String description) {
        // Add your logic to extract location here
        Pattern pattern = Pattern.compile("Forecast for (.+),");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }
}