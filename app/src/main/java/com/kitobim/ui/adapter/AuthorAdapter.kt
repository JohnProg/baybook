package com.kitobim.ui.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kitobim.R
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.ui.custom.ImageHelper
import kotlinx.android.synthetic.main.item_grid_author.view.*
import kotlinx.android.synthetic.main.item_list_author.view.*


class AuthorAdapter(private val context: Context, private val isGridLayout: Boolean = false)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mList = emptyList<AuthorEntity>()
    private lateinit var mClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isGridLayout) {
            GridViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_grid_author, parent, false) as ConstraintLayout)
        } else {
            ListViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_author, parent, false) as ConstraintLayout)
        }
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        val author = mList[position]

        if (isGridLayout) {
            val holder = viewholder as GridViewHolder
            holder.bind(mList[position].id, mClickListener)
            holder.name.text = author.name

            if (author.thumbnail != null) {
                ImageHelper.setAuthorImage(holder.thumbnail, author.thumbnail)
            }
        } else {
            val holder = viewholder as ListViewHolder
            holder.bind(mList[position].id, mClickListener)
            holder.name.text = author.name

            if (author.thumbnail != null) {
                ImageHelper.setAuthorImage(holder.thumbnail, author.thumbnail)
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    fun updateData(list: List<AuthorEntity>) {
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
        val name: TextView = view.txt_name_list_author
        val thumbnail: ImageView = view.img_list_author

        fun bind(item: Int, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    inner class GridViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.txt_name_grid_author
        val thumbnail: ImageView = view.img_grid_author

        fun bind(item: Int, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(item) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

}