package com.panghu.uikit.utils

import android.content.Context
import android.text.*
import android.text.Annotation
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import java.util.regex.Pattern


/**
 * Style text using Annotations.
 * It support basic styling by HTML tags and custom styles by Annotation
 * @see android.text.Annotation
 * Make sure you add the <annotation> tag to all your string, in every strings.xml file.
 *
 * @author hugh.wang
 * @date 11/22/2019
 */
object TextStylingHelper {
    private const val INDEX = "(\\d+\\\$)"
    private const val WIDTH = "(\\d+)?"
    private const val PRECISION = "(\\.\\d+)?"
    private const val GENERAL = "(-)?($WIDTH[bBhHsScC])"
    private const val CHARACTER = "(-)?($WIDTH[cC])"
    private const val DECIMAL = "(([-+ 0,(]*)?${WIDTH}d)"
    private const val INTEGRAL = "(([-#+ 0(]*)?$WIDTH[oxX])"
    private const val FLOATING_POINT = "(([-#+ 0(]*)?$WIDTH$PRECISION[eEfgG])"
    private const val FLOATING_POINT_HEX = "(([-#+ ]*)?$WIDTH$PRECISION[aA])"
    private const val DATE_TIME = "(([-]*)?$WIDTH[tT][HIklMSLNpzZsQBbhAaCYyjmdeRTrDFc])"
    private const val PERCENT = "(-?$WIDTH%)"
    private const val LINE_SEPARATOR = "(n)"

    @JvmStatic
    private val pattern = Pattern.compile(
        "%$INDEX($GENERAL|$CHARACTER|$DECIMAL|$INTEGRAL|$FLOATING_POINT" +
                "|$FLOATING_POINT_HEX|$DATE_TIME|$PERCENT|$LINE_SEPARATOR)"
    )

    /**
     * Android only permits resource string formatting to be performed on simple string resources,
     * so if you attempt to load a formatted String which contains annotations, then the annotations will
     * be lost as it is loaded. This function enables string resources containing formatting to be loaded
     * as Spanned objects keeping the annotation spans intact.It can then process these as normal.
     * @param formatBuilder a string resources as SpannableStringBuilder
     * @param formatArgs Arguments referenced by the format specifiers in the format string
     * @see java.util.Formatter
     */
    @JvmStatic
    private fun format(formatBuilder: SpannableStringBuilder, vararg formatArgs: Any?): CharSequence {
        with(pattern.matcher(formatBuilder)) {
            var count = 0
            while (find()) {
                val placeholder = formatBuilder.substring(start(), end())
                val replacement = String.format(placeholder, *formatArgs)
                formatBuilder.replace(start(), end(), replacement)
                //In extreme cases, when 'replacement' is the same as 'placeholder', there will be infinite loop
                reset(formatBuilder)
                //Avoid infinite loop
                if (count++ > formatArgs.size) {
                    break
                }
            }
        }
        return formatBuilder
    }

    /**
     * Get the string from resources, iterate through the annotations and get
     * the ones with the key font and the corresponding value.Then create the
     * custom span and set it to text in the same positions as the annotation span.
     * @param resId The string in xml with <annotation> tag
     * @param formatArgs If you don't need formatting, ignore it
     * @see format
     */
    @JvmStatic
    fun parseStylingText(context: Context, resId: Int, vararg formatArgs: Any?): CharSequence {
        val resText = context.getText(resId)
        val spannableString = SpannableStringBuilder(resText)

        if (resText is SpannedString) {
            val annotations = resText.getSpans(0, resText.length, Annotation::class.java)
            for (annotation in annotations) {
                if (annotation.key == "fontColor") {
                    val id = context.resources.getIdentifier(
                        annotation.value, "color", context.packageName
                    )
                    if (id != 0) {
                        spannableString.setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(context, id)),
                            resText.getSpanStart(annotation),
                            resText.getSpanEnd(annotation),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
        if (formatArgs.isNotEmpty()) {
            return format(spannableString, *formatArgs)
        }
        return spannableString
    }
}