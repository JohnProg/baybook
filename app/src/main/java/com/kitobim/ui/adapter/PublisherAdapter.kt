package com.kitobim.ui.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.PublisherEntity
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.item_list_publisher.view.*


class PublisherAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<PublisherEntity>()
    private lateinit var mClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_publisher, parent, false) as ConstraintLayout)

    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        val publisher = mList[position]

        val holder = viewholder as ListViewHolder
        holder.bind(mList[position].id, mClickListener)
        holder.name.text = publisher.name

        if (publisher.logo != null) {
            ImageHelper.setBookCover(holder.thumbnail, publisher.logo)
        }

    }

    override fun getItemCount(): Int = mList.size

    fun updateData(list: List<PublisherEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        mList = emptyList()
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }


    inner class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.txt_name_publisher
        val thumbnail: ImageView = view.img_thumbnail_publisher

        fun bind(item: Int, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

}