package com.newland.mvvmproject.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @author: leellun
 * @data: 16/6/2021.
 *
 */
object ScreenUtils {
    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return Math.max(
            outMetrics.heightPixels,
            outMetrics.widthPixels
        )
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return Math.max(outMetrics.heightPixels, outMetrics.widthPixels)
    }

    fun isLandScreen(context: Context): Boolean {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return outMetrics.heightPixels < outMetrics.widthPixels
    }
}