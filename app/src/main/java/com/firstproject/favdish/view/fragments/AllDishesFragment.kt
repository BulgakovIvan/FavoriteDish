package com.firstproject.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.FragmentAllDishesBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.view.activities.AddUpdateDishActivity
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.view.adapters.FavDishAdapter
import com.firstproject.favdish.viewmodels.AllDishesViewModel
import com.firstproject.favdish.viewmodels.AllDishesViewModelFactory

class AllDishesFragment : Fragment() {

    private val allDishesViewModel: AllDishesViewModel by viewModels {
        AllDishesViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private var _binding: FragmentAllDishesBinding? = null
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
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this)
        binding.rvDishesList.adapter = favDishAdapter

        allDishesViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    binding.rvDishesList.visibility = View.VISIBLE
                    binding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)
                } else {
                    binding.rvDishesList.visibility = View.GONE
                    binding.tvNoDishesAddedYet.visibility = View.VISIBLE
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

    fun dishDetails(favDish: FavDish) {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView(
                AllDishesFragmentDirections.actionNavigationAllDishesToDishDetailsFragment(
                    favDish
                )
            )
        }
    }
}