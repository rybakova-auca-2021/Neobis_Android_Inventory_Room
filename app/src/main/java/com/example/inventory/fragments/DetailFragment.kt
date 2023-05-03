package com.example.inventory.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inventory.R
import com.example.inventory.model.Product
import com.example.inventory.databinding.FragmentDetailOfProductBinding
import com.example.inventory.presenter.PresenterClass

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailOfProductBinding
    private lateinit var presenter: PresenterClass
    private var selectedImageUri: Uri? = null
    private var PICK_IMAGE_REQUEST = 1

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
        binding.button.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        editProduct()
    }
    private fun getProduct() {
        presenter = PresenterClass(requireContext())
    }

    private fun editProduct() {
        binding.imageHolder.setOnClickListener {
            chooseImage()
        }
        binding.apply {
            buttonSave.setOnClickListener {
                if (binding.name.text.isNotEmpty() && binding.price.text.isNotEmpty() &&
                    binding.manufacturer.text.isNotEmpty() && binding.quantity.text.isNotEmpty()
                ) {
                    val product = Product(
                        image = selectedImageUri.toString(),
                        name = binding.name.toString(),
                        price = binding.price.toString(),
                        manufacturer = binding.manufacturer.toString(),
                        quantity = binding.quantity.toString()
                    )
                    presenter.updateProduct(product)
                    Toast.makeText(requireContext(), "Product is edited", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
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
}