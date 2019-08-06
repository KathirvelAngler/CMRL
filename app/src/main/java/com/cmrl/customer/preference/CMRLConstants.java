package com.cmrl.customer.preference;

/**
 * Created by Mathan on 09-07-2019.
 */

public interface CMRLConstants {

    int BOOK_CAB = 0;
    int TRACK_CAB = 1;
    int HISTORY = 2;
    int HELP = 3;


    int BOOKED = -1;
    int AVAILABLE = 0;
    int SELECTED = 1;

    String IND_RUPEE = "\u20B9";

    // Date/Time Formats

    String DD_MM_YY = "dd-MM-yyyy"; // 06-12-1993
    String YY_MM_DD = "yyyy-MM-dd"; // 1993-12-06
    String YY_MM_DD_SLASH = "yyyy/MM/dd"; // 1993/12/06
    String DD_MMM_YY = "dd MMM yyyy"; // 06 Dec 1993
    String DD_MMM_YY_HH_MM = "dd MMM yyyy hh:mm aa"; // 06 Dec 1993 05:10 PM
    String DD_MMM_yy_HH_MM = "dd MMM yy hh:mm aa"; // 06 Dec 93 05:10 PM
    String DD_MM_YY_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // 2019-07-29T17:45:08.629+05:30
    String MMM_DD_EEE = "MMM dd, EEE - hh:mm aa"; // Dec 06,Mon - 05:10  PM
    String HH_MM = "HH:mm"; // 20:30
    String HH_MM_AA = "hh:mm aa"; // 10:30 AM


}
