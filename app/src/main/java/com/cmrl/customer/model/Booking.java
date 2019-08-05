package com.cmrl.customer.model;


import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mathan on 15-07-2019.
 */

public class Booking extends Response {
    /**
     * -1 : Not Available
     * 0 : Available
     * 1 : Selected, But not Booked.
     */
    public int available = 0;

    @SerializedName("data")
    public Booking details;

    @SerializedName("id")
    public int id;
    public int routeId;

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
    public int totalTickets;

    @SerializedName("cabCapacity")
    public int cabCapacity;

    @SerializedName("fare")
    public double fare;

    @SerializedName("dateTime")
    public String dateTime = "";

    @SerializedName("tripId")
    public int tripId;

    @SerializedName("pickup")
    public Booking pickup;

    @SerializedName("drop")
    public Booking drop;

    @SerializedName("lat")
    public double lat;

    @SerializedName("lng")
    public double lng;

    @SerializedName("location")
    public String location = "";

    @SerializedName("stopId")
    public int stopId;

    // Ticket Booked

    @SerializedName("bookingNo")
    public String bookingNo = "";

    @SerializedName("bookedDate")
    public String bookedDate = "";
}
