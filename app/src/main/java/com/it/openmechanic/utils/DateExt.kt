package com.it.openmechanic.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


fun dateFromSeconds(seconds: Long): Date {
    return Date((seconds * 1000.toLong()))
}
fun Date.stringDate(): String {
  //  val locale = context?.getLangLocale() ?: Locale.US
    val sdf = SimpleDateFormat("dd MMMM")
    return sdf.format(time)
}

fun Date.stringTime(): String {
  //  val locale = context?.getLangLocale() ?: Locale.US
    val sdf = SimpleDateFormat("HH:mm")
    return sdf.format(time)
}

fun Date.stringDateTime(): String {
    //val locale = context?.getLangLocale() ?: Locale.US
    val sdf = SimpleDateFormat("dd MMMM HH:mm")
    return sdf.format(time)
}

fun Context.getLangLocale(): Locale {
    return Locale("ru")
}

fun getCurrentMillis(timeLong: Long) = timeLong * 1000

fun msTimeFormatter(millis: Long): String {
    val min = abs(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
    val sec = abs(TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
    var format = "%02d:%02d"
    if (millis < 0) format = "-$format"
    return String.format(format, min, sec)
}