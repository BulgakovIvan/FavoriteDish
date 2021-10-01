package com.firstproject.favdish.view.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.FragmentRandomDishBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.model.entities.RandomDish
import com.firstproject.favdish.utils.DISH_IMAGE_SOURCE_ONLINE
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.viewmodels.*

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

        binding.srlRandomDish.setOnRefreshListener {
            randomDishViewModel.getRandomRecipeFromAPI()
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

    private fun randomDishViewModelObserver() {
        randomDishViewModel.randomDishResponse.observe(viewLifecycleOwner){ response ->
            response.let {
                Log.e("ups", it.recipes[0].title)
                setRandomDishResponseInUI(response.recipes[0])

                if (binding.srlRandomDish.isRefreshing) {
                    binding.srlRandomDish.isRefreshing = false
                }
            }
        }

        randomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner) {
            Log.e("ups", "Random dish error: $it")

            if (binding.srlRandomDish.isRefreshing) {
                binding.srlRandomDish.isRefreshing = false
            }
        }

        randomDishViewModel.loadRandomDish.observe(viewLifecycleOwner){
            Log.e("ups", "Random dish loading: $it")
        }
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(binding.ivDishImage)

        binding.tvTitle.text = recipe.title

        var dishType = "other"
        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            binding.tvType.text = dishType
        }

        binding.tvCategory.text = "other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {
            ingredients = if (ingredients.isEmpty()) {
                value.original
            } else {
                ingredients + ", \n" + value.original
            }
        }
        binding.tvIngredients.text = ingredients

        binding.tvCookingDirection.text = Html.fromHtml(
            recipe.instructions,
            Html.FROM_HTML_MODE_COMPACT
        )

        binding.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        binding.ivFavoriteDish.setImageDrawable(
            getDrawable(requireActivity(), R.drawable.ic_favorite_unselected)
        )

        var addedToFavorites = false

        binding.ivFavoriteDish.setOnClickListener {

            if (addedToFavorites) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                addedToFavorites = true

                val randomDishDetails = FavDish(
                    recipe.image,
                    DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

                val addUpdateViewModel: AddUpdateViewModel by viewModels {
                    AddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
                }
                addUpdateViewModel.insert(randomDishDetails)

                binding.ivFavoriteDish.setImageDrawable(
                    getDrawable(requireActivity(), R.drawable.ic_favorite_selected)
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }
}