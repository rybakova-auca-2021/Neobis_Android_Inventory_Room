package com.example.inventory.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.inventory.Product
import com.example.inventory.R
import com.example.inventory.database.ProductRepository
import com.example.inventory.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private var product: Product? = null
    private val PICK_IMAGE_REQUEST = 1
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
        binding.backButton.setOnClickListener {
            val fragment = MainFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.imageHolder.setOnClickListener {
            chooseImage()
        }
        product?.let {
            binding.name.text = it.name
            binding.price.text = it.price
            binding.manufacturer.text = it.manufacturer
            binding.quantity.text = it.quantity
        }

        val repo = ProductRepository(requireContext())

        binding.button2.setOnClickListener {
            if (binding.name.text.isNotEmpty() && binding.price.text.isNotEmpty() &&
                binding.manufacturer.text.isNotEmpty() && binding.quantity.text.isNotEmpty()) {
                product?.let {
                    val product = Product(
                        image = selectedImageUri.toString(),
                        name = binding.name.toString(),
                        price = binding.price.toString(),
                        manufacturer = binding.manufacturer.toString(),
                        quantity = binding.quantity.toString())
                    repo.updateProduct(product)
                } ?: kotlin.run {
                    val product = Product(
                        image = selectedImageUri.toString(),
                        name = binding.name.toString(),
                        price = binding.price.toString(),
                        manufacturer = binding.manufacturer.toString(),
                        quantity = binding.quantity.toString())
                    product.let { it1 -> repo.insertProduct(it1) }
                }
            Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show()
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Glide.with(this).load(selectedImageUri).into(binding.imageView3)
        }
    }
}
