package com.cmrl.customer.model;


import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mathan on 05-08-2019.
 */

public class History extends Response {

    @SerializedName("data")
    public ArrayList<History> histories;

    @SerializedName("bookingNo")
    public String bookingNo = "";

    @SerializedName("bookedDateTime")
    public String bookedDateTime = "";

    @SerializedName("noOfSeats")
    public int noOfSeats;

    @SerializedName("fare")
    public double fare;

    @SerializedName("pickupLocation")
    public String pickupLocation = "";

    @SerializedName("dropLocation")
    public String dropLocation = "";

    @SerializedName("bookingStatus")
    public String bookingStatus = "";

    @SerializedName("tripId")
    public int tripId;
}
