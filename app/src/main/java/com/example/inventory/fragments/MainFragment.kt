package com.example.inventory.fragments

import RecyclerViewAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventory.model.Product
import com.example.inventory.R
import com.example.inventory.databinding.FragmentMainBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClassMain
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainFragment : Fragment(), Presenter.ProductView {
    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { RecyclerViewAdapter() }
    private lateinit var presenter: PresenterClassMain
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        presenter = PresenterClassMain(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getProduct()
        setupAddButton()
    }

    private fun getProduct() {
        presenter.attachView(this)
        presenter.getAllProducts()
    }

    private fun setupRecyclerView() {
        binding.rvMain.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMain.adapter = adapter
        adapter.setOnItemClick(object : RecyclerViewAdapter.ListClickListener<Product> {
            override fun onClick(data: Product, position: Int) {
                val fragment = DetailFragment()
                fragment.arguments = Bundle().apply { putParcelable("products", data) }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onThreeDotsClick(data: Product, position: Int) {
                openBottomDialog()
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
    override fun onResume() {
        super.onResume()
        presenter.getAllProducts()
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun showAllProducts(products: List<Product>) {
        Log.e("Test", "showAllProductsFragment")
        adapter.updateProduct(products)
    }
    fun archiveProduct(product: Product){
        this.product = Product(product.id, product.name, product.price, product.manufacturer, product.quantity, product.image)
        presenter.updateProduct(product)
    }
    private fun openBottomDialog() {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.main_bottom_sheet, null)
            dialog.setCancelable(true)
            dialog.setContentView(view)

            val archiveTextView = view.findViewById<TextView>(R.id.archiveButton)
            archiveTextView.setOnClickListener {
                val startDialogFragment = AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
                startDialogFragment.apply {
                    setTitle("Архивировать из каталога?")
                    setPositiveButton("Yes") { _, _ ->
                        archiveProduct(product)
                        dialog.dismiss()
                    }
                    setNegativeButton("No") { _, _ ->
                        dialog.dismiss()
                    }
                }.create()
                startDialogFragment.show()
            }
        dialog.show()
    }
}
