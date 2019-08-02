package com.cmrl.customer.model;


import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mathan on 15-07-2019.
 */

public class Seat extends Response {
    /**
     * -1 : Not Available
     * 0 : Available
     * 1 : Selected, But not Booked.
     */
    public int available = 0;

    @SerializedName("data")
    public Seat details;

    @SerializedName("id")
    public int id;

    @SerializedName("scheduleSlot")
    public String scheduleSlot = "";

    @SerializedName("firstStopName")
    public String firstStopName = "";

    @SerializedName("lastStopName")
    public String lastStopName = "";

    @SerializedName("cabNumber")
    public String cabNumber = "";

    @SerializedName("routeUrl")
    public String routeUrl = "";

    @SerializedName("availableSeats")
    public int availableSeats;

    @SerializedName("cabCapacity")
    public int cabCapacity;

    @SerializedName("fare")
    public double fare;

    @SerializedName("dateTime")
    public String dateTime = "";

    @SerializedName("tripId")
    public int tripId;
}
