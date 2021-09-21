package com.firstproject.favdish.view.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.AddUpdateFragmentBinding
import com.firstproject.favdish.model.DialogCustomListModel
import com.firstproject.favdish.utils.*
import com.firstproject.favdish.view.dialogs.DialogCustomList
import com.firstproject.favdish.view.dialogs.customImageSelectionDialog
import com.firstproject.favdish.viewmodels.AddUpdateViewModel

class AddUpdateFragment : Fragment(), LifecycleObserver {

    private var _binding: AddUpdateFragmentBinding? = null
    private val binding get() = _binding!!

    private val addUpdateViewModel: AddUpdateViewModel by activityViewModels()
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
        _binding = AddUpdateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    override fun onDestroy() {
        super.onDestroy()
        changeActivity?.hideMenu(true)
        _binding = null
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