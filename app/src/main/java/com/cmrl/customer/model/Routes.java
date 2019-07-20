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

    @SerializedName("routeId")
    public int routeId;

    @SerializedName("routeName")
    public String routeName = "";

    @SerializedName("routeSlot")
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

    @SerializedName("distance")
    public int distance;

}
