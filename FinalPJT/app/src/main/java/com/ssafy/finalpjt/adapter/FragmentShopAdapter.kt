package com.ssafy.finalpjt.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.StoreDTO

class FragmentShopAdapter() : RecyclerView.Adapter<FragmentShopAdapter.FragmentShopViewHolder>() {
    var itemList = mutableListOf<StoreDTO>()
    lateinit var itemClickListener: ItemClickListener
    private var rPosition = RecyclerView.NO_POSITION

    inner class FragmentShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener
    {
        var itemImage: ImageView = itemView.findViewById(R.id.store_item_iv)
        var itemName: TextView = itemView.findViewById(R.id.store_item_tv)
        var itemPrice: TextView = itemView.findViewById(R.id.store_item_price_tv)

        fun bindInfo(item: StoreDTO) {
//            val resId = itemView.context.resources.getIdentifier("index" + (layoutPosition % 11), "drawable", itemView.context.packageName)
            itemImage.setImageResource(item.image)
            itemName.text = item.name
            itemPrice.text = item.cost.toString()
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            rPosition = this.adapterPosition
            menu?.add(0, 0, 0, "삭제")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.store_item, parent, false)
        return FragmentShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragmentShopViewHolder, position: Int) {
        holder.bindInfo(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun getPostion(): Int = rPosition
}