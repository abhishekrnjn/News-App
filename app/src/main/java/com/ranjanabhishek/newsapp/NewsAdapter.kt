package com.ranjanabhishek.newsapp

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.getSystemService

class NewsAdapter(context: Context, arrayList: ArrayList<Data>):BaseAdapter() {


    var arrayList: ArrayList<Data> = ArrayList()
    var context: Context?

    init {
        this.context = context
        this.arrayList = arrayList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if(convertView == null) {
            holder = ViewHolder()
            view = inflater.inflate()

        }

    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    class ViewHolder{
        var sectionName : TextView? =null
        var webTitle: TextView? = null


    }

}