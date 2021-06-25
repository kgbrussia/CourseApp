package com.kgbrussia.java.calendar

import java.util.*

interface CalendarRepository {
    fun getCalendar(): Calendar
    fun setCalendarDate(day: Int, month: Int, year: Int)
    fun getNow(): Calendar
}