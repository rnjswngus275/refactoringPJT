package com.ssafy.finalpjt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.adapter.FragmentShopAdapter
import com.ssafy.finalpjt.databinding.FragmentShopBinding
import java.util.ArrayList

class FragmentShop() : Fragment() {
    private lateinit var binding: FragmentShopBinding

    //    var shopAdapter: ShopAdapter? = null
    private lateinit var shopAdapter: FragmentShopAdapter
    var itemList = ArrayList<StoreDTO>()
    var dbHelper: DBHelper? = DBHelper(context, "QuestApp.db", null, 1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dbHelper!!.isEmptyShopItem == 0) {
            createShopList()
        }

        initAdapter()

        initView()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 0) {
            val position = shopAdapter.getPostion()
            dbHelper!!.deleteShopItem(itemList[position].name)
            itemList.removeAt(position)
            refreshData()
            Toast.makeText(
                requireContext(),
                "삭제되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
        return super.onContextItemSelected(item)
    }

    private fun createShopList() {
        itemList.clear()
        val temp = dbHelper!!.selectShopItem().split("\n").toTypedArray()
        val data = Array(2) { arrayOfNulls<String>(temp.size) }
        for (i in temp.indices) {
            for (k in 0..1) {
                data[k][i] = temp[i].split("\\|").toTypedArray()[k]
            }
            itemList.add(
                StoreDTO(
                    data[0][i],
                    data[1][i]!!.toInt(),
                    resources.getIdentifier("index$i", "drawable", context!!.packageName)
                )
            )
        }
    }

    private fun initAdapter() {
        shopAdapter = FragmentShopAdapter().apply {
            this.itemList = this@FragmentShop.itemList
            this.itemClickListener = object : FragmentShopAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    val builder = AlertDialog.Builder(requireContext()).apply {
                        setMessage("구매하시겠습니까?")
                        setPositiveButton("예") { dialog, which ->
                            if (dbHelper!!.selectUserpoint() < this@FragmentShop.itemList[position].cost) {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    (this@FragmentShop.itemList[position].cost - dbHelper!!.selectUserpoint()).toString() + "포인트가 부족하여 구매할 수 없습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                dbHelper!!.minusUserPoint(this@FragmentShop.itemList[position].cost)
                                Toast.makeText(
                                    requireContext(),
                                    "${this@FragmentShop.itemList[position].cost} 포인트를 지불하여 구매하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        setNegativeButton("아니오") { dialog, which ->
                            Toast.makeText(
                                requireContext(),
                                "아니오를 선택했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    builder.show()
                }
            }
        }
    }

    private fun initView() {
        binding.fragmentShopRecyclerview.apply {
            adapter = shopAdapter
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        }

        binding.button.setOnClickListener(View.OnClickListener {
            val name = binding.editText.text.toString().trim { it <= ' ' }
            val cost = binding.editText2.text.toString().trim { it <= ' ' }.toInt()
            itemList.add(
                StoreDTO(
                    name,
                    cost,
                    resources.getIdentifier(
                        "index" + (itemList.size % 11),
                        "drawable",
                        requireContext().packageName
                    )
                )
            )
            refreshData()
            dbHelper!!.setShopItem(name, cost)
        })
    }

    private fun refreshData() {
        shopAdapter.itemList = this@FragmentShop.itemList
        (binding.fragmentShopRecyclerview.adapter as FragmentShopAdapter).notifyDataSetChanged()
    }

}

//inner class ShopAdapter() : BaseAdapter() {
//        override fun getCount(): Int {
//            return items.size
//        }
//
//        fun addItem(singerItem: StoreDTO) {
//            items.add(singerItem)
//        }
//
//        override fun getItem(i: Int): StoreDTO {
//            return items[i]
//        }
//
//        override fun getItemId(i: Int): Long {
//            return i.toLong()
//        }
//
//        override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
//            val storeItem = StoreItem(requireContext())
//            storeItem.setItem(items[i])
//            return storeItem
//        }
//    }

//binding.gridView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
//    override fun onItemLongClick(
//        adapterView: AdapterView<*>?,
//        view: View,
//        i: Int,
//        l: Long
//    ): Boolean {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(
//            (activity)!!
//        )
//        builder.setMessage("삭제하시겠습니까?")
//        builder.setPositiveButton("예",
//            object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                    dbHelper!!.deleteShopItem(items.get(i).name)
//                    Toast.makeText(
//                        activity!!.applicationContext,
//                        items.get(i).name + "아이템을 삭제하였습니다.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    items.removeAt(i)
//                    shopAdapter!!.notifyDataSetChanged()
//                }
//            })
//        builder.setNegativeButton("아니오",
//            object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface, which: Int) {
//                    Toast.makeText(
//                        activity!!.applicationContext,
//                        "아니오를 선택했습니다.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            })
//        builder.show()
//        return true
//    }
//}


//        binding.gridView.adapter = shopAdapter
//        binding.gridView.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
//                val builder: AlertDialog.Builder = AlertDialog.Builder(
//                    (activity)!!
//                )
//                builder.setMessage("구매하시겠습니까?")
//                builder.setPositiveButton("예",
//                    object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface, which: Int) {
//                            if (dbHelper!!.selectUserpoint() < items.get(i).cost) {
//                                Toast.makeText(
//                                    activity!!.applicationContext,
//                                    (items.get(i).cost - dbHelper!!.selectUserpoint()).toString() + "포인트가 부족하여 구매할 수 없습니다.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            } else {
//                                dbHelper!!.minusUserPoint(items.get(i).cost)
//                                Toast.makeText(
//                                    activity!!.applicationContext,
//                                    items.get(i).cost.toString() + "포인트를 지불하여 구매하였습니다.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    })
//                builder.setNegativeButton("아니오",
//                    object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface, which: Int) {
//                            Toast.makeText(
//                                activity!!.applicationContext,
//                                "아니오를 선택했습니다.",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    })
//                builder.show()
//            }
//        }