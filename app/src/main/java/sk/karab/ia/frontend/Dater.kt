package sk.karab.ia.frontend

import android.util.Log
import android.widget.EditText
import sk.karab.ia.configurable.Timetable
import java.util.Calendar

class Dater {
    companion object {


        fun setDateToday(yearInput: EditText, monthInput: EditText, dayInput: EditText) {
            println("Setting the date inputs to today")

            val current = Calendar.getInstance()

            val year = current.get(Calendar.YEAR).toString()
            val month = (current.get(Calendar.MONTH) + 1).toString()
            val day = current.get(Calendar.DAY_OF_MONTH).toString()

            yearInput.setText(year)
            monthInput.setText(month)
            dayInput.setText(day)

            println("Successfully set the date inputs to today")
        }


        fun setDateSoonest(yearInput: EditText, monthInput: EditText, dayInput: EditText, subject: String) {
            println("Setting the date inputs to the soonest date for a subject $subject")

            val soonest = Timetable.getSoonestSubject(subject)

            val year = soonest.get(Calendar.YEAR).toString()
            val month = (soonest.get(Calendar.MONTH) + 1).toString()
            val day = soonest.get(Calendar.DAY_OF_MONTH).toString()

            yearInput.setText(year)
            monthInput.setText(month)
            dayInput.setText(day)

            println("Successfully set the date inputs to the soonest date found for the subject $subject")
        }


    }
}