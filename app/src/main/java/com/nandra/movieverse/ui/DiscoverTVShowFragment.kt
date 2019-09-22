package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverRecyclerViewAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DiscoverTVShowFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.isLoading.observe(this, Observer {
            loadingIndicator(it)
        })
        sharedViewModel.isError.observe(this, Observer {
            errorIndicator(it)
        })
        sharedViewModel.listTVLive.observe(this, Observer {
            tvshow_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(it, Constant.TV_FILM_TYPE), true)
        })
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        tvshow_recyclerview.apply {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 3)
        }
        attemptPrepareView()
    }

    private fun loadingIndicator(state: Boolean) {
        if (state) {
            tvshow_loading_back.visibility = View.VISIBLE
        }
        else {
            tvshow_loading_back.visibility = View.GONE
        }
    }

    private fun errorIndicator(state: Boolean){
        if(state){
            tvshow_error_back.visibility = View.VISIBLE
            viewLanguageAdjustment()
            tvshow_error_button.setOnClickListener {
                prepareTVShowListView()
            }
        } else {
            tvshow_error_back.visibility = View.GONE
        }
    }

    private fun attemptPrepareView() {
        if(sharedViewModel.isError.value == true) {
            errorIndicator(sharedViewModel.isError.value!!)
            return
        }
        prepareTVShowListView()
    }

    private fun prepareTVShowListView() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        Glide.with(this)
            .load(R.drawable.img_loading_indicator)
            .into(tvshow_loading_image)
        if (sharedViewModel.isDataHasLoaded)
            tvshow_recyclerview.swapAdapter(DiscoverRecyclerViewAdapter(sharedViewModel.listTVLive.value!!, Constant.TV_FILM_TYPE), true)
        else {
            scope.launch {
                sharedViewModel.requestDiscoverData()
            }
        }
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    private fun viewLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue)
            tvshow_error_button.text = getString(R.string.button_try_again_en)
        else
            tvshow_error_button.text = getString(R.string.button_try_again_id)
    }
}