package com.cmrl.customer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cmrl.customer.R

/**
 * Created by Mathan on 12-07-2019.
 */

class SingleChoiceAdapter(
        var mContext: Context,
        var mItems: Array<String>,
        var mCallback: Callback
) :
        RecyclerView.Adapter<SingleChoiceAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inflate_single_choice_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.mName.text = mItems[position]
        holder.mName.setOnClickListener {
            mCallback.info(position, mItems[position])
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    open class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mName = view.findViewById<TextView>(R.id.inflate_single_item_text)!!
    }

    interface Callback {
        fun info(position: Int, text: String)
    }
}
