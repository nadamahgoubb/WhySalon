package com.dot_jo.whysalon.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.dot_jo.whysalon.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


fun convertPttern(serverDateFormat: Date?): String {
    var userPattern: String? = "yyyy-MM-dd"
    val userDateFormat = SimpleDateFormat(userPattern, Locale.ENGLISH)
      if (serverDateFormat != null) {
        val userDate = userDateFormat.format(serverDateFormat)
        return userDate
    }
    return ""
}

fun convertPttern(INPUT_DATE_STRING: String, userPattern: String = "dd MMMM yyyy"): String {

    var pattern: String? = "yyyy-MM-dd"
    val serverDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
// Format the date output, as you wish to see, after we read the Date() value
    val userDateFormat = SimpleDateFormat(userPattern, Locale.getDefault())

    val defaultDate = serverDateFormat.parse(INPUT_DATE_STRING)
    if (defaultDate != null) {
        val userDate = userDateFormat.format(defaultDate)
        return userDate

    }
    return ""
}

fun toLong(s: String): Long {
    return (SimpleDateFormat("yyyy-MM-dd").parse(s)).time

}

@RequiresApi(Build.VERSION_CODES.N)
@Throws(ParseException::class)
fun convertDateFormat(
    inputDate: String,
    inputFormat: String = "YYYY-MM-dd",
    outputFormat: String = "dd,MMM,yyyy"
): String? {
    val inputDf: DateFormat = SimpleDateFormat(inputFormat)
    val outputDf: DateFormat = SimpleDateFormat(outputFormat)
    val tempDate: Date = inputDf.parse(inputDate)
    return outputDf.format(tempDate)
}
fun LocalDate.formatDate(pattern: String, locale: Locale = Locale.ENGLISH): String {
    return DateTimeFormatter.ofPattern(pattern, locale).format(this)
}

fun timetoLong(s: String): Long {
    return (SimpleDateFormat("hh:mm:ss").parse(s)).time

}

@RequiresApi(Build.VERSION_CODES.O)
fun getYearFromDate(s: String): Int? {
    //  return   (SimpleDateFormat("yyyy-MM-dd").parse(s) )
    var ld = LocalDate.parse(
        s,
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    return ld.year
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthFromDate(s: String): Int? {
    //  return   (SimpleDateFormat("yyyy-MM-dd").parse(s) )
    var ld = LocalDate.parse(
        s,
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    return ld.monthValue-1
}
@RequiresApi(Build.VERSION_CODES.O)
fun getDayFromDate(s: String): Int? {
    //  return   (SimpleDateFormat("yyyy-MM-dd").parse(s) )
    var ld = LocalDate.parse(
        s,
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    return ld.dayOfMonth
}
@RequiresApi(Build.VERSION_CODES.O)
fun getMonthNameFromDate(s: String): String? {
    //  return   (SimpleDateFormat("yyyy-MM-dd").parse(s) )
    var ld = LocalDate.parse(
        s,
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()))

    return ld.month.name
}
fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}
fun ArabicToEnglish(str: String):String {
    var result = ""
    var en = '0'
    for (ch in str) {
        en = ch
        when (ch) {
           '٠'  -> en = '0'
            '١' -> en = '1'
            '٢' -> en = '2'
            '٣' -> en = '3'
            '٤' -> en = '4'
            '٥' -> en = '5'
            '٦' -> en = '6'
            '٧' -> en = '7'
            '٨' -> en = '8'
            '٩' -> en = '9'
        }
        result = "${result}$en"
    }
    return result
}
fun getDuration(min: Int, context: Context): String {
    if (min > 60) {
        var h = min / 60
        var mintue = min - (h * 60)
        return "" + h + " " + context.getString(R.string.h) + mintue + " " + context.getString(R.string.min)
    } else return "" + min + " " + context.getString(R.string.min)
}
