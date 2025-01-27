package com.ilgusu.presentation.view.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentHomeBinding
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private var timeJob: Job? = null
    private lateinit var myRecentNewsAdapter: RvMyRecentNewsAdapter

    override fun initView() {
        requireActivity().window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setTime()
        setBottomNav()
    }

    private fun setTime() {
        timeJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val currentTime =
                    SimpleDateFormat(
                        "yyyy년 MM월 dd일 (E) hh:mm:ss",
                        Locale.getDefault()
                    ).format(Date())
                binding.tvTime.text = currentTime

                delay(1000)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun initListener() {
        super.initListener()

        binding.vpMyMoveLog.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.circleIndicator.selectDot(position)
            }
        })

        binding.circleIndicator.createDotPanel(
            3,
            R.drawable.indicator_dot_off,
            R.drawable.indicator_dot_on,
            0
        )

        binding.tvChooseDate.setOnClickListener {
            timeJob?.cancel()
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Calendar)
                )
            }
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

        binding.btnRecord.setOnClickListener {
            timeJob?.cancel()
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Record)
                )
            }
        }

        binding.btnSetting.setOnClickListener {
            timeJob?.cancel()
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Setting)
                )
            }
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.recordState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> showToast(it.message)
                is UiState.Success -> {
                    val secondaryColor = ContextCompat.getColor(requireContext(), R.color.secondary)
                    it.data.forEach { type ->
                        when (type) {
                            0 -> binding.ivType0.imageTintList =
                                ColorStateList.valueOf(secondaryColor)

                            1 -> binding.ivType1.imageTintList =
                                ColorStateList.valueOf(secondaryColor)

                            2 -> binding.ivType2.imageTintList =
                                ColorStateList.valueOf(secondaryColor)
                        }
                    }
                }
            }
        }

        viewModel.newsRecordState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> showToast(it.message)
                is UiState.Success -> {
                    for (i in 1..it.data) {
                        when (i) {
                            1 -> binding.imgComplete1.visibility = View.VISIBLE
                            2 -> binding.imgComplete2.visibility = View.VISIBLE
                            3 -> binding.imgComplete3.visibility = View.VISIBLE
                            4 -> binding.imgComplete4.visibility = View.VISIBLE
                            5 -> binding.imgComplete5.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.currentImageState.observe(viewLifecycleOwner){
            when(it) {
                is UiState.Loading -> {}
                is UiState.Error -> showToast(it.message)
                is UiState.Success -> {
                    myRecentNewsAdapter = RvMyRecentNewsAdapter()
                    myRecentNewsAdapter.list = it.data.toMutableList()
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

                    binding.circleIndicator.createDotPanel(
                        myRecentNewsAdapter.itemCount,
                        R.drawable.indicator_dot_off,
                        R.drawable.indicator_dot_on,
                        0
                    )
                }
            }
        }
    }

    private fun setBottomNav(){
        binding.bottomNav.ivHome.setImageResource(R.drawable.ic_home_enabled)
        binding.bottomNav.tvHome.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_1c))

        binding.bottomNav.menuNews.setOnClickListener {
            timeJob?.cancel()
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.NewsRecent)
                )
            }
        }

        binding.bottomNav.menuChart.setOnClickListener {
            timeJob?.cancel()
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Setting)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        timeJob?.cancel()
    }
}