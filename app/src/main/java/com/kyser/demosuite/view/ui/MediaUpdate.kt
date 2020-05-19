package com.kyser.demosuite.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kyser.demosuite.R
import com.kyser.demosuite.service.model.ListingModel
import com.kyser.demosuite.viewmodel.SearchListModel
import kotlinx.android.synthetic.main.activity_media_update.*
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.kyser.demosuite.view.ui.adaptor.SearchListAdapter
import android.content.Intent
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.view.inputmethod.InputMethodManager
import com.kyser.demosuite.service.streamservice.StreamService

class MediaUpdate : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mSearchListModel: SearchListModel
    private lateinit var result_spiner_adaptor: SearchListAdapter
    private var categories = arrayOf("Movies", "Tv", "Music")
    private var mPicturePath: String = ""
    private var mSelectedMid :  String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_update)
        updateCategorySpinner()
        initViewModel()

        find_btn.setOnClickListener(this)
        set_item.setOnClickListener(this)
        gallery.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        category_spinner.onItemSelectedListener = this
        result_spinner.onItemSelectedListener = this
    }

    override fun onResume() {
        super.onResume()
        hideNavBars()
    }

    private fun initViewModel() {
       mSearchListModel = ViewModelProviders.of(this).get(SearchListModel::class.java)
        observeViewModel(mSearchListModel)
    }

    private fun observeViewModel(mSearchListModel: SearchListModel) {
        mSearchListModel.getSearchListModel()?.observe(this,  Observer  {
            listingModel-> setTitleAdaptor(listingModel)
        })
    }

    private fun setTitleAdaptor(listingModel: List<ListingModel>) {
        result_spiner_adaptor.setSearchList(listingModel)
        Log.v("==============","================added")
    }

    fun updateCategorySpinner(){
        val aa = ArrayAdapter(this, R.layout.category_spinner, categories)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = aa
        result_spiner_adaptor = SearchListAdapter(this )
        result_spinner.adapter = result_spiner_adaptor
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        hideNavBars()
        dismissKeyboard()
    }

    private lateinit var b_url: StringBuilder

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        hideNavBars()
        dismissKeyboard()
        b_url = StringBuilder(resources.getString(R.string.BASE_URL))
        b_url.append("getAlbumImageAsset/")
        b_url.append(result_spiner_adaptor.getSearchList()?.get(position)?.poster)
        updatedPoster(b_url.toString())
        mSelectedMid = result_spiner_adaptor.getSearchList()?.get(position)?.mid.toString()
    }

    private fun updatedPoster(posterUrl :String) {
        Glide.with(poster_update)
                .load(posterUrl)//poster_update
                .error(Glide.with(poster_update).load(R.drawable.poster_unavailable))
                .into(poster_update)
    }

    override fun onClick(v: View?) {
        if(v==find_btn){
            if (search_text.text.toString()!="") {
                dismissKeyboard()
                mSearchListModel.getSearchList(search_text.text.toString())
                observeViewModel(mSearchListModel)
            }
        }else if(v == gallery)
            selectPoster()
        else if( v== btn_reset)
            updatedPoster(b_url.toString())
        else if (v == btn_update)
            StreamService.getInstance().uploadPoster(mPicturePath,mSelectedMid) { result -> Log.v("==========", "===uploaded==") }
    }

    private fun selectPoster() {
        val pickPhoto = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 1)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED && requestCode ==1) {
            if (resultCode == RESULT_OK && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (selectedImage != null) {
                    val cursor = contentResolver.query(selectedImage,  filePathColumn, null, null, null)
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        poster_update.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                        mPicturePath = picturePath
                        cursor.close()
                    }
                }
            }
        }
    }

    fun dismissKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if(currentFocus !=null)
             imm.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
    fun hideNavBars(){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
