package com.example.inventory.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inventory.model.Product
import com.example.inventory.R
import com.example.inventory.databinding.FragmentAddProductBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClassMain

class AddProductFragment : Fragment(), Presenter.ProductView {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var presenter: PresenterClassMain
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = PresenterClassMain(requireContext())
        presenter.attachView(this)
        binding.backButton.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.buttonCancel.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.imageHolder.setOnClickListener {
            chooseImage()
        }
        insertProduct()
    }

    private fun insertProduct() {
        binding.buttonAdd.setOnClickListener {
            val image = selectedImageUri.toString()
            val name = binding.inputName.text.toString()
            val price = binding.inputPrice.text.toString()
            val manufacturer = binding.inputManufacturer.text.toString()
            val quantity = binding.inputQuantity.text.toString()
            val product = Product(null, image, name, price, manufacturer, quantity, 0)
            presenter.addProduct(product)
            Toast.makeText(requireContext(), "Product is added", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Glide.with(this).load(selectedImageUri).into(binding.imageView3)
        }
    }
    override fun showAllProducts(products: List<Product>) {
    }
}
