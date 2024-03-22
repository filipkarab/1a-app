package sk.karab.ia.util

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import sk.karab.ia.Main
import sk.karab.ia.R
import sk.karab.ia.configurable.Timetable
import sk.karab.ia.database.Database
import sk.karab.ia.database.RequestType
import sk.karab.ia.frontend.Dater
import java.io.File
import java.util.Calendar

class Activities {
    companion object {


        private val main = Utility.notNull(Main.i) as Main


        fun displayActivity(layout: Int) {
            println("Attempting to display the layout ($layout) on the main thread")

            main.runOnUiThread {
                main.setContentView(layout)
                assignEvents(layout)
            }

            println("Successfully displayed the layout ($layout) on the main thread")
        }


        private fun assignEvents(layout: Int) {
            println("Assigning events for elements in layout ($layout)")

            when (layout) {

                R.layout.activity_main -> setupMainActivity()

                R.layout.activity_database_error -> setupActivityDatabaseError()

                R.layout.activity_error -> setupErrorActivity()

                R.layout.activity_homework -> setupHomeworkActivity()

                R.layout.activity_test -> setupTestActivity()

                R.layout.activity_lesson -> setupLessonActivity()

                R.layout.activity_note -> setupNoteActivity()

                R.layout.activity_links -> setupLinksActivity()

                R.layout.activity_attachment -> setupAttachmentActivity()

            }

            println("Successfully assigned events for elements in layout ($layout)")
        }


        private fun setupMainActivity() {
            println("Setting up the main activity")

            val addHomeworkButton = main.findViewById<Button>(R.id.add_homework)
            addHomeworkButton?.setOnClickListener {
                displayActivity(R.layout.activity_homework)
            }

            val addTestButton = main.findViewById<Button>(R.id.add_test)
            addTestButton?.setOnClickListener {
                displayActivity(R.layout.activity_test)
            }

            val addLessonButton = main.findViewById<Button>(R.id.add_lesson)
            addLessonButton?.setOnClickListener {
                displayActivity(R.layout.activity_lesson)
            }

            val addNoteButton = main.findViewById<Button>(R.id.add_note)
            addNoteButton?.setOnClickListener {
                displayActivity(R.layout.activity_note)
            }
            
            val addLinksButton = main.findViewById<Button>(R.id.add_links)
            addLinksButton.setOnClickListener { 
                displayActivity(R.layout.activity_links)
            }

            val addAttachmentButton = main.findViewById<Button>(R.id.add_attachment)
            addAttachmentButton.setOnClickListener {
                displayActivity(R.layout.activity_attachment)
            }

            println("Successfully set up the main activity")
        }


        private fun setupHomeworkActivity() {
            println("Setting up the homework activity")

            val fromYear = main.findViewById<EditText>(R.id.homework_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.homework_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.homework_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val toYear = main.findViewById<EditText>(R.id.homework_to_date_year)
            val toMonth = main.findViewById<EditText>(R.id.homework_to_date_month)
            val toDay = main.findViewById<EditText>(R.id.homework_to_date_day)

            if (toYear == null || toMonth == null || toDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            val subjectInput = main.findViewById<TextView>(R.id.homework_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    Dater.setDateSoonest(toYear, toMonth, toDay, Timetable.getSubject(Utility.lastWhich))
                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val submitButton = main.findViewById<Button>(R.id.homework_finish_button)
            submitButton?.setOnClickListener {

                val fromDate = Calendar.getInstance()
                fromDate.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val toDate = Calendar.getInstance()
                toDate.set(
                    Integer.parseInt(toYear.text.toString()),
                    Integer.parseInt(toMonth.text.toString()),
                    Integer.parseInt(toDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())

                val messageInput = main.findViewById<EditText>(R.id.homework_message_input)
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_HOMEWORK,
                        fromDate,
                        toDate,
                        subject,
                        message
                    )
                }

            }

            println("Successfully set up the homework activity")
        }


        private fun setupTestActivity() {
            println("Setting up the test activity")

            val fromYear = main.findViewById<EditText>(R.id.test_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.test_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.test_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val toYear = main.findViewById<EditText>(R.id.test_to_date_year)
            val toMonth = main.findViewById<EditText>(R.id.test_to_date_month)
            val toDay = main.findViewById<EditText>(R.id.test_to_date_day)

            if (toYear == null || toMonth == null || toDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            val subjectInput = main.findViewById<TextView>(R.id.test_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    Dater.setDateSoonest(toYear, toMonth, toDay, Timetable.getSubject(Utility.lastWhich))
                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val submitButton = main.findViewById<Button>(R.id.test_finish_button)
            submitButton?.setOnClickListener {

                val fromDate = Calendar.getInstance()
                fromDate.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val toDate = Calendar.getInstance()
                toDate.set(
                    Integer.parseInt(toYear.text.toString()),
                    Integer.parseInt(toMonth.text.toString()),
                    Integer.parseInt(toDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())

                val messageInput = main.findViewById<EditText>(R.id.test_message_input)
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_TEST,
                        fromDate,
                        toDate,
                        subject,
                        message
                    )
                }

            }

            println("Successfully set up the test activity")
        }


        private fun setupLessonActivity() {
            println("Setting up the lesson activity")

            val fromYear = main.findViewById<EditText>(R.id.lesson_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.lesson_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.lesson_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val subjectInput = main.findViewById<TextView>(R.id.lesson_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val submitButton = main.findViewById<Button>(R.id.lesson_finish_button)
            submitButton?.setOnClickListener {

                val date = Calendar.getInstance()
                date.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())

                val messageInput = main.findViewById<EditText>(R.id.lesson_message_input)
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_LESSON,
                        date,
                        null,
                        subject,
                        message
                    )
                }

            }

            println("Successfully set up the lesson activity")
        }


        private fun setupNoteActivity() {
            println("Setting up the note activity")

            val fromYear = main.findViewById<EditText>(R.id.note_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.note_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.note_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val subjectInput = main.findViewById<TextView>(R.id.note_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val submitButton = main.findViewById<Button>(R.id.note_finish_button)
            submitButton?.setOnClickListener {

                val date = Calendar.getInstance()
                date.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())

                val messageInput = main.findViewById<EditText>(R.id.note_message_input)
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_NOTE,
                        date,
                        null,
                        subject,
                        message
                    )
                }

            }

            println("Successfully set up the note activity")
        }


        private fun setupLinksActivity() {
            println("Setting up the links activity")

            val fromYear = main.findViewById<EditText>(R.id.links_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.links_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.links_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val subjectInput = main.findViewById<TextView>(R.id.links_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val submitButton = main.findViewById<Button>(R.id.links_finish_button)
            submitButton?.setOnClickListener {

                val date = Calendar.getInstance()
                date.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())

                val messageInput = main.findViewById<EditText>(R.id.links_message_input)
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_LINKS,
                        date,
                        null,
                        subject,
                        message
                    )
                }

            }

            println("Successfully set up the links activity")
        }


        private fun setupAttachmentActivity() {
            println("Setting up the attachment activity")

            val fromYear = main.findViewById<EditText>(R.id.attachment_from_date_year)
            val fromMonth = main.findViewById<EditText>(R.id.attachment_from_date_month)
            val fromDay = main.findViewById<EditText>(R.id.attachment_from_date_day)

            if (fromYear == null || fromMonth == null || fromDay == null) {
                displayActivity(R.layout.activity_error)
                return
            }

            Dater.setDateToday(fromYear, fromMonth, fromDay)

            val subjectInput = main.findViewById<TextView>(R.id.attachment_subject_input)
            subjectInput?.setOnClickListener {

                val options = Timetable.getFullNameList()
                Utility.showDialogue(options) {

                    subjectInput.text = Timetable.getFullNameList()[Utility.lastWhich]

                    val color = Color.parseColor("#FFFFFF")
                    subjectInput.setTextColor(color)
                }
            }

            val messageInput = main.findViewById<TextView>(R.id.attachment_message_input)
            messageInput.setOnClickListener {

                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)

                main.startActivityForResult(Intent.createChooser(intent, "Vyber súbor"), 121)
            }

            Database.filesToBeUploaded = arrayListOf()

            val submitButton = main.findViewById<Button>(R.id.attachment_finish_button)
            submitButton?.setOnClickListener {

                val date = Calendar.getInstance()
                date.set(
                    Integer.parseInt(fromYear.text.toString()),
                    Integer.parseInt(fromMonth.text.toString()),
                    Integer.parseInt(fromDay.text.toString())
                )

                val subject = Timetable.getSubject(subjectInput?.text.toString())
                val message = messageInput?.text.toString()

                displayActivity(R.layout.activity_sending_request)

                Utility.runAsync {
                    Database.sendRequest(
                        RequestType.ADD_ATTACHMENT,
                        date,
                        null,
                        subject,
                        null
                    )
                }

            }

            println("Successfully set up the attachment activity")
        }


        private fun setupActivityDatabaseError() {
            println("Setting up the database error activity")

            val closeAppButton = main.findViewById<Button>(R.id.database_close_app_button)
            closeAppButton?.setOnClickListener {
                main.finishAffinity()
            }

            println("Successfully set up the database error activity")
        }


        private fun setupErrorActivity() {
            println("Setting up the error activity")

            val closeAppButton = main.findViewById<Button>(R.id.error_close_app_button)
            closeAppButton?.setOnClickListener {
                main.finishAffinity()
            }

            println("Successfully set up the error activity")
        }


    }
}