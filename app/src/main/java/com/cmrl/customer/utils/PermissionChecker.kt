package com.cmrl.customer.utils


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import java.util.*


/**
 * Created by Mathan on 12-07-19.
 */

open class PermissionChecker {

    private val REQUEST_CODE_PERMISSION = 2
    private var mPermissions: ArrayList<String>? = null

    fun askAllPermissions(context: Context, aPermissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(context as Activity, aPermissions, REQUEST_CODE_PERMISSION)
        }
    }

    fun checkAllPermission(context: Context, aPermissions: Array<String>): Boolean {
        var isGranted = false
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (i in aPermissions.indices) {
                    isGranted = false
                    if (!checkPermission(context, aPermissions[i])) {
                        askPermission(context, aPermissions[i])
                        break
                    } else isGranted = true
                }
            } else isGranted = true
        } catch (e: Exception) {
            e.printStackTrace()
            return isGranted
        }

        return isGranted
    }

    fun askPermission(context: Context, aPermission: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, aPermission) == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, aPermission)) {
                        mPermissions = ArrayList()
                        mPermissions!!.add(aPermission)
                        ActivityCompat.requestPermissions(
                                context,
                                mPermissions!!.toArray(arrayOfNulls<String>(mPermissions!!.size)),
                                REQUEST_CODE_PERMISSION
                        )
                    } else {
                        Log.d(aPermission, " -----> Never ask again")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkPermission(context: Context, aPermission: String): Boolean {
        var isGranted = false
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isGranted = ContextCompat.checkSelfPermission(context, aPermission) == PackageManager.PERMISSION_GRANTED
            } else isGranted = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isGranted
    }
}
