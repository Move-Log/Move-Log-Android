package com.ilgusu.presentation.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ilgusu.navigation.NavigationManager
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private var currentToast: Toast? = null

    @Inject
    lateinit var navigationManager: NavigationManager

    private val keyboardListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private val rect = Rect()

        override fun onGlobalLayout() {
            val rootView = view?.rootView ?: return
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                onKeyboardVisibilityChanged(true)
            } else {
                onKeyboardVisibilityChanged(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        setObserver()
    }

    // ViewBinding 인스턴스를 자동으로 생성하는 함수
    @Suppress("UNCHECKED_CAST")
    private fun inflateBinding(): VB {
        val clazz = (javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.get(0) as? Class<VB>
            ?: throw IllegalStateException("ViewBinding class not found")

        val method: Method = clazz.getMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun initView()

    protected open fun initListener() {}

    protected open fun setObserver() {}

    protected fun showToast(msg: String) {
        currentToast?.cancel()
//        currentToast = CustomToast.makeToast(requireContext(), msg)
        currentToast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    protected open fun onKeyboardVisibilityChanged(isVisible: Boolean) {}

    protected fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    protected fun showKeyboardAndFocus(view: View) {
        view.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupKeyboardListener() {
        view?.rootView?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardListener)
    }
}