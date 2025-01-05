package com.ilgusu.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.LinearLayout
import com.ilgusu.presentation.R

class StepProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentStep: Int = 1
    private val totalSteps: Int = 3

    private val filledCircleRes = R.drawable.ic_step_filled
    private val emptyCircleRes = R.drawable.ic_step
    private val connectorRes = R.drawable.ic_dash_line

    init {
        orientation = HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        gravity = CENTER
        setupSteps()
    }

    private fun setupSteps() {
        removeAllViews()

        for (i in 1..totalSteps) {
            val circleImageView = ImageView(context).apply {
                val params = LayoutParams(
                    dpToPx(16),
                    dpToPx(16)
                )
                layoutParams = params
                setImageResource(
                    when {
                        i < currentStep -> emptyCircleRes
                        i == currentStep -> filledCircleRes
                        else -> emptyCircleRes
                    }
                )
            }
            addView(circleImageView)

            if (i < totalSteps) {
                val connectorImageView = ImageView(context).apply {
                    val params = LayoutParams(
                        dpToPx(36),
                        dpToPx(16)
                    ).apply {
                        marginStart = dpToPx(8)
                        marginEnd = dpToPx(8)
                    }
                    layoutParams = params
                    setImageResource(connectorRes)
                }
                addView(connectorImageView)
            }
        }
    }

    fun setCurrentStep(step: Int) {
        currentStep = step.coerceIn(1, totalSteps)
        setupSteps()
    }

    fun getCurrentStep(): Int = currentStep

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}