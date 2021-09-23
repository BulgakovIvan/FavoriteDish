package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firstproject.favdish.databinding.FragmentRandomDishBinding
import com.firstproject.favdish.viewmodels.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private lateinit var randomDishViewModel: RandomDishViewModel

    private var _binding: FragmentRandomDishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomDishViewModel =
            ViewModelProvider(this).get(RandomDishViewModel::class.java)

        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        randomDishViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}