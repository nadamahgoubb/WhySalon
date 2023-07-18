package com.example.whysalon.util

import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.example.whysalon.R
import com.example.whysalon.util.ext.isNull
import com.example.whysalon.util.ext.supportFragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object DateUtil {
    const val DEFAULT_PATTERN = "yyyy-MM-dd"
    val YESTERDAY: Long = LocalDate.now().atStartOfDay(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
        .minus(1, ChronoUnit.DAYS).toInstant().toEpochMilli()
}

@Parcelize
class RangeValidator(
    private var minDate: Long = 0,
    private var maxDate: Long = 0
) : CalendarConstraints.DateValidator {
    //show hide dates in calendar
    override fun isValid(date: Long): Boolean {
        return !(minDate > date || maxDate < date)
    }
}

fun Fragment.showRangeDatePicker(
    dateConstraintFromTo: Pair<Long, Long>,
    pattern: String = "dd/MM/yyyy",
    selectedDateFromTo: Pair<Long, Long>? = null,
    dateCallback: (Long?, Long?) -> Unit
) {
    if (dateConstraintFromTo.first.isNull() || dateConstraintFromTo.second.isNull()) {
       // "date cannot be null or blank".showLogMessage()
        return
    }
    val minDate = dateConstraintFromTo.first
    val maxDate = dateConstraintFromTo.second
    if (minDate > maxDate) {
       // showLogMessage("dateFrom: ${dateConstraintFromTo.first} cannot be after dateTo: ${dateConstraintFromTo.second}")
        return
    }
    val validator: CalendarConstraints.DateValidator = RangeValidator(minDate, maxDate)
    val constraintsBuilder =
        CalendarConstraints.Builder()
            .setStart(minDate)
            .setEnd(maxDate)
            .setValidator(validator)
    val picker =
        MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
    if (selectedDateFromTo != null) {
        picker.setSelection(selectedDateFromTo)
    } else {
        //set default selection to current day
        picker.setSelection(
            Pair(
                MaterialDatePicker.todayInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
            )
        )
    }
    val build = picker.build()
    build.show(supportFragmentManager, "range_date_picker_tag")
    build.addOnPositiveButtonClickListener {
        dateCallback.invoke(it.first, it.second)
    }
}
fun toLong(s:String): Long {
    return   (SimpleDateFormat("dd/MM/yy").parse(s)).time

}

@Throws(ParseException::class)
fun convertDateFormat(
    inputDate: String ,
    inputFormat: String="YYYY-MM-dd",
    outputFormat: String="dd,MMM,yyyy"
): String? {
    val inputDf: DateFormat = SimpleDateFormat(inputFormat)
    val outputDf: DateFormat = SimpleDateFormat(outputFormat)
    val tempDate: Date = inputDf.parse(inputDate)
    return outputDf.format(tempDate)
}
fun timetoLong(s:String): Long {
    return   (SimpleDateFormat("hh:mm:ss").parse(s)).time

}
fun timetoDate(s:String): Date? {
    return   (SimpleDateFormat("hh:mm").parse(s) )

}
fun Fragment.showDatePicker(
    constraintsBuilder: CalendarConstraints.Builder = CalendarConstraints.Builder(),
    selectedDate: Long = DateUtil.YESTERDAY,
    onResult: (Long) -> Unit
) {
    MaterialDatePicker.Builder.datePicker()
        .setTitleText(getString(R.string.select_date))
        .setCalendarConstraints(constraintsBuilder.build())
        .setSelection(selectedDate)
        .build().also {
            it.show(childFragmentManager, "date_picker_tag")
            it.addOnPositiveButtonClickListener { date ->
                onResult.invoke(date)
            }
        }
}

fun Long.toLocaleDate(): LocalDate = LocalDate.ofEpochDay(this)
fun Long.toInstantOfMilli(): Instant = Instant.ofEpochMilli(this)
fun Long.toInstantOfSec(): Instant = Instant.ofEpochSecond(this)

fun LocalDate.formatDate(pattern: String, locale: Locale = Locale.ENGLISH): String {
    return DateTimeFormatter.ofPattern(pattern, locale).format(this)
}

fun String.toDateTime(pattern: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(this, formatter)
}

fun String.toInstantOfMilli(pattern: String): Instant {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()
}

fun String.toLocalDate(pattern: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter)
}

fun LocalDateTime.formatDate(pattern: String, locale: Locale = Locale.ENGLISH): String {
    return DateTimeFormatter.ofPattern(pattern, locale).format(this)
}

fun Instant.toLocalDate(): LocalDate {
    return toLocalDateTime().toLocalDate()
}

fun Instant.formatDate(pattern: String, locale: Locale = Locale.ENGLISH): String =
    LocalDateTime.ofInstant(this, ZoneId.systemDefault()).formatDate(pattern, locale)

fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

fun ZonedDateTime.isEqualTo(oldDate: ZonedDateTime): Boolean {
    val diff = Duration.between(this, oldDate)
    return if (diff.isZero) {
        true
    } else {
      //  "${diff.toDays()}days".showLogMessage()
      //  "${diff.minusDays(diff.toDays()).toHours()}hours".showLogMessage()
      //  "${
            diff.minusDays(diff.toDays()).minusHours(diff.toHours()).toMinutes()
         //mints".showLogMessage()
        if (diff.toDays() != 0L) {
            return false
        }
        if (diff.toHours() != 0L) {
            return false
        }
        val minutes = diff.toMinutes()
        return !(minutes != 0L && (minutes * -1) >= 15L) //this 15 mints buffer (you can remove it if you want)
    }
}