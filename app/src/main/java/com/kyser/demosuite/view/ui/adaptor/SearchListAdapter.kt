package com.kyser.demosuite.view.ui.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kyser.demosuite.R
import com.kyser.demosuite.service.model.ListingModel
import kotlinx.android.synthetic.main.category_spinner.view.*


class SearchListAdapter(val mContext: Context) : BaseAdapter() {

    private var search_list : List<ListingModel> ?=null

    fun setSearchList(list : List<ListingModel>){
        search_list = list
        notifyDataSetChanged()
    }

    fun getSearchList(): List<ListingModel>? {
        return  search_list;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val  view = LayoutInflater.from(mContext).inflate(R.layout.search_spinner,null,false)
        view.text1.text = search_list?.get(position)?.title
        return view;
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val  view = LayoutInflater.from(mContext).inflate(R.layout.category_spinner_item,null,false)
        view.text1.text = search_list?.get(position)?.title
        return view;
    }

    override fun getItem(position: Int): String {
       return search_list?.get(position)?.title?: ""
    }

    override fun getItemId(position: Int): Long {
        return search_list?.get(position)?.mid?.toLong()?:0
    }

    override fun getCount(): Int {
        return search_list?.size?: 0
    }


}