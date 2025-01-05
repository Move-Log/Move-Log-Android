package com.ilgusu.presentation.view.home

import android.annotation.SuppressLint
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.ilgusu.domain.model.MyRecentNewsEntity
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import kotlin.math.abs
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {

    private lateinit var myRecentNewsAdapter: RvMyRecentNewsAdapter

    override fun initView() {

    }

    @SuppressLint("SimpleDateFormat")
    override fun initListener() {
        super.initListener()

        val currentTime: Long = System.currentTimeMillis() // ms로 반환

        val dataFormat1 = SimpleDateFormat("yyyy년 MM월 dd일 (E) hh:mm:ss") // 년 월 일
        binding.tvTime.text = dataFormat1.format(currentTime)

        val data = mutableListOf<MyRecentNewsEntity>()
        data.add(MyRecentNewsEntity("https://picsum.photos/1600/900"))
        data.add(MyRecentNewsEntity("https://picsum.photos/1600/900"))
        data.add(MyRecentNewsEntity("https://picsum.photos/1600/900"))

        myRecentNewsAdapter = RvMyRecentNewsAdapter()
        myRecentNewsAdapter.list = data
        binding.vpMyMoveLog.adapter = myRecentNewsAdapter
        binding.vpMyMoveLog.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.vpMyMoveLog.offscreenPageLimit = 4
        // item_view 간의 양 옆 여백을 상쇄할 값
        val offsetBetweenPages =
            resources.getDimensionPixelOffset(R.dimen.offsetBetweenPages).toFloat()
        binding.vpMyMoveLog.setPageTransformer { page, position ->
            val myOffset = position * -(2 * offsetBetweenPages)
            if (position < -1) {
                page.translationX = -myOffset
            } else if (position <= 1) {
                // Paging 시 Y축 Animation 배경색을 약간 연하게 처리
                val scaleFactor = 0.85f.coerceAtLeast(1 - abs(position))
                page.translationX = myOffset
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
            } else {
                page.alpha = 0f
                page.translationX = myOffset
            }
        }

        val pageCount = data.size
        binding.circleIndicator.createDotPanel(
            pageCount,
            R.drawable.indicator_dot_off,
            R.drawable.indicator_dot_on,
            0
        )

        binding.vpMyMoveLog.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.circleIndicator.selectDot(position) // Update the indicator
            }
        })

        binding.circleIndicator.createDotPanel(
            3,
            R.drawable.indicator_dot_off,
            R.drawable.indicator_dot_on,
            0
        )

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

        val wiseSayings = listOf(
            "역사는 단지 과거의 기록이 아니라, 현재와 미래를 이해하는 열쇠이다." to "-윌리엄 런던-",
            "기록은 사실을 보존하고, 사실은 진실을 밝히며, 진실은 정의를 이끈다." to "-조지 오웰-",
            "기록하지 않으면, 역사는 사라지고, 사람들의 기억 속에서도 잊혀질 것이다." to "-알렉스 헤일리-",
            "기록은 우리를 진실로 이끈다.\n그 진실은 우리를 자유롭게 만든다." to "-존 F. 케네디-",
            "기록된 것은 절대 사라지지 않는다.\n그것은 시간 속에서 계속해서 말한다." to "-아이작 아시모프-",
            "당신의 삶에서 중요한 순간을 기록하라.\n그것이 미래 세대에 가장 큰 선물이 될 것이다." to "-로빈 샤르마-",
            "기록은 단순한 기억이 아니다.\n그것은 우리의 경험을 다음 세대에 전달하는 방법이다." to "-칼 세이건-"
        )

        val randomSaying = wiseSayings[Random.nextInt(wiseSayings.size)]
        val (saying, author) = randomSaying

        binding.tvWiseSaying.text = saying
        binding.tvWiseSayingWho.text = author

//        binding.btnSetting.setOnClickListener {
//            lifecycleScope.launch {
//                navigationManager.navigate(
//                    NavigationCommand.ToRoute(NavigationRoutes.Setting)
//                )
//            }
//        }
    }
}