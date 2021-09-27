package com.firstproject.favdish.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ItemDishLayoutBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.view.activities.MainActivity
import com.firstproject.favdish.view.fragments.AllDishesFragment
import com.firstproject.favdish.view.fragments.AllDishesFragmentDirections
import com.firstproject.favdish.view.fragments.FavoriteDishesFragment

class FavDishListAdapter(private val fragment: Fragment) : ListAdapter<FavDish, FavDishListAdapter.ViewHolder>(DISH_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current.image, current.title)

        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment) {
                fragment.dishDetails(current)
            }
            if (fragment is FavoriteDishesFragment) {
                fragment.dishDetails(current)
            }
        }

        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            val destination = AllDishesFragmentDirections
                .actionNavigationAllDishesToNavigationAddUpdate(current, "Update")

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {
                    if (fragment.requireActivity() is MainActivity) {
                        (fragment.requireActivity() as MainActivity)
                            .hideBottomNavigationView(destination)
                    }

                } else if (it.itemId == R.id.action_delete_dish) {
                    if (fragment is AllDishesFragment) {
                        fragment.deleteDish(current)
                    }
                }
                true
            }

            popup.show()
        }

        if (fragment is AllDishesFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else if (fragment is FavoriteDishesFragment) {
            holder.ibMore.visibility = View.GONE
        }
    }

    inner class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        private val ivDishImage = view.ivDishImage
        private val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore

        fun bind(imagePath: String, title: String) {
            Glide.with(fragment)
                .load(imagePath)
                .into(ivDishImage)
            tvTitle.text = title
        }
    }

    companion object {
        private val DISH_COMPARATOR = object : DiffUtil.ItemCallback<FavDish>() {
            override fun areItemsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
                return oldItem == newItem
            }
        }
    }


}