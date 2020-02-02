package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.util.Scanner;

import com.google.gson.Gson;


public class ApiGames {
    public static final int DEFAULT_RETRIES = 5;
    public static final long DEFAULT_TIME_TO_WAIT_MS = 100;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Incorrect number of arguments passed.");
            System.exit(0);
        }
        String city = args[0];
        for (int i = 1; i < args.length; i++) {
            city = city + "+" + args[i];
        }

        city = city.replace("-", "+"); // For cities with a "-" in the name

//  This section was for GUI interaction, commented out and replaced by CLI actions above. //--
//        Scanner input = new Scanner(System. in);
//        System.out.println("Enter the name of a city, I will tell you the weather and a top headline: ");
//        String city;
//        city = input.nextLine();
//        city = city.replace(" ", "+");

//        while (city.length() < 1 || !city.matches(regex)) {
//            System.out.println("Please enter a valid city name: ");
//            input.next();
//            city = input.next();
//        }

        String regex = "^[a-zA-Z, +]+$";
        if (city.length() < 1 || !city.matches(regex)) {
            System.out.println("That was an invalid city entry. Please try again. \nEnding program.");
        }
        else {
            GetWeather(city);
            GetNews(city);
        }
    }
    private static void GetWeather(String city) throws IOException {
        BufferedReader weatherReader = new BufferedReader(new FileReader("/Users/mitchell/Documents/UW_Bothell/Winter_2020/CloudComputing/Program2/src/com/company/WeatherAPIkey.txt"));
        String weather_api_key = weatherReader.readLine();
        weatherReader.close();

        String weatherBaseURL = "http://api.openweathermap.org/data/2.5/weather?q="+ city + weather_api_key;
        try {
            boolean success = false;
            URL url = new URL(weatherBaseURL);
            int connectionAttempts = 1;
            int responseCode = 0;
            while (connectionAttempts < DEFAULT_RETRIES) {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                responseCode= con.getResponseCode();
                if ( responseCode >= 200 && responseCode < 400) {
                    BufferedReader buff = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = buff.readLine()) != null) {
                        response.append(inputLine);
                    }
                    buff.close();

                    Gson gsonObj = new Gson();
                    WeatherData newWeather = gsonObj.fromJson(String.valueOf(response), WeatherData.class);
                    PrintWeather(city.replace("+", " "), newWeather.MainObject, connectionAttempts);
                    success = true;
                    break;
                }
                else if (responseCode >= 400 && responseCode < 500) {
                    System.out.print("Client side error. ");
                    break;
                }
                else{
                    waitUntilNextTry(connectionAttempts++);
                }
            }
            if (!success) {
                System.out.println("We are sorry, we are unable to find a weather forecast for " + city.replace("+", " ") + ". Check your spelling?\n");
                System.out.println("Final response code: " + responseCode + "\n\n");
            }
        }
        catch(MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void PrintWeather(String city, WeatherMain weatherMain, int connectionAttempts) {
        System.out.println("\nAfter " + connectionAttempts + " connection attempt(s), today's forecast for " + city + ".  All units are in Farenheit: \nThe current temperature in F is " + weatherMain.getTemp()+
                "\nThe day's max temperature will be " + weatherMain.getTemp_max() +
                "\nThe day's min temperature will be " + weatherMain.getTemp_min() +
                "\nThe day's humidity is " + weatherMain.getHumidity()+ "%" +
                "\nThe day's atmospheric pressure is " + weatherMain.getPressure() + " hPa\n\n");
    }
    private static void GetNews(String city) throws IOException {

        BufferedReader newsReader = new BufferedReader(new FileReader("/Users/mitchell/Documents/UW_Bothell/Winter_2020/CloudComputing/Program2/src/com/company/NewsAPIkey.txt"));
        String news_api_key = newsReader.readLine();
        newsReader.close();

        String newsBaseURL = "https://newsapi.org/v2/everything?q=" + city + "&from=2020-01-24&sortBy=popularity&apiKey=" + news_api_key;

        try {
            boolean success = false;
            URL url = new URL(newsBaseURL);
            int connectionAttempts = 1;
            while (connectionAttempts < DEFAULT_RETRIES) {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if ( responseCode >= 200 && responseCode < 400) {
                    BufferedReader buff = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer newsResponse = new StringBuffer();
                    while ((inputLine = buff.readLine()) != null) {
                        newsResponse.append(inputLine);
                    }
                    buff.close();

                    Gson gsonObj = new Gson();
                    NewsArticle newsArticle = gsonObj.fromJson(String.valueOf(newsResponse), NewsArticle.class);
                    PrintNews(city.replace("+", " "), newsArticle.articles.get(0), connectionAttempts);
                    success = true;
                    break;
                }
                else if (responseCode >= 400 && responseCode < 500) {
                    System.out.print("Client side error. ");
                    break;
                }
                else{
                    waitUntilNextTry(connectionAttempts++);
                }
            }
            if (!success) {
                System.out.println("We are sorry, either a super slow day in the news for " + city + ", or there are server side issues.\n\n");
            }

        }
        catch(MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void PrintNews(String city, Article article, int connectionAttempts) {
        System.out.println("After " + connectionAttempts + " connection attempt(s), The top news article concerning " + city + ",\nwritten by "
        + article.getAuthor() + " from " + article.getSource().getName() + " is: \n\"" + article.getTitle() + "\"\n" + article.getContent() + "\"" + "\"");
    }

    // If url connection fails, this waits exponentially longer between connection attempts.
    public static void waitUntilNextTry(int connectionAttempts)
    {
        try {
            Thread.sleep((connectionAttempts*connectionAttempts) * DEFAULT_TIME_TO_WAIT_MS);
        }
        catch (InterruptedException iex) { }
    }
}
