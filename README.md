# WeatherVue Application

## Project Overview
This Android mobile application displays the weather status and temperature from the user's location.
Users can also pick a specific location on the map or search for it using an autocomplete edit text
. Additionally, they can add locations to a list of favorite locations and get weather information for those locations.
The application also allows users to set weather alerts for rain, wind, extreme temperatures, fog, snow, etc.


## Features
- **Settings Screen:**
  - Choose location by GPS or map selection
  - Choose temperature units (Kelvin, Celsius, Fahrenheit)
  - Choose wind speed units (meter/sec, miles/hour)
  - Choose language (Arabic, English)

- **Home Screen:**
  - Display current weather information
  - Current temperature, date, time, humidity, wind speed, pressure, clouds, city, weather icon, and description
  - Past hourly weather for the current date
  - Past weather forecast for 5 days

- **Weather Alerts Screen:**
  - Add weather alerts with customizable settings
  - Set alarm duration and type
  - Option to stop notifications or turn off alarms

- **Favorite Screen:**
  - List of favorite locations
  - View forecast information for each favorite location
  - Add new favorite locations via map selection or autocomplete edit text
  - Remove saved locations

## API
Weather data is fetched from the [OpenWeatherMap API](https://api.openweathermap.org). 

- **Android SDK Version:**
-  compileSdk = 34
-  targetSdk = 34




