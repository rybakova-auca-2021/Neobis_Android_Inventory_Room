package com.example.inventory.fragments

import RecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventory.Product
import com.example.inventory.R
import com.example.inventory.database.ProductRepository
import com.example.inventory.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { RecyclerViewAdapter() }
    private val repo by lazy { ProductRepository(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAddButton()
    }

    override fun onResume() {
        super.onResume()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        binding.rvMain.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMain.adapter = adapter
        adapter.setOnItemClick(object : RecyclerViewAdapter.ListClickListener<Product> {
            override fun onClick(data: Product, position: Int) {
                val fragment = AddProductFragment()
                fragment.arguments = Bundle().apply { putParcelable("product", data) }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onDelete(product: Product) {
                repo.deleteProduct(product)
                fetchProducts()
            }
        })
    }
    private fun setupAddButton() {
        binding.addButtonMain.setOnClickListener {
            val fragment = AddProductFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun fetchProducts() {
        val products = repo.getAllProducts()
        adapter.setProduct(products)
    }
}
