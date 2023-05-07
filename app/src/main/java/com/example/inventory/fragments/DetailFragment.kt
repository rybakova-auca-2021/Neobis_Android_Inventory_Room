package com.example.inventory.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inventory.R
import com.example.inventory.model.Product
import com.example.inventory.databinding.FragmentDetailOfProductBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClassMain

class DetailFragment : Fragment(), Presenter.ProductView {
    private lateinit var binding: FragmentDetailOfProductBinding
    private lateinit var presenter: PresenterClassMain
    private var selectedImageUri: Uri? = null
    private var PICK_IMAGE_REQUEST = 1
    private lateinit var product: Product

    companion object {
        const val ARG_PRODUCT = "products"
        fun newInstance(product: Product): DetailFragment {
            val args = Bundle()
            args.putParcelable(ARG_PRODUCT, product)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable(ARG_PRODUCT) ?: product
    }

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
        getProduct()
        getInfo()
        binding.buttonCancel.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.imageView2.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.imageHolder.setOnClickListener {
            chooseImage()
        }
        showDialog()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
    private fun getProduct() {
        presenter = PresenterClassMain(requireContext())
        presenter.attachView(this)
    }

    private fun getInfo() {
        binding.inputName.setText(product.name)
        binding.inputPrice.setText(product.price)
        binding.inputManufacturer.setText(product.manufacturer)
        binding.inputQuantity.setText(product.quantity)
        Glide.with(this)
            .load(Uri.parse(product.image))
            .into(binding.imageView3)
    }
    private fun editProduct(id:Int) {
        if (binding.name.text.isNotEmpty() && binding.price.text.isNotEmpty() &&
            binding.manufacturer.text.isNotEmpty() && binding.quantity.text.isNotEmpty()
        ) {
            val image = selectedImageUri.toString()
            val name = binding.inputName.text.toString()
            val price = binding.inputPrice.text.toString()
            val manufacturer = binding.inputManufacturer.text.toString()
            val quantity = binding.inputQuantity.text.toString()
            val product = Product(id, image, name, price, manufacturer, quantity, 0)
            presenter.updateProduct(product)
            Toast.makeText(requireContext(), "Product is edited", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
        }
    }
    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Glide.with(this).load(selectedImageUri).into(binding.imageView3)
        }
    }
    override fun showAllProducts(products: List<Product>) {
    }
    @SuppressLint("MissingInflatedId")
    private fun showDialog() {
        binding.buttonSave.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            dialog.apply {
                setTitle("Сохранить изменения?")
                setPositiveButton("Yes") { _, _ ->
                    product.id?.let { it1 -> editProduct(it1) }
                    Toast.makeText(requireActivity(), "Изменения сохранены!", Toast.LENGTH_SHORT)
                        .show()
                }
                setNegativeButton("No") { _, _ ->
                    Toast.makeText(requireActivity(), "Отменено", Toast.LENGTH_SHORT).show()
                }
            }.create()
            dialog.show()
        }
    }
}