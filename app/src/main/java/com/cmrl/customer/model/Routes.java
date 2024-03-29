package com.cmrl.customer.model;


import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mathan on 20-07-2019.
 */

public class Routes extends Response {

    @SerializedName("data")
    public ArrayList<Routes> data = new ArrayList<>();

    @SerializedName("pickupStopName")
    public String stopName = "";

    @SerializedName("pickupStopId")
    public int pickId;

    @SerializedName("dropStopId")
    public int dropId;

    public boolean isCurrentLocation = false;

    @SerializedName("pickupLat")
    public Double pickupLat;

    @SerializedName("pickupLng")
    public Double pickupLng;

    @SerializedName("duration")
    public String duration = "";

    @SerializedName("route")
    public ArrayList<Routes> routes = new ArrayList<>();

    @SerializedName("routeName")
    public String routeName = "";

    @SerializedName("noOfInbetweenStops")
    public int totalStops;

    @SerializedName("routeStops")
    public ArrayList<String> routeStops = new ArrayList<>();

    @SerializedName("routeUrl")
    public String routeUrl = "";

    @SerializedName("routeId")
    public int routeId;

    @SerializedName("slot")
    public ArrayList<Routes> routeSlots = new ArrayList<>();

    @SerializedName("id")
    public int id;

    public String subId = "";

    @SerializedName("routeCode")
    public String routeCode = "";

    @SerializedName("scheduleSlot")
    public String scheduleSlot = "";

    @SerializedName("cabNumber")
    public String cabNumber = "";

    @SerializedName("availableSeats")
    public int availableSeats;

}
