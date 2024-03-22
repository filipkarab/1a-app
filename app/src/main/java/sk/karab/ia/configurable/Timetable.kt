package sk.karab.ia.configurable

import sk.karab.ia.R
import sk.karab.ia.util.Activities
import sk.karab.ia.util.Day
import java.util.Calendar

class Timetable {
    companion object {


        fun getSubject(fullName: String): String {

            val fullNamesOfSubjects = getFullNameList()
            val index = fullNamesOfSubjects.indexOf(fullName)

            return getSubject(index)
        }


        fun getFullNameList(): Array<String> {
            return arrayOf(
                "Anglina",
                "Aplikovaná informatika",
                "Elektrotechnika",
                "Etika",
                "Fyzika",
                "Hardvér počítača",
                "Matematika",
                "Nemčina",
                "Odborná prax",
                "Robotika",
                "Slovina",
                "Telesná",
                "Triednická",
                "Úvod do programovania"
            )
        }


        fun getSubject(index: Int): String {
            when (index) {
                0 -> return "anglina"
                1 -> return "aplikovana-informatika"
                2 -> return "elektrotechnika"
                3 -> return "etika"
                4 -> return "fyzika"
                5 -> return "hardver-pocitaca"
                6 -> return "matika"
                7 -> return "nemcina"
                8 -> return "odborna-prax"
                9 -> return "robotika"
                10 -> return "slovina"
                11 -> return "telesna"
                12 -> return "triednicka"
                13 -> return "uvod-do-programovania"
                else -> {
                    println("Couldn't find the subject selected by index $index, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_error)
                    return "unknown"
                }
            }
        }


        fun getSoonestSubject(subject: String): Calendar {
            val soonestDate = Calendar.getInstance()

            val possibleDay = Calendar.getInstance()
            possibleDay.add(Calendar.DATE, 1)

            var reachedCap = 0
            while (true) {

                val weekday = Day.getFromNumber(
                    possibleDay.get(Calendar.DAY_OF_WEEK)
                )
                if (hasASubject(weekday, subject)) {

                    val possibleYear = possibleDay.get(Calendar.YEAR)
                    val possibleMonth = possibleDay.get(Calendar.MONTH)
                    val possibleDayOfMonth = possibleDay.get(Calendar.DAY_OF_MONTH)

                    soonestDate.set(possibleYear, possibleMonth, possibleDayOfMonth)
                    return possibleDay

                } else {
                    possibleDay.add(Calendar.DATE, 1)
                    reachedCap++
                    if (reachedCap > 7) {

                        println("Couldn't find the soonest date for the subject $subject, ended on $possibleDay, throwing an error activity")
                        Activities.displayActivity(R.layout.activity_error)
                        return possibleDay
                    }
                }
            }
        }


        fun hasASubject(day: Day, subject: String): Boolean {

            if (day != Day.SATURDAY && day != Day.SUNDAY) {
                for (i in getClasses(day)) {
                    if (i.contentEquals(subject)) {
                        return true
                    }
                }
            }

            return false
        }


        fun getTimetable(): HashMap<Day, ArrayList<String>> {
            println("Getting the timetable")

            val timetable = HashMap<Day, ArrayList<String>>()
            timetable[Day.MONDAY] = getMondayClasses()
            timetable[Day.TUESDAY] = getTuesdayClasses()
            timetable[Day.WEDNESDAY] = getWednesdayClasses()
            timetable[Day.THURSDAY] = getThursdayClasses()
            timetable[Day.FRIDAY] = getFridayClasses()

            println("Successfully got the timetable")
            return timetable
        }


        fun getClasses(day: Day): ArrayList<String> {
            println("Getting the classes for day $day")

            when (day) {
                Day.MONDAY -> return getMondayClasses()
                Day.TUESDAY -> return getTuesdayClasses()
                Day.WEDNESDAY -> return getWednesdayClasses()
                Day.THURSDAY -> return getThursdayClasses()
                Day.FRIDAY -> return getFridayClasses()
                else -> {
                    println("Requested to load the classes for a day off, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_error)
                }
            }

            println("Couldn't load the classes for the day $day, throwing an error activity")
            Activities.displayActivity(R.layout.activity_error)

            return ArrayList()
        }


        private fun getMondayClasses(): ArrayList<String> {
            val classes = ArrayList<String>()

            classes.add("slovina")
            classes.add("telesna")
            classes.add("anglina")
            classes.add("anglina")
            classes.add("nemcina")
            classes.add("uvod-do-programovania")
            classes.add("uvod-do-programovania")
            classes.add("triednicka")

            return classes
        }


        private fun getTuesdayClasses(): ArrayList<String> {
            val classes = ArrayList<String>()

            classes.add("nemcina")
            classes.add("matika")
            classes.add("fyzika")
            classes.add("slovina")
            classes.add("robotika")
            classes.add("robotika")
            classes.add("hardver-pocitaca")

            return classes
        }


        private fun getWednesdayClasses(): ArrayList<String> {
            val classes = ArrayList<String>()

            classes.add("fyzika")
            classes.add("elektrotechnika")
            classes.add("etika")
            classes.add("slovina")
            classes.add("anglina")
            classes.add("nemcina")
            classes.add("matika")

            return classes
        }


        private fun getThursdayClasses(): ArrayList<String> {
            val classes = ArrayList<String>()

            classes.add("matika")
            classes.add("elektrotechnika")
            classes.add("odborna-prax")
            classes.add("odborna-prax")
            classes.add("fyzika")
            classes.add("telesna")
            classes.add("slovina")

            return classes
        }


        private fun getFridayClasses(): ArrayList<String> {
            val classes = ArrayList<String>()

            classes.add("elektrotechnika")
            classes.add("elektrotechnika")
            classes.add("anglina")
            classes.add("aplikovana-informatika")
            classes.add("aplikovana-informatika")

            return classes
        }


    }
}