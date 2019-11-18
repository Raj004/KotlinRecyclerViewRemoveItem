package com.example.testing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.util.ArrayList


class CustomAdapter(
    var userList: ArrayList<PlaceWorkModel>,
    val mContext: MainActivity
) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    private var mcontext: Context? = null
    private var itemList: List<PlaceWorkModel>? = null


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.MyViewHolder, position: Int) {
        val article = userList.get(position)
        val holder1 = holder


        Log.e("List-+++", userList.size.toString())
        Log.e("IMAGE-++", article.getPhot_url())

        Glide.with(mContext).load(article.getPhot_url()).into(holder1.icon)


    }


    fun notifyData(myList: ArrayList<PlaceWorkModel>) {
        Log.d("notifyData ", myList.size.toString() + "")
        this.userList = myList
        notifyDataSetChanged()
    }
//
//    //this method is giving the size of the list
//    override fun getItemCount(): Int {
//        return userList!!.size
//    }

    override fun getItemCount(): Int {
        return if (null != userList) userList.size else 0
    }


    //the class is hodling the list view
    inner class MyViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
        var icon: ImageView

        init {
            icon = parent.findViewById(R.id.image_set) as ImageView
            icon.setOnClickListener {
                Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT)
                    .show();
                userList.removeAt(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size);
            }
        }


    }
}