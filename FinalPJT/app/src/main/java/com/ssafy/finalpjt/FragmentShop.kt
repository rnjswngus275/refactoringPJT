package com.ssafy.finalpjt

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.databinding.FragmentShopBinding
import java.util.ArrayList

class FragmentShop() : Fragment() {
    private lateinit var binding: FragmentShopBinding

    var singerAdapter: SingerAdapter? = null
    var items = ArrayList<StoreDTO>()
    var dbHelper: DBHelper? = DBHelper(context, "QuestApp.db", null, 1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentShopBinding.inflate(inflater,container,false)

        if (dbHelper!!.isEmptyShopItem == 0) {
            createShopList()
        }

        singerAdapter = SingerAdapter()
        binding.gridView.adapter = singerAdapter
        binding.gridView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(
                    (activity)!!
                )
                builder.setMessage("구매하시겠습니까?")
                builder.setPositiveButton("예",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            if (dbHelper!!.selectUserpoint() < items.get(i).cost) {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    (items.get(i).cost - dbHelper!!.selectUserpoint()).toString() + "포인트가 부족하여 구매할 수 없습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                dbHelper!!.minusUserPoint(items.get(i).cost)
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    items.get(i).cost.toString() + "포인트를 지불하여 구매하였습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
                builder.setNegativeButton("아니오",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "아니오를 선택했습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                builder.show()
            }
        }
        binding.gridView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ): Boolean {
                val builder: AlertDialog.Builder = AlertDialog.Builder(
                    (activity)!!
                )
                builder.setMessage("삭제하시겠습니까?")
                builder.setPositiveButton("예",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dbHelper!!.deleteShopItem(items.get(i).name)
                            Toast.makeText(
                                activity!!.applicationContext,
                                items.get(i).name + "아이템을 삭제하였습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            items.removeAt(i)
                            singerAdapter!!.notifyDataSetChanged()
                        }
                    })
                builder.setNegativeButton("아니오",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "아니오를 선택했습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                builder.show()
                return true
            }
        }
        binding.button.setOnClickListener(View.OnClickListener {
            val name = binding.editText.text.toString().trim { it <= ' ' }
            val cost = binding.editText2.text.toString().trim { it <= ' ' }.toInt()
            items.add(
                StoreDTO(
                    name,
                    cost,
                    resources.getIdentifier("index" + items.size, "drawable", requireContext().packageName)
                )
            )
            singerAdapter!!.notifyDataSetChanged()
            dbHelper!!.setShopItem(name, cost)
            //singerAdapter.addItem(new SingerShopItem(name, cost, R.drawable.gift));
        })
        return binding.root
    }

    fun createShopList() {
        items.clear()
        val temp = dbHelper!!.selectShopItem().split("\n").toTypedArray()
        val data = Array(2) { arrayOfNulls<String>(temp.size) }
        for (i in temp.indices) {
            for (k in 0..1) {
                data[k][i] = temp[i].split("\\|").toTypedArray()[k]
            }
            Log.e("sangeun", (data[0][i])!!)
            Log.e("sangeun", (data[1][i])!!)
            items.add(
                StoreDTO(
                    data[0][i],
                    data[1][i]!!.toInt(),
                    resources.getIdentifier("index$i", "drawable", context!!.packageName)
                )
            )
        }
    }

    inner class SingerAdapter() : BaseAdapter() {
        override fun getCount(): Int {
            return items.size
        }

        fun addItem(singerItem: StoreDTO) {
            items.add(singerItem)
        }

        override fun getItem(i: Int): StoreDTO {
            return items[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
            val storeItem = StoreItem(activity!!.applicationContext)
            storeItem.setItem(items[i])
            return storeItem
        }
    }
}