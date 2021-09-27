package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.FragmentFavoriteDishesBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.utils.TAG
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.view.adapters.FavDishListAdapter
import com.firstproject.favdish.viewmodels.AllDishesViewModel
import com.firstproject.favdish.viewmodels.AllDishesViewModelFactory
import com.firstproject.favdish.viewmodels.FavoriteDishesViewModel

class FavoriteDishesFragment : Fragment() {

    private lateinit var favoriteDishesViewModel: FavoriteDishesViewModel

    private val allDishesViewModel: AllDishesViewModel by viewModels {
        AllDishesViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private var _binding: FragmentFavoriteDishesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoriteDishesViewModel =
            ViewModelProvider(this).get(FavoriteDishesViewModel::class.java)
        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishListAdapter(this)
        binding.rvDishesList.adapter = favDishAdapter

        allDishesViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    binding.rvDishesList.visibility = View.VISIBLE
                    binding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.submitList(it)
                } else {
                    binding.rvDishesList.visibility = View.GONE
                    binding.tvNoDishesAddedYet.visibility = View.VISIBLE

                    Log.e(TAG, "List of favorite dishes is empty!")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigationView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun dishDetails(favDish: FavDish) {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView(
                FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToNavigationDishDetail(
                    favDish
                )
            )
        }
    }
}
