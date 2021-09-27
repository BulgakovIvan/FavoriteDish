package com.firstproject.favdish.view.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.firstproject.favdish.BR
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.AddUpdateFragmentBinding
import com.firstproject.favdish.model.DialogCustomListModel
import com.firstproject.favdish.utils.*
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.view.dialogs.DialogCustomList
import com.firstproject.favdish.view.dialogs.customImageSelectionDialog
import com.firstproject.favdish.viewmodels.AddUpdateViewModel
import com.firstproject.favdish.viewmodels.AddUpdateViewModelFactory

class AddUpdateFragment : Fragment(), LifecycleObserver {

    private lateinit var dataBinding: AddUpdateFragmentBinding
    private var isUpdate: Boolean = false

    private val addUpdateViewModel: AddUpdateViewModel by activityViewModels {
        AddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private val startCameraXApp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val path = result.data?.getStringExtra(IMAGE_URI)
            if (!path.isNullOrEmpty()) {
                addUpdateViewModel.setImagePath(path)
            }

            dataBinding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri.let {
            val path = saveImage(requireActivity(), it)
            addUpdateViewModel.setImagePath(path)

            dataBinding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_update_fragment, container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: AddUpdateFragmentArgs by navArgs()
        if (args.dishDetails != null) {

            isUpdate = true
            addUpdateViewModel.loadDishDetails(args.dishDetails!!)
            // TODO: 27.09.2021 clean viewModel if not updatet

            dataBinding.btnAddDish.text = "update"








        } else {
            Log.e(TAG, "without details")
        }

        dataBinding.setVariable(BR.myAddUpdateViewModel, addUpdateViewModel)

        dataBinding.ivAddDishImage.setOnClickListener {
            customImageSelectionDialog(this,startCameraXApp, getContent)
        }

        addUpdateViewModel.imagePath.observe(viewLifecycleOwner) {
            Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(dataBinding.ivDishImage)
        }

        addUpdateViewModel.apply {
            type.observe(viewLifecycleOwner) {
                dataBinding.etType.setText(it)
            }
            category.observe(viewLifecycleOwner) {
                dataBinding.etCategory.setText(it)
            }
            cookingTime.observe(viewLifecycleOwner) {
                dataBinding.etCookingTime.setText(it)
            }
        }

        dataBinding.etType.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_type),
                dishTypes(),
                FieldType.DISH_TYPE
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_type")
        }

        dataBinding.etCategory.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_category),
                dishCategories(),
                FieldType.DISH_CATEGORY
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_category")
        }

        dataBinding.etCookingTime.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_cooking_time),
                dishCookTime(),
                FieldType.DISH_COOKING_TIME
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_cooking_time")
        }

        dataBinding.btnAddDish.setOnClickListener {

            if (isUpdate) {
                // TODO: 27.09.2021 update values
                addUpdateViewModel.update()
            } else {
                addUpdateViewModel.trimValues()

                when {
                    addUpdateViewModel.imagePath.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_select_dish_image))
                    }
                    addUpdateViewModel.title.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_enter_dish_title))
                    }
                    addUpdateViewModel.type.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_select_dish_type))
                    }
                    addUpdateViewModel.category.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_select_dish_category))
                    }
                    addUpdateViewModel.ingredients.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_enter_dish_ingredients))
                    }
                    addUpdateViewModel.cookingTime.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_select_dish_cooking_time))
                    }
                    addUpdateViewModel.instruction.value.isNullOrEmpty() -> {
                        makeToast(resources.getString(R.string.err_msg_enter_dish_cooking_instructions))
                    }
                    else -> {
                        makeToast("All the entries are valid.")
                        addUpdateViewModel.insert()

//                    requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
//                        .navigate(R.id.navigation_all_dishes)
                    }
                }
            }

            Navigation.findNavController(it).navigate(
                AddUpdateFragmentDirections.actionNavigationAddUpdateToNavigationAllDishes()
            )
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val IMAGE_URI = "IMAGE_URI"
    }
}