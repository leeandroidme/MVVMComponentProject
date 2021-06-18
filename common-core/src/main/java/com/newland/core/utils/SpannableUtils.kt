package com.newland.core.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
object SpannableUtils {
    fun getSizeSpannable(content: String?, start: Int, end: Int, size: Int): SpannableString? {
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            AbsoluteSizeSpan(size),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun getColorSpannable(content: String?, start: Int, end: Int, color: Int): SpannableString? {
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}