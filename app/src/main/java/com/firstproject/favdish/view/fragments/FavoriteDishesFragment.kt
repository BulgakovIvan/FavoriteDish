package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firstproject.favdish.databinding.FragmentFavoriteDishesBinding
import com.firstproject.favdish.viewmodels.FavoriteDishesViewModel

class FavoriteDishesFragment : Fragment() {

    private lateinit var favoriteDishesViewModel: FavoriteDishesViewModel

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
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        favoriteDishesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}