package com.firstproject.favdish.view.fragments

import android.app.Activity
import android.content.Context
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.firstproject.favdish.BR
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.AddUpdateFragmentBinding
import com.firstproject.favdish.model.DialogCustomListModel
import com.firstproject.favdish.utils.*
import com.firstproject.favdish.view.dialogs.DialogCustomList
import com.firstproject.favdish.view.dialogs.customImageSelectionDialog
import com.firstproject.favdish.viewmodels.AddUpdateViewModel
import com.firstproject.favdish.viewmodels.AddUpdateViewModelFactory

class AddUpdateFragment : Fragment(), LifecycleObserver {

    private lateinit var binding: AddUpdateFragmentBinding

    private val addUpdateViewModel: AddUpdateViewModel by activityViewModels {
        AddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private var changeActivity: ChangeActivity? = null

    private val startCameraXApp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val path = result.data?.getStringExtra(IMAGE_URI)
            if (!path.isNullOrEmpty()) {
                addUpdateViewModel.setImagePath(path)
            }

            binding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri.let {
            val path = saveImage(requireActivity(), it)
            addUpdateViewModel.setImagePath(path)

            binding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_vector_edit))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @Suppress("unused")
    fun onCreatedActivity(){
        activity?.lifecycle?.removeObserver(this)

        changeActivity = context as ChangeActivity
//        Log.e(TAG, "set context")

        changeActivity?.hideMenu(false)
//        Log.e(TAG, "hide menu")

//        val fstate = lifecycle.currentState.name
//        val state = activity?.lifecycle?.currentState?.name
//        Log.e(TAG, "lifecycle fun on, state: $state - $fstate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.add_update_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setVariable(BR.myAddUpdateViewModel, addUpdateViewModel)

        binding.ivAddDishImage.setOnClickListener {
            customImageSelectionDialog(this,startCameraXApp, getContent)
        }

        addUpdateViewModel.imagePath.observe(viewLifecycleOwner) {
            Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.ivDishImage)
        }

        addUpdateViewModel.apply {
            type.observe(viewLifecycleOwner) {
                binding.etType.setText(it)
            }
            category.observe(viewLifecycleOwner) {
                binding.etCategory.setText(it)
            }
            cookingTime.observe(viewLifecycleOwner) {
                binding.etCookingTime.setText(it)
            }
        }

        binding.etType.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_type),
                dishTypes(),
                FieldType.DISH_TYPE
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_type")
        }

        binding.etCategory.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_category),
                dishCategories(),
                FieldType.DISH_CATEGORY
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_category")
        }

        binding.etCookingTime.setOnClickListener {
            val dialogParams = DialogCustomListModel(
                resources.getString(R.string.title_select_dish_cooking_time),
                dishCookTime(),
                FieldType.DISH_COOKING_TIME
            )

            DialogCustomList.newInstance(dialogParams)
                .show(requireActivity().supportFragmentManager, "dialog_cooking_time")
        }

        binding.btnAddDish.setOnClickListener {
            addUpdateViewModel.trimValues()

//            val title = binding.etTitle.text.toString().trim { it <= ' ' }
//            val type = binding.etType.text.toString().trim { it <= ' ' }
//            val category = binding.etCategory.text.toString().trim { it <= ' ' }
//            val ingredients = binding.etIngredients.text.toString().trim { it <= ' ' }
//            val cookingTimeInMinutes = binding.etCookingTime.text.toString().trim { it <= ' ' }
//            val cookingDirection = binding.etDirectionToCook.text.toString().trim { it <= ' ' }

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
                    changeActivity?.hideMenu(true)

                    // TODO: 22.09.2021 navigation with popUP
                    requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.navigation_home)
                }
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        changeActivity?.hideMenu(true)
    }

    interface ChangeActivity {
        fun hideMenu(isVisible: Boolean)
    }

    companion object {
        @Suppress("unused")
        fun newInstance() = AddUpdateFragment()
        const val IMAGE_URI = "IMAGE_URI"
    }
}