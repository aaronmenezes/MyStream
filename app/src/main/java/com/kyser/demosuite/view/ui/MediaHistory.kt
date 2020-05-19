package com.kyser.demosuite.view.ui

import com.google.android.material.tabs.TabLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager

import com.kyser.demosuite.R
import com.kyser.demosuite.service.model.ListingModel
import com.kyser.demosuite.view.ui.adaptor.MediaListAdaptor
import com.kyser.demosuite.view.ui.components.SpaceItemDecoration
import com.kyser.demosuite.viewmodel.HistoryListModel
import kotlinx.android.synthetic.main.activity_media_hsitory.*
import kotlinx.android.synthetic.main.fragment_media_hsitory.view.*

class MediaHistory : AppCompatActivity() {

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_hsitory)
        hideNavBars()

        // setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show()
    }


    override fun onResume() {
        super.onResume()
        hideNavBars()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_media_hsitory, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment(), MediaListAdaptor.ItemSelection {

        private lateinit var mMediaListAdaptor: MediaListAdaptor
        private lateinit var mListviewModel: HistoryListModel

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            val rootView = inflater.inflate(R.layout.fragment_media_hsitory, container, false)
            rootView.section_label.text = getString(R.string.section_format)
            if( arguments?.getInt(ARG_SECTION_NUMBER) == 1){
                mMediaListAdaptor = MediaListAdaptor(context, this)
                rootView.history_grid.layoutManager = GridLayoutManager(context,3)
                rootView.history_grid.adapter = mMediaListAdaptor
                rootView.history_grid.addItemDecoration(SpaceItemDecoration(30, 1))
                initViewModel()
            }
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

        private fun initViewModel() {
            mListviewModel = ViewModelProviders.of(this).get(HistoryListModel::class.java)
            observeViewModel(mListviewModel)
        }

        private fun observeViewModel(viewModel: HistoryListModel) {
            if (viewModel.historyListObservable != null)
                viewModel.historyListObservable.observe(this, Observer { projects ->
                    if (projects != null) {
                        mMediaListAdaptor.setCategoryList(projects)
                        if(projects.size >0)
                            view?.section_label?.visibility = GONE
                        else
                            view?.section_label?.visibility = VISIBLE
                    }
                })
        }
        override fun onItemSelection(MediaList: ListingModel?, position: Int) {

        }
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
