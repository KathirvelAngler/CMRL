package com.cmrl.customer.http;

/**
 * Created by Mathan on 08-07-2019.
 */

public interface ResponseListener {

    /**
     * @param r - The model class that is passed on the parser
     */
    void onResponse(Response r);
}
