package com.cmrl.customer.http;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mathan on 19-07-2019.
 */

public class Response implements Serializable {

    @SerializedName("code")
    public String responseCode;

    @SerializedName("message")
    public String message;

    @SerializedName("custom_message")
    public String customMessage;

    @SerializedName("xmlns")
    public String xmlns;

    @SerializedName("content")
    public String content;

    /**
     * unique integer number for the request
     */
    public int requestType;


    public String extraOutput;

    /**
     * @return true if the response gets success id
     */
    public boolean isSuccess() {
        return responseCode.equals("200");
    }

    @Override
    public String toString() {
        if (customMessage == null) {
            return message;
        }
        return message + ". \t" + customMessage;
    }
}
