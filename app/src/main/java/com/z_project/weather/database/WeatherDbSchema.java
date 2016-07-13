package com.z_project.weather.database;


public class WeatherDbSchema {


    public static final class PlaceTable {

        public static final String NAME = "places";

        public static final class Cols {

            public static final String UUID = "uuid";

            public static final String EXTERNAL_ID = "external_id";

            public static final String NAME = "name";

            public static final String COUNTRY = "country";

            public static final String LONGITUDE = "longitude";

            public static final String LATITUDE = "latitude";

        }
    }
}
