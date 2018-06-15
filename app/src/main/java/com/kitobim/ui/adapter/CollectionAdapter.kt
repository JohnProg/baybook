package com.kitobim.ui.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.CollectionEntity
import kotlinx.android.synthetic.main.item_list_collection.view.*


class CollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<CollectionEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_collection, parent, false) as LinearLayout)
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        val collection = mList[position]

        val holder = viewholder as ListViewHolder
        holder.name.text = collection.name
    }

    override fun getItemCount(): Int = mList.size

    fun updateData(list: List<CollectionEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        mList = emptyList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = mList[position]

    inner class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.txt_title_collection
    }
}