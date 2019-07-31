package com.cmrl.customer.api;


import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.cmrl.customer.BuildConfig;
import com.cmrl.customer.http.JsonRestClient;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.http.RestClient;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.model.Seat;
import com.cmrl.customer.model.Stops;
import com.cmrl.customer.model.TripDetails;

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
        String tripDetails = "tripdetails";

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

    public static void searchRoutes(Context aContext, String pickId, String stopId, Location location) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.routes), API.routes.hashCode());
            JSONObject object = new JSONObject();
            object.put("pickupStopId", pickId);
            object.put("dropStopId", stopId);
            object.put("lat", location.getLatitude());
            object.put("lng", location.getLongitude());
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


    public static void getBookDetails(Context aContext, int pickId, int dropId, int routeId) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.bookDetails), API.bookDetails.hashCode());
            JSONObject object = new JSONObject();
            object.put("routeSlotId", routeId);
            object.put("pickUpStopId", pickId);
            object.put("dropStopId", dropId);
            client.execute((ResponseListener) aContext, object, Seat.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTripDetails(Context aContext, int id) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.tripDetails), API.tripDetails.hashCode());
            JSONObject object = new JSONObject();
            object.put("customerId", "3434 3045 3437 7854");
            object.put("tripId", 1);
            client.execute((ResponseListener) aContext, object, TripDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
