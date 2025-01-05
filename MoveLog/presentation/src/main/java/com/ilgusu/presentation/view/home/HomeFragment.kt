package com.ilgusu.presentation.view.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilgusu.domain.model.MyRecentNewsEntity
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {

    // binding
    private lateinit var myRecentNewsAdapter : RvMyRecentNewsAdapter

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()
        val data = mutableListOf<MyRecentNewsEntity>()
        data.add(MyRecentNewsEntity("https://cdn.tgdaily.co.kr/news/photo/201911/216553_46204_931.jpg"))
        data.add(MyRecentNewsEntity("https://cdn.tgdaily.co.kr/news/photo/201911/216553_46204_931.jpg"))
        data.add(MyRecentNewsEntity("https://cdn.tgdaily.co.kr/news/photo/201911/216553_46204_931.jpg"))

        // adapter 연결
        myRecentNewsAdapter = RvMyRecentNewsAdapter()
        myRecentNewsAdapter.list = data
        binding.myMoveLogRv.adapter = myRecentNewsAdapter
        binding.myMoveLogRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.btn1.setOnClickListener {
            binding.imgComplete1.visibility = View.VISIBLE
        }
        binding.btn2.setOnClickListener {
            binding.imgComplete2.visibility = View.VISIBLE
        }
        binding.btn3.setOnClickListener {
            binding.imgComplete3.visibility = View.VISIBLE
        }
        binding.btn4.setOnClickListener {
            binding.imgComplete4.visibility = View.VISIBLE
        }
        binding.btn5.setOnClickListener {
            binding.imgComplete5.visibility = View.VISIBLE
        }

//        binding.btnNavigate.setOnClickListener {
//            lifecycleScope.launch {
//                navigationManager.navigate(
//                    NavigationCommand.ToRoute(NavigationRoutes.SignIn)
//                )
//            }
//        }
    }
}