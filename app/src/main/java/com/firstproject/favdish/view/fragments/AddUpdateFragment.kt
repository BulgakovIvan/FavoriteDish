package com.firstproject.favdish.view.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.firstproject.favdish.R
import com.firstproject.favdish.utils.TAG
import com.firstproject.favdish.viewmodels.AddUpdateViewModel
import androidx.core.app.NavUtils




class AddUpdateFragment : Fragment(), LifecycleObserver {

    private lateinit var viewModel: AddUpdateViewModel
    var changeActivity: ChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreatedActivity(){
        activity?.lifecycle?.removeObserver(this)

        changeActivity = context as ChangeActivity
//        Log.e(TAG, "set context")

        changeActivity?.hideMenu(false)
//        Log.e(TAG, "hide menu")

//        val fstate = lifecycle.currentState.name
//        val state = activity?.lifecycle?.currentState?.name
//        Log.e(TAG, "lifecycle fun on, state: $state - $fstate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_update_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val set: String = activity?.actionBar?.displayOptions.toString()
        Log.e(TAG, set)


        viewModel = ViewModelProvider(this).get(AddUpdateViewModel::class.java)
        // TODO: change like in AllDish

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        this is work
//        changeActivity?.hideMenu(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        changeActivity?.hideMenu(true)
    }





    interface ChangeActivity {
        fun hideMenu(isVisible: Boolean)
    }

    companion object {
        fun newInstance() = AddUpdateFragment()
    }
}