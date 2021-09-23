package com.firstproject.favdish.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firstproject.favdish.databinding.ItemDishLayoutBinding
import com.firstproject.favdish.model.entities.FavDish
import com.firstproject.favdish.view.fragments.AllDishesFragment

// TODO: 22.09.2021 change to list adapter, with databind adapter???
class FavDishAdapter(private val fragment: AllDishesFragment)
    :RecyclerView.Adapter<FavDishAdapter.ViewHolder>(){

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title

        holder.itemView.setOnClickListener {
            fragment.dishDetails()
        }
    }

    override fun getItemCount(): Int = dishes.size

    @SuppressLint("NotifyDataSetChanged")
    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }
}