package com.cmrl.customer.model;


import android.graphics.drawable.Drawable;

import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mathan on 15-07-2019.
 */

public class TripDetails extends Response {
    public String detail = "";
    public Drawable icon;

    @SerializedName("data")
    public TripDetails details;

    @SerializedName("customerName")
    public String customerName = "";

    @SerializedName("cabNo")
    public String cabNo = "";

    @SerializedName("bookedTickets")
    public int tickets;

    @SerializedName("fare")
    public Double fare;

    @SerializedName("estimatedMins")
    public String traveledTime;

    @SerializedName("estimatedKm")
    public String traveledDistance;

    @SerializedName("pickUpStopName")
    public String pickLocation;

    @SerializedName("dropStopName")
    public String dropLocation;

    @SerializedName("pickUpDateTime")
    public String pickTime;

    @SerializedName("dropDateTime")
    public String dropTime;

    @SerializedName("paymentMode")
    public String paymentMode;

    @SerializedName("routeUrl")
    public String routeUrl;
}
