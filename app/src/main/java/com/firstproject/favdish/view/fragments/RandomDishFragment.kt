package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firstproject.favdish.databinding.FragmentRandomDishBinding
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.viewmodels.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private val randomDishViewModel: RandomDishViewModel by viewModels()

    private var _binding: FragmentRandomDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomDishViewModelObserver()
        randomDishViewModel.getRandomRecipeFromAPI()
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

    private fun randomDishViewModelObserver() {
        randomDishViewModel.randomDishResponse.observe(viewLifecycleOwner){ response ->
            response.let {
                Log.e("ups", it.recipes[0].title)
            }
        }

        randomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner) {
            Log.e("ups", "Random dish error: $it")
        }

        randomDishViewModel.loadRandomDish.observe(viewLifecycleOwner){
            Log.e("ups", "Random dish loading: $it")
        }
    }
}