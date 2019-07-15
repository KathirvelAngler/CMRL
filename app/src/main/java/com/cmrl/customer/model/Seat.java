package com.cmrl.customer.model;


/**
 * Created by Mathan on 15-07-2019.
 */

public class Seat {
    public String status = "";

    /**
     * -1 : Not Available
     * 0 : Available
     * 1 : Selected, But not Booked.
     */
    public int available = 0;
}
