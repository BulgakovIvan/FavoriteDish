package com.firstproject.favdish.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firstproject.favdish.databinding.ItemDishLayoutBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.view.fragments.AllDishesFragment
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
    }

    inner class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        private val ivDishImage = view.ivDishImage
        private val tvTitle = view.tvDishTitle

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