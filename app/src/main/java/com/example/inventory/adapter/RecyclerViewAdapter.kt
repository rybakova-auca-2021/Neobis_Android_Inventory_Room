import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventory.model.Product
import com.example.inventory.databinding.CardBinding

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var products: ArrayList<Product> = arrayListOf()
    var onClickListener: ListClickListener<Product>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(product, position)
        }
    }

    override fun getItemCount() = products.count()

    fun setOnItemClick(listClickListener: ListClickListener<Product>) {
        this.onClickListener = listClickListener
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateProduct(newList: List<Product>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding: CardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                Glide.with(image).load(product.image).into(image)
                title.text = product.name
                price.text = product.price
                manufacturer.text = product.manufacturer
                quantity.text = product.quantity
            }
        }
    }
    interface ListClickListener<T> {
        fun onClick(data: T, position: Int)
    }
}
