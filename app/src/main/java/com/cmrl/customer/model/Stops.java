package com.cmrl.customer.model;


import com.cmrl.customer.http.Response;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mathan on 17-07-2019.
 */

public class Stops extends Response {
    public int header = 0;

    @SerializedName("data")
    public ArrayList<Stops> data = new ArrayList<>();

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name = "";

    @SerializedName("stopName")
    public String stopName = "";

}
