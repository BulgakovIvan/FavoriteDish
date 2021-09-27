package com.firstproject.favdish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstproject.favdish.R
import com.firstproject.favdish.application.FavDishApplication
import com.firstproject.favdish.databinding.DialogCustomListBinding
import com.firstproject.favdish.databinding.FragmentAllDishesBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.utils.ALL_ITEMS
import com.firstproject.favdish.utils.FILTER_SELECTION
import com.firstproject.favdish.utils.dishTypes
import com.firstproject.favdish.view.activities.AddUpdateDishActivity
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.view.adapters.CustomListItemAdapter
import com.firstproject.favdish.view.adapters.FavDishListAdapter
import com.firstproject.favdish.viewmodels.AllDishesViewModel
import com.firstproject.favdish.viewmodels.AllDishesViewModelFactory

class AllDishesFragment : Fragment() {

    private val allDishesViewModel: AllDishesViewModel by viewModels {
        AllDishesViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private var _binding: FragmentAllDishesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favDishAdapter: FavDishListAdapter
    private lateinit var customListDialog: Dialog

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
//        val favDishAdapter = FavDishAdapter(this)
//        with ListAdapter
        favDishAdapter = FavDishListAdapter(this)

        binding.rvDishesList.adapter = favDishAdapter

        allDishesViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    binding.rvDishesList.visibility = View.VISIBLE
                    binding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.submitList(it)
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
            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun dishDetails(favDish: FavDish) {
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView(
                AllDishesFragmentDirections
                    .actionNavigationAllDishesToDishDetailsFragment(
                    favDish
                )
            )
        }
    }

    fun deleteDish(dish: FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, dish.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            allDishesViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog() {
        customListDialog = Dialog(requireActivity())

        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        customListDialog.setContentView(binding.root)

        binding.tvDialogTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = dishTypes()
        dishTypes.add(0, ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(
            requireActivity(),
            this,
            dishTypes,
            FILTER_SELECTION
        )

        binding.rvList.adapter = adapter
        customListDialog.show()
    }

    fun filterSelection(filterItemSelection: String) {
        customListDialog.dismiss()

        if (filterItemSelection == ALL_ITEMS) {
            allDishesViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE

                        favDishAdapter.submitList(it)
                    } else {
                        binding.rvDishesList.visibility = View.GONE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            allDishesViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE

                        favDishAdapter.submitList(it)
                    } else {
                        binding.rvDishesList.visibility = View.GONE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}