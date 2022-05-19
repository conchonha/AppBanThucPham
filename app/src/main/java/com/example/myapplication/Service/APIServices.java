package com.example.myapplication.Service;

public class APIServices {
    private static String baseurl="http://172.20.0.186/cuahangsach/public/";
    public static String urlhinh="http://172.20.0.186/cuahangsach/public/";

    public static DataService getService(){
        return APIRetrofitClient.getClient(baseurl).create(DataService.class);
    }
}
