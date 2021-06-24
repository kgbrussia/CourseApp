package com.kgbrussia.java.calendar

import java.util.*

class CalendarModel: CalendarRepository {
    private var calendar = Calendar.getInstance()

    override fun getCalendar(): Calendar = calendar

    override fun setCalendarDate(day: Int, month: Int, year: Int) {
        with(calendar) {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
    }

    override fun getNow(): Calendar = Calendar.getInstance()
}