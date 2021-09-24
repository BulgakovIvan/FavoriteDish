package com.firstproject.favdish.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.FragmentDishDetailsBinding
import com.firstproject.favdish.utils.TAG
import com.firstproject.favdish.viewmodels.AllDishesViewModel
import com.firstproject.favdish.viewmodels.AllDishesViewModelFactory
import java.io.IOException
import java.util.*

class DishDetailsFragment : Fragment() {

    private var _binding: FragmentDishDetailsBinding? = null
    private val binding get() = _binding!!

    private val allDishesViewModel: AllDishesViewModel by viewModels {
        AllDishesViewModelFactory((requireActivity().application as FavDishApplication).repository)
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
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "Error loading image", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.toBitmap()?.let { res ->
                                Palette.from(res).generate{ palette ->
                                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                    binding.rlDishDetailMain.setBackgroundColor(intColor)
                                }
                            }
                            return false
                        }
                    } )
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

            if (args.dishDetails.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(
                    requireActivity().getDrawable(R.drawable.ic_favorite_selected)
                )
            } else {
                binding.ivFavoriteDish.setImageDrawable(
                    requireActivity().getDrawable(R.drawable.ic_favorite_unselected)
                )
            }
        }

        binding.ivFavoriteDish.setOnClickListener {
            args.let {
                args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

                allDishesViewModel.update(args.dishDetails)

                if (args.dishDetails.favoriteDish) {
                    binding.ivFavoriteDish.setImageDrawable(
                        requireActivity().getDrawable(R.drawable.ic_favorite_selected)
                    )
                    Toast.makeText(
                        requireActivity(),
                        requireActivity().getString(R.string.msg_added_to_favorites),
                        Toast.LENGTH_SHORT).show()
                } else {
                    binding.ivFavoriteDish.setImageDrawable(
                        requireActivity().getDrawable(R.drawable.ic_favorite_unselected)
                    )
                    Toast.makeText(
                        requireActivity(),
                        requireActivity().getString(R.string.msg_removed_from_favorite),
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}