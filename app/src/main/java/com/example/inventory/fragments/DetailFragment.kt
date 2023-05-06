package com.example.inventory.fragments

import RecyclerViewAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.inventory.R
import com.example.inventory.model.Product
import com.example.inventory.databinding.FragmentDetailOfProductBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClass

class DetailFragment : Fragment(), Presenter.ProductView {
    private lateinit var binding: FragmentDetailOfProductBinding
    private lateinit var presenter: PresenterClass
    private var selectedImageUri: Uri? = null
    private var PICK_IMAGE_REQUEST = 1
    private lateinit var product: Product

    companion object {
        private const val ARG_PRODUCT = "products"
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
        editProduct()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
    private fun getProduct() {
        presenter = PresenterClass(requireContext())
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
    private fun editProduct() {
        binding.buttonSave.setOnClickListener {
            if (binding.name.text.isNotEmpty() && binding.price.text.isNotEmpty() &&
                binding.manufacturer.text.isNotEmpty() && binding.quantity.text.isNotEmpty()
            ) {
                val product = Product(
                    image = selectedImageUri.toString(),
                    name = binding.inputName.text.toString(),
                    price = binding.inputPrice.text.toString(),
                    manufacturer = binding.inputManufacturer.text.toString(),
                    quantity = binding.inputQuantity.text.toString()
                )
                presenter.updateProduct(product)
                Toast.makeText(requireContext(), "Product is edited", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
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
}