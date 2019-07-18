package com.cmrl.customer.helper

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.cmrl.customer.R
import com.cmrl.customer.utils.SingleChoiceAdapter

/**
 * Created by Mathan on 12/07/19.
 **/

object AppHelper {

    private var selection_dialog: Dialog? = null


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

}