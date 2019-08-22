package com.cmrl.customer.helper

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.auth0.android.jwt.JWT
import com.cmrl.customer.R
import com.cmrl.customer.model.User
import com.cmrl.customer.utils.PermissionChecker
import com.cmrl.customer.utils.SingleChoiceAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mathan on 12/07/19.
 **/

object AppHelper {

    private var selection_dialog: Dialog? = null
    internal var mPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)


    /**
     * @param context Context
     */
    fun navigateAppSetting(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    /**
     * Single Choice Selection
     */
    fun showSingleChoice(
            context: Context,
            title: String?,
            array: Array<String>,
            callback: SingleChoiceAdapter.Callback,
            isCancelable: Boolean
    ) {

        val builder = AlertDialog.Builder(context, R.style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_single_selection, null)

        val dialogTitle = view.findViewById(R.id.dialog_title) as TextView
        val dialogRecyclerView = view.findViewById(R.id.dialog_recycler) as RecyclerView
        val dialogClose = view.findViewById(R.id.dialog_close_button) as ImageView

        dialogRecyclerView.layoutManager = LinearLayoutManager(context)
        dialogRecyclerView.adapter = SingleChoiceAdapter(context, array, callback)

        builder.setCancelable(false)

        if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(R.string.app_name) else title

        dialogClose.setOnClickListener {
            selection_dialog!!.dismiss()
        }

        builder.setView(view)
        selection_dialog = builder.create()
        selection_dialog!!.show()
    }

    fun hideSingleChoice() {
        if (selection_dialog != null && selection_dialog!!.isShowing)
            selection_dialog!!.dismiss()
    }

    fun getColor(context: Context, i: Int): Int {
        return ContextCompat.getColor(context, i)
    }

    fun showNoData(view: View, show: Boolean, icon: Int?, msg: String?) {
        try {
            val mNodataLayout = view.findViewById<LinearLayout>(R.id.layout_nodata_layout)
            val mNodataImage = view.findViewById<ImageView>(R.id.layout_nodata_image)
            val mNodataMessage = view.findViewById<TextView>(R.id.layout_nodata_message)

            if (show) {
                mNodataLayout.visibility = View.VISIBLE
                icon?.let { mNodataImage.setImageResource(it) }
                msg?.let { mNodataMessage.text = msg }
            } else mNodataLayout.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showNoData(view: View, show: Boolean, msg: String?) {
        try {
            val mNodataLayout = view.findViewById<LinearLayout>(R.id.layout_nodata_layout)
            val mNodataImage = view.findViewById<ImageView>(R.id.layout_nodata_image)
            val mNodataMessage = view.findViewById<TextView>(R.id.layout_nodata_message)

            if (show) {
                mNodataLayout.visibility = View.VISIBLE
                mNodataImage.setImageResource(R.mipmap.ic_launcher_round)
                msg?.let { mNodataMessage.text = msg }
            } else mNodataLayout.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun swipeRefColor(context: Context, swipe: SwipeRefreshLayout) {
        swipe.setColorSchemeColors(
                getColor(context, R.color.app_blue),
                getColor(context, R.color.app_light_blue),
                getColor(context, R.color.app_gray)
        )
    }

    /**
     * @param aSelectedDate     String
     * @param aCurrentFormat    String
     * @param aConversionFormat String
     * @return String
     */
    fun convertDateFormat(aSelectedDate: String, aCurrentFormat: String, aConversionFormat: String): String {
        var aDate = ""
        try {
            if (aSelectedDate.isEmpty())
                return ""
            val d = SimpleDateFormat(aCurrentFormat, Locale.ENGLISH).parse(aSelectedDate)
            aDate = SimpleDateFormat(aConversionFormat, Locale.ENGLISH).format(d.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return aDate
    }

    fun getCurrentLocation(context: Context, successListener: OnSuccessListener<Location>, failureListener: OnFailureListener) {
        try {
            val mFusedClient = LocationServices.getFusedLocationProviderClient(context)

            if (PermissionChecker().checkAllPermission(context, mPermissions)) {
                AppDialogs.showProgressDialog(context)
                mFusedClient.lastLocation.addOnSuccessListener(successListener)
                mFusedClient.lastLocation.addOnFailureListener(failureListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigateGoogleMap(context: Context, OrginLatLng: LatLng, DesigLatLng: LatLng) {
        try {
            val ai = context.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            val appStatus = ai.enabled
            if (appStatus) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s"
                        , OrginLatLng.latitude, OrginLatLng.longitude, DesigLatLng.latitude, DesigLatLng.longitude)))
                context.startActivity(intent)
            } else
                AppDialogs.okAction(context, context.getString(R.string.alert_enable_map))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    fun decodeToken(context: Context, token: String): User? {
        val jwt = JWT(token)
        val user = User()
        user.acctNbr = jwt.claims["acctNbr"]!!.asString()
        user.firstName = jwt.claims["firstName"]!!.asString()
        user.lastName = jwt.claims["lastName"]!!.asString()
        user.mobile = jwt.claims["mobile"]!!.asString()
        user.dob = jwt.claims["dob"]!!.asString()
        user.gender = jwt.claims["gender"]!!.asString()
        user.exp = jwt.claims["exp"]!!.asString()
        user.fullName = String.format("%s %s", user.firstName, user.lastName).trim()
        user.token = token

        return user
    }

}