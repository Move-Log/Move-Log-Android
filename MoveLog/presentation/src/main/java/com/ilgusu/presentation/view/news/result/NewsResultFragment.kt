package com.ilgusu.presentation.view.news.result

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentNewsResultBinding
import com.ilgusu.presentation.util.UiState
import com.ilgusu.presentation.util.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.OutputStream
import java.util.Date

@AndroidEntryPoint
class NewsResultFragment : BaseFragment<FragmentNewsResultBinding>() {

    private val viewModel: NewsResultViewModel by viewModels()
    private lateinit var headline: String
    private var keywordId: Int = -1

    override fun initView() {
        requireActivity().window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        arguments?.let {
            headline = it.getString("headline") ?: ""
            keywordId = it.getInt("keywordId")
        }
        startAnimation()
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    lifecycleScope.launch {
                        navigationManager.navigate(
                            NavigationCommand.ToRouteAndClear(NavigationRoutes.Home)
                        )
                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnCancel.setOnClickListener {
            saveLayoutAsImageAndGetFile(binding.clNewsResult)?.let {
                viewModel.uploadNews(it, headline, keywordId)
            }
        }

        binding.llSaveNews.setOnClickListener { saveLayoutAsImage(binding.clNewsResult) }

        binding.flColorMint.setOnClickListener {
            binding.tvHeadliner.setImageResource(R.drawable.headliner_type_1)
            createSpannableHeadline(R.color.headline_mint)
        }
        binding.flColorRed.setOnClickListener {
            binding.tvHeadliner.setImageResource(R.drawable.headliner_type_2)
            createSpannableHeadline(R.color.headline_red)
        }
        binding.flColorYellow.setOnClickListener {
            binding.tvHeadliner.setImageResource(R.drawable.headliner_type_3)
            createSpannableHeadline(R.color.headline_yellow)
        }
        binding.flColorGreen.setOnClickListener {
            binding.tvHeadliner.setImageResource(R.drawable.headliner_type_4)
            createSpannableHeadline(R.color.headline_green)
        }
        binding.flColorPink.setOnClickListener {
            binding.tvHeadliner.setImageResource(R.drawable.headliner_type_5)
            createSpannableHeadline(R.color.headline_pink)
        }
    }

    private fun createSpannableHeadline(colorId: Int) {
        var splitHeadline = headline.split(",")
        if (splitHeadline.size == 2) {
            splitHeadline = listOf(splitHeadline[0] + ",", splitHeadline[1])
        } else {
            splitHeadline = if (headline.length > 12) {
                listOf(headline.substring(0, 11), headline.substring(11))
            } else {
                listOf("", headline)
            }
        }

        splitHeadline = splitHeadline.map { it.trim() }
        val newHeadline = buildSpannedString {
            append(splitHeadline[0] + if (splitHeadline[1].isNotBlank()) "\n" else "")
            color(ContextCompat.getColor(requireContext(), colorId)) {
                append(splitHeadline[1])
            }
        }

        binding.tvHeadline.text = newHeadline
    }

    private fun startAnimation() {
        showStepWithBlink(binding.ivDash1, binding.groupStep1) {
            binding.ivDash1.visibility = View.VISIBLE
            showStepWithBlink(binding.ivDash2, binding.groupStep2) {
                binding.ivDash2.visibility = View.VISIBLE
                showStepWithBlink(binding.ivStep3, binding.groupStep3) {
                    binding.groupResult.visibility = View.VISIBLE
                    binding.ivLogo.visibility = View.INVISIBLE
                    binding.root.setBackgroundColor(Color.WHITE)
                    binding.groupColors.visibility = View.VISIBLE
                    binding.groupStep1.visibility = View.INVISIBLE
                    binding.groupStep2.visibility = View.INVISIBLE
                    binding.groupStep3.visibility = View.INVISIBLE
                    binding.ivDash1.visibility = View.INVISIBLE
                    binding.ivDash2.visibility = View.INVISIBLE

                    requireActivity().window?.apply {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }

                    arguments?.let {
                        Glide.with(requireContext())
                            .load(it.getString("path"))
                            .transform(RoundedCorners(requireContext().dpToPx(8f).toInt()))
                            .into(binding.ivNewsResult)
                    }

                    binding.tvHeadliner.setImageResource(R.drawable.headliner_type_1)
                    createSpannableHeadline(R.color.headline_mint)
                }
            }
        }
    }

    private fun showStepWithBlink(dash: ImageView, stepGroup: Group, onStepComplete: () -> Unit) {
        val blink = ObjectAnimator.ofFloat(dash, "alpha", 0f, 1f)
        blink.duration = 500
        blink.repeatCount = 4
        blink.repeatMode = ObjectAnimator.REVERSE

        blink.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                dash.visibility = View.VISIBLE
                stepGroup.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                onStepComplete()
            }
        })

        blink.start()
    }

    private fun saveLayoutAsImage(constraintLayout: ConstraintLayout) {
        val bitmap = Bitmap.createBitmap(
            constraintLayout.width,
            constraintLayout.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        constraintLayout.draw(canvas)

        saveImageToDownloads(bitmap)
    }

    private fun saveImageToDownloads(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "my_news_${Date().time}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
        }

        val contentResolver = requireActivity().contentResolver
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { imageUri ->
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    showToast("이미지가 저장되었습니다")
                }

                var file: File
                runBlocking(Dispatchers.IO) {
                    val future = Glide.with(requireContext())
                        .asFile()
                        .load(uri)
                        .submit()
                    file = future.get()
                }

                viewModel.uploadNews(file, headline, keywordId)
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("이미지 저장에 실패했습니다.")
            }
        }
    }

    private fun saveLayoutAsImageAndGetFile(constraintLayout: ConstraintLayout): File? {
        val bitmap = Bitmap.createBitmap(
            constraintLayout.width,
            constraintLayout.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        constraintLayout.draw(canvas)

        return saveImageToFile(bitmap)
    }

    private fun saveImageToFile(bitmap: Bitmap): File? {
        val file = File(requireContext().cacheDir, "my_news_${Date().time}.png")
        try {
            val outputStream = file.outputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}