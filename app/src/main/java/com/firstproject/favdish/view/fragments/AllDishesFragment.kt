package com.firstproject.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.FragmentAllDishesBinding
import com.firstproject.favdish.view.activities.AddUpdateDishActivity
import com.firstproject.favdish.viewmodels.HomeViewModel

class AllDishesFragment : Fragment() {

    // TODO: 17.09.2021 view model
    private val homeViewModel: HomeViewModel by viewModels()
//    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentAllDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO: 17.09.2021 del viewmodel provider???
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        val homeViewModel by viewModels<HomeViewModel>()
//        val homeViewModel: HomeViewModel by viewModels()

        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_all_dishes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}