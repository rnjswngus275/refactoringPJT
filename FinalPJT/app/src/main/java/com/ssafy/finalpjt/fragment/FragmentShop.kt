package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.FragmentShopViewModel
import com.ssafy.finalpjt.adapter.FragmentShopAdapter
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.Shop
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.UserRepository
import com.ssafy.finalpjt.databinding.FragmentShopBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FragmentShop_싸피"
class FragmentShop() : Fragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var shopAdapter: FragmentShopAdapter
    private val fragmentShopViewModel: FragmentShopViewModel by viewModels()
    lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentShopViewModel.user.observe(viewLifecycleOwner) {
            user = it
        }

        initAdapter()

        initView()

    }

    private fun initAdapter() {
        shopAdapter = FragmentShopAdapter().apply {
            this.itemClickListener = object : FragmentShopAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    val builder = AlertDialog.Builder(requireContext()).apply {
                        setMessage("구매하시겠습니까?")
                        setPositiveButton("예") { dialog, which ->
                            if (user.Point < shopAdapter.itemList[position].Price) {
                                Toast.makeText(
                                    requireContext(),
                                    "${(shopAdapter.itemList[position].Price - user.Point)} 포인트가 부족하여 구매할 수 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                fragmentShopViewModel.updateUser(user.Point - shopAdapter.itemList[position].Price)
                                Toast.makeText(
                                    requireContext(),
                                    "${shopAdapter.itemList[position].Price} 포인트를 지불하여 구매하였습니다.",
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
            this.longClickListener = object : FragmentShopAdapter.LongClickListener {
                override fun onLongClick(view: View, position: Int) {
                    val builder = AlertDialog.Builder(requireContext()).apply {
                        setMessage("삭제하시겠습니까?")
                        setPositiveButton("예") { dialog, which ->
                            fragmentShopViewModel.deleteShop(shopAdapter.itemList[position])
                            Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
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
        fragmentShopViewModel.shopList.observe(viewLifecycleOwner) {
            shopAdapter.itemList = it
            shopAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        binding.fragmentShopRecyclerview.apply {
            adapter = shopAdapter
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        }

        binding.addBtn.setOnClickListener {
            if (
                !binding.nameEt.text.toString().isNullOrBlank()
                && !binding.pointEt.text.toString().isNullOrBlank()
            ) {
                val name = binding.nameEt.text.toString().trim { it <= ' ' }
                val price = binding.pointEt.text.toString().trim { it <= ' ' }.toInt()
                fragmentShopViewModel.insertShop(Shop(name, price))
            }
        }
    }

}
