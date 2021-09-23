package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.FragmentDishDetailsBinding
import java.io.IOException
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DishDetailsFragment : Fragment() {

    private var _binding: FragmentDishDetailsBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDishDetailsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        args.let {
            try {
                // Load the dish image in the ImageView.
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .into(binding.ivDishImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            binding.tvTitle.text = it.dishDetails.title
            binding.tvType.text =
                it.dishDetails.type.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString() }
            binding.tvCategory.text = it.dishDetails.category
            binding.tvIngredients.text = it.dishDetails.ingredients
            binding.tvCookingDirection.text = it.dishDetails.directionToCook
            binding.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, it.dishDetails.cookingTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DishDetailsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}