package com.example.namigtahmazli.shrineappnative

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        products.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position % 3 == 2) 2 else 1
                    }
                }
            }
            addItemDecoration(ProductsItemDecoration())
            adapter = ProductsAdapter().apply { submitList(ProductEntry.initProductEntryList(resources)) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                animateView()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var isStarted = false

    private fun animateView() {
        val (frontValues, btnValues) = if (isStarted) {
            isStarted = false
            Pair(
                    floatArrayOf(front_layer.height - 56 * density, 0f),
                    floatArrayOf(btn_cart.height.f, 0f)
            )
        } else {
            isStarted = true
            Pair(
                    floatArrayOf(0f, front_layer.height - 56 * density),
                    floatArrayOf(0f, btn_cart.height.f)
            )
        }

        AnimatorSet().apply {
            this += ObjectAnimator.ofFloat(front_layer, "translationY", *frontValues).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }

            this += ObjectAnimator.ofFloat(btn_cart, "translationY", *btnValues).apply {
                interpolator = AccelerateDecelerateInterpolator()
            }

            duration = 300

            start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_home, menu)
        return true
    }


    class ProductsAdapter : ListAdapter<ProductEntry, ProductsAdapter.ViewHolder>(DiffUtil) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
            val viewId = when (viewType) {
                1 -> R.layout.list_item_2
                2 -> R.layout.list_item_3
                else -> R.layout.list_item_1
            }
            return ViewHolder(
                    view = LayoutInflater.from(parent.context).inflate(viewId, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
            holder.bindTo(getItem(position))
        }

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            private val image: ImageView = view.findViewById(R.id.product_image)
            private val title: TextView = view.findViewById(R.id.title)
            private val price: TextView = view.findViewById(R.id.price)

            fun bindTo(productEntry: ProductEntry) {
                image.loadImage(productEntry.url)
                title.text = productEntry.title
                price.text = productEntry.price
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position % 3
        }

        object DiffUtil : android.support.v7.util.DiffUtil.ItemCallback<ProductEntry>() {
            override fun areItemsTheSame(oldItem: ProductEntry?, newItem: ProductEntry?): Boolean {
                return oldItem?.title == newItem?.title
            }

            override fun areContentsTheSame(oldItem: ProductEntry?, newItem: ProductEntry?): Boolean {
                return oldItem?.title == newItem?.title
            }
        }
    }
}