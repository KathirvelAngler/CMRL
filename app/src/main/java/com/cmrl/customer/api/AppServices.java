package com.cmrl.customer.api;


import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.cmrl.customer.BuildConfig;
import com.cmrl.customer.http.JsonRestClient;
import com.cmrl.customer.http.ResponseListener;
import com.cmrl.customer.http.RestClient;
import com.cmrl.customer.model.Booking;
import com.cmrl.customer.model.History;
import com.cmrl.customer.model.Routes;
import com.cmrl.customer.model.Stops;
import com.cmrl.customer.model.TripDetails;
import com.cmrl.customer.model.User;
import com.cmrl.customer.preference.LocalStorageSP;

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
        String bookTicket = "book";
        String history = "bookingHistory";

    }

    public static void getStops(Context aContext, int id) {
        try {
            // Generating Req

            String url = String.format("%s/%s", constructUrl(API.stops), id);

            RestClient client = new RestClient(aContext, Request.Method.GET,
                    url, API.stops.hashCode());
            client.execute((ResponseListener) aContext, Stops.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getStations(Context aContext, int id) {
        try {
            // Generating Req

            String url = String.format("%s/%s", constructUrl(API.stations), id);

            RestClient client = new RestClient(aContext, Request.Method.GET,
                    url, API.stations.hashCode());
            client.execute((ResponseListener) aContext, Stops.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchRoutes(Context aContext, String pickId, String stopId,
                                    Location location, boolean isCurrentLocation) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.routes), API.routes.hashCode());
            JSONObject object = new JSONObject();

            if (isCurrentLocation)
                object.put("isCurrentGps", true);
            else {
                object.put("pickupStopId", pickId);
                object.put("dropStopId", stopId);
            }

            object.put("lat", location.getLatitude());
            object.put("lng", location.getLongitude());
            client.execute((ResponseListener) aContext, object, Routes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoutes(Context aContext, ResponseListener listener, int startId, int stopId) {
        try {
            // Generating Req
            String url = String.format("%s/%s/%s", constructUrl(API.routestop), startId, stopId);

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
            if (dropId != 0)
                object.put("dropStopId", dropId);
            client.execute((ResponseListener) aContext, object, Booking.class);
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
            object.put("customerMobile", LocalStorageSP.INSTANCE.getLoginUser(aContext).mobile);
            object.put("tripId", id != 0 ? id : 1);
            client.execute((ResponseListener) aContext, object, TripDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bookTicket(Context aContext, Booking details) {
        try {
            // Generating Req
            JsonRestClient client = new JsonRestClient(aContext, Request.Method.POST,
                    constructUrl(API.bookTicket), API.bookTicket.hashCode());
            JSONObject object = new JSONObject();
            object.put("tripId", details.tripId);

            User user = LocalStorageSP.INSTANCE.getLoginUser(aContext);
            object.put("customerMobile", user.mobile);
            object.put("customerName", user.fullName);
            object.put("customerToken", user.token);

            object.put("cabAssigned", details.cabNumber);
            object.put("noOfSeats", details.totalTickets);
            object.put("fare", details.totalTickets * details.fare);

            object.put("pickupLat", details.pickup.lat);
            object.put("pickupLng", details.pickup.lng);
            object.put("pickUpLocation", details.pickup.location);
            object.put("pickupStopId", details.pickup.stopId);

            object.put("dropLat", details.drop.lat);
            object.put("dropLng", details.drop.lng);
            object.put("dropLocation", details.drop.location);
            object.put("dropStopId", details.drop.stopId);

            object.put("paymentMode", "Cash");
            object.put("routeSlotId", details.routeId);

            client.execute((ResponseListener) aContext, object, Booking.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getHistory(Context aContext) {
        try {
            // Generating Req
            String url = String.format("%s/%s", constructUrl(API.history),
                    LocalStorageSP.INSTANCE.getLoginUser(aContext).mobile);

            RestClient client = new RestClient(aContext, Request.Method.POST,
                    url, API.history.hashCode());
            client.execute((ResponseListener) aContext, History.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
