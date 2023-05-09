package com.example.inventory.fragments

import RecyclerViewAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Query
import com.example.inventory.model.Product
import com.example.inventory.R
import com.example.inventory.databinding.FragmentMainBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClassMain
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainFragment : Fragment(), Presenter.ProductView {

    private lateinit var binding: FragmentMainBinding
    private lateinit var presenter: PresenterClassMain
    private val adapter by lazy { RecyclerViewAdapter() }

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

        presenter.attachView(this)
        presenter.getAllProducts()
        setupRecyclerView()
        setupAddButton()
        search()
    }

    private fun setupRecyclerView() {
        binding.rvMain.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = this@MainFragment.adapter
        }

        adapter.setOnItemClick(object : RecyclerViewAdapter.ListClickListener<Product> {
            override fun onClick(data: Product, position: Int) {
                val fragment = DetailFragment().apply {
                    arguments = Bundle().apply { putParcelable("products", data) }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onThreeDotsClick(data: Product, position: Int) {
                val dialog = createBottomSheetDialog()
                val archiveTextView = dialog.findViewById<TextView>(R.id.archiveButton)
                archiveTextView?.setOnClickListener {
                    dialog.dismiss()
                    showArchiveConfirmationDialog(data)
                }
                dialog.show()
            }
        })
    }

    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.main_bottom_sheet, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        return dialog
    }

    private fun showArchiveConfirmationDialog(data: Product) {
        AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setTitle("Архивировать ${data.name} из каталога?")
            .setPositiveButton("Да") { _, _ ->
                archiveProduct(data)
            }
            .setNegativeButton("Нет", null)
            .create()
            .show()
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

    private fun performSearch(query: String) {
        presenter.searchProductsByName(query)
    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query.let {
                    if (it != null) {
                        performSearch(it)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showAllProducts(products: List<Product>) {
        Log.e("Test", "showAllProductsFragment")
        adapter.updateProduct(products)
    }

    private fun archiveProduct(product: Product) {
        val archivedProduct = Product(product.id, product.image, product.name, product.price, product.manufacturer, product.quantity, 1)
        presenter.updateProduct(archivedProduct)
    }
}
