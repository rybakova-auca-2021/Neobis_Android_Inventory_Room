package com.example.inventory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inventory.Product
import com.example.inventory.databinding.FragmentDetailOfProductBinding

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailOfProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailOfProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product: Product? = arguments?.getParcelable("product")
        product?.let {
            binding.name.text = it.name
            binding.price.text = it.price
            binding.manufacturer.text = it.manufacturer
            binding.quantity.text = it.quantity
            // Load image using Picasso or Glide
            Glide.with(this).load(it.image).into(binding.imageView2)
        }
    }
}