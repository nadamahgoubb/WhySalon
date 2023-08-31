package com.dot_jo.whysalon.util.ext

 import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun Any?.isNull(): Boolean = this == null

//get Generic Class Type
@Suppress("UNCHECKED_CAST")
fun <T : Any> Any.getTClass(): Class<T> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    return type as Class<T>
}


@RequiresApi(Build.VERSION_CODES.O)
fun String.toDate(pattern: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter)
}


fun Double?.roundTo(n: Int): Double {
    return "%.${n}f".format(Locale.ENGLISH, this).toDouble()
}
@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())


