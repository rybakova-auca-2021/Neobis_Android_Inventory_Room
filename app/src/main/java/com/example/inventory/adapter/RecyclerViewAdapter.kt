package com.example.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventory.Product
import com.example.inventory.databinding.CardBinding


class RecyclerViewAdapter(
    private val products: List<Product>,
    private val onItemClickListener: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    inner class ViewHolder(val binding: CardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                Glide.with(image).load(product.image).into(image)
                title.text = product.name
                price.text = product.price.toString()
                manufacturer.text = product.manufacturer
                quantity.text = product.quantity.toString()
            }
        }
    }

    override fun getItemCount() = products.size
}
