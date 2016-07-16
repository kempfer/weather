package com.z_project.weather.database;


public class WeatherDbSchema {


    public static final class PlaceTable {

        public static final String NAME = "places";

        public static final class Cols {

            public static final String UUID = "uuid";

            public static final String EXTERNAL_ID = "external_id";

            public static final String NAME = "name";

            public static final String REGION = "region";

            public static final String COUNTRY = "country";

            public static final String LONGITUDE = "longitude";

            public static final String LATITUDE = "latitude";

        }
    }

    public static final class WeatherTable {

        public static final String NAME = "weather";

        public static final class Cols {

            public static final String PLACE_UUID = "place_uuid";

            public static final String SUNRISE = "sunrise";

            public static final String SUNSET = "sunset";

            public static final String TIME = "time";

            public static final String TEMPERATURE = "temperature";

            public static final String TEMPERATURE_MIN = "temperature_min";

            public static final String TEMPERATURE_MAX = "temperature_max";

            public static final String PRESSURE  = "pressure";

            public static final String HUMIDITY = "humidity";

            public static final String DESCRIPTION = "description";

            public static final String ICON = "icon";

            public static final String WIND_SPEED = "wind_speed";

            public static final String WIND_DEG = "wind_deg";

            public static final String UPDATED_AT = "updated_at";

        }
    }
}
