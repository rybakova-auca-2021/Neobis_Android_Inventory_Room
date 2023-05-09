package com.example.inventory.fragments

import RecyclerViewAdapter
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventory.R
import com.example.inventory.databinding.FragmentArchiveBinding
import com.example.inventory.model.Product
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClassArchived
import com.google.android.material.bottomsheet.BottomSheetDialog

class ArchiveFragment : Fragment(), Presenter.ArchivedView {
    lateinit var binding: FragmentArchiveBinding
    private val adapter by lazy { RecyclerViewAdapter() }
    private lateinit var presenter: PresenterClassArchived

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        presenter = PresenterClassArchived(requireContext())
        presenter.attachView(this)
        presenter.getAllArchived()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewArchive.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewArchive.adapter = adapter
        setupClickListeners()
    }

    private fun setupClickListeners() {
        adapter.setOnItemClick(object : RecyclerViewAdapter.ListClickListener<Product> {
            override fun onClick(data: Product, position: Int) {
                navigateToDetailFragment(data)
            }

            override fun onThreeDotsClick(data: Product, position: Int) {
                showBottomSheetDialog(data)
            }
        })
    }

    private fun navigateToDetailFragment(data: Product) {
        val fragment = DetailFragment()
        fragment.arguments = Bundle().apply { putParcelable("products", data) }
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showBottomSheetDialog(data: Product) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.archive_bottom_sheet, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)

        setupDeleteAction(dialog, data)
        setupRestoreAction(dialog, data)

        dialog.show()
    }

    private fun setupDeleteAction(dialog: BottomSheetDialog, data: Product) {
        val deleteTextView = dialog.findViewById<TextView>(R.id.delete)
        deleteTextView?.setOnClickListener {
            showDeleteConfirmationDialog(data, dialog)
        }
    }

    private fun showDeleteConfirmationDialog(data: Product, dialog: BottomSheetDialog) {
        val startDialogFragment = AlertDialog.Builder(requireContext())
            .setTitle("Удалить ${data.name} из архива?")
            .setPositiveButton("Да") { _, _ ->
                presenter.deleteProduct(data)
                dialog.dismiss()
            }
            .setNegativeButton("Нет"){ _, _ ->
                dialog.dismiss()
            }
            .create()
        startDialogFragment.show()
    }

    private fun setupRestoreAction(dialog: BottomSheetDialog, data: Product) {
        val restoreTextView = dialog.findViewById<TextView>(R.id.restore)
        restoreTextView?.setOnClickListener {
            showRestoreConfirmationDialog(data, dialog)
        }
    }

    private fun showRestoreConfirmationDialog(data: Product, dialog: BottomSheetDialog) {
        val startDialogFragment = AlertDialog.Builder(requireContext())
            .setTitle("Восстановить ${data.name} из архива?")
            .setPositiveButton("Да") { _, _ ->
                restoreProduct(data)
                dialog.dismiss()
            }
            .setNegativeButton("Нет") { _, _ ->
                dialog.dismiss()
            }
            .create()
        startDialogFragment.show()
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllArchived()
    }

    fun restoreProduct(product: Product){
        val restoredProduct = Product(product.id, product.image, product.name, product.price, product.manufacturer, product.quantity, 0)
        presenter.updateProduct(restoredProduct)
    }

    override fun showAllArchived(products: List<Product>) {
        adapter.updateProduct(products)
    }

    override fun showAllProducts(products: List<Product>) {
    }
}
