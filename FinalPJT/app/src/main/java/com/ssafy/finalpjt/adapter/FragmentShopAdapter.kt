package com.ssafy.finalpjt.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.Shop

class FragmentShopAdapter : RecyclerView.Adapter<FragmentShopAdapter.FragmentShopViewHolder>() {
    var itemList = mutableListOf<Shop>()
    lateinit var itemClickListener: ItemClickListener
    lateinit var menuItemClickListener: MenuItemClickListener

    inner class FragmentShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener
    {
        var itemImage: ImageView = itemView.findViewById(R.id.store_item_iv)
        var itemName: TextView = itemView.findViewById(R.id.store_item_tv)
        var itemPrice: TextView = itemView.findViewById(R.id.store_item_price_tv)

        fun bindInfo(item: Shop) {
            val resId = itemView.context.resources.getIdentifier("index" + (layoutPosition % 11), "drawable", itemView.context.packageName)
            itemImage.setImageResource(resId)
            itemName.text = item.Item
            itemPrice.text = item.Price.toString()
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val menuItem = menu?.add(0, 0, 0, "삭제")
            menuItem?.setOnMenuItemClickListener {
                menuItemClickListener.onClick(itemList[layoutPosition])
                true
            }
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

    interface MenuItemClickListener {
        fun onClick(item: Shop)
    }
}