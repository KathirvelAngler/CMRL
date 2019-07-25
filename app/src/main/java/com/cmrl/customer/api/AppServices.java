package com.cmrl.customer.api;


import android.content.Context;

import com.android.volley.Request;
import com.cmrl.customer.BuildConfig;
import com.cmrl.customer.http.JsonRestClient;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.http.RestClient;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.model.Seat;
import com.cmrl.customer.model.Stops;

import org.json.JSONObject;

/**
 * Created by Mathan on 19-07-2019.
 */

public class AppServices {

    /**
     * @param urlKey - API name
     * @return - API url
     */
    private static String constructUrl(String urlKey) {
        return String.format("%s/api/v1/%s", BuildConfig.API_URL, urlKey);
    }

    public interface API {
        //Common Api's

        String stops = "stops";
        String stations = "stations";
        String routes = "routes";
        String routestop = "routestop";
        String bookDetails = "getslot";

    }

    public static void getStops(Context aContext) {
        try {
            // Generating Req
            RestClient client = new RestClient(aContext, Request.Method.GET,
                    constructUrl(API.stops), API.stops.hashCode());
            client.execute((ResponseListener) aContext, Stops.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getStations(Context aContext) {
        try {
            // Generating Req
            RestClient client = new RestClient(aContext, Request.Method.GET,
                    constructUrl(API.stations), API.stations.hashCode());
            client.execute((ResponseListener) aContext, Stops.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchRoutes(Context aContext, int pickId, int stopId) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.routes), API.routes.hashCode());
            JSONObject object = new JSONObject();
            object.put("pickupStopId", pickId);
            object.put("dropStopId", stopId);
            client.execute((ResponseListener) aContext, object, Routes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoutes(Context aContext, ResponseListener listener, int id) {
        try {
            // Generating Req
            String url = String.format("%s/%s", constructUrl(API.routestop), id);

            RestClient client = new RestClient(aContext, Request.Method.GET,
                    url, API.routestop.hashCode());
            client.execute(listener, Routes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getBookDetails(Context aContext, int id) {
        try {
            // Generating Req
            String url = String.format("%s/%s", constructUrl(API.bookDetails), id);

            RestClient client = new RestClient(aContext, Request.Method.GET,
                    url, API.bookDetails.hashCode());
            client.execute((ResponseListener) aContext, Seat.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
