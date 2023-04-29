import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventory.Product
import com.example.inventory.databinding.CardBinding

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var products: List<Product> = emptyList()
    var onClickListener: ListClickListener<Product>? = null

    fun setProduct(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

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

    override fun getItemCount() = products.size

    fun setOnItemClick(listClickListener: ListClickListener<Product>) {
        this.onClickListener = listClickListener
    }

    interface ListClickListener<T> {
        fun onClick(data: T, position: Int)
        fun onDelete(product: T)
    }

    inner class ViewHolder(private val binding: CardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                Glide.with(image).load(product.image).into(image)
                title.text = product.name
                price.text = product.price.toString()
                manufacturer.text = product.manufacturer
                quantity.text = product.quantity.toString()
            }
        }
    }
}
