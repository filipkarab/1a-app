package sk.karab.ia.util

import android.app.AlertDialog
import android.util.Log
import androidx.appcompat.widget.ContentFrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import sk.karab.ia.Main
import sk.karab.ia.R
import java.io.File
import java.util.Calendar
import kotlin.reflect.typeOf

class Utility {
    companion object {


        private val main = Main.i


        fun runAsync(task: Runnable) {
            Thread {
                run {
                    task.run()
                }
            }.start()
        }


        var lastWhich: Int = 0

        fun showDialogue(options: Array<String>, after: Runnable) {
            println("Showing an options dialogue")

            main?.runOnUiThread {
                val builder = AlertDialog.Builder(main)
                builder.setTitle("Vyber predmet")
                builder.setItems(options) { _, which ->
                    lastWhich = which
                    after.run()
                }
                builder.show()
            }

            println("Successfully showed an options dialogue")
        }


        fun showSnackbar(message: String) {
            println("Showing a snackbar message with content $message")

            main?.runOnUiThread {
                val snackbar = main.findViewById<ContentFrameLayout>(android.R.id.content)?.let {
                    Snackbar.make(
                        it,
                        message,
                        Snackbar.LENGTH_SHORT
                    )
                }
                snackbar?.show()
            }

            println("Successfully showd a snackbar message with content $message")
        }


        private fun fillToTwoDigits(input: String): String {
            if (input.length == 1) {
                return "0$input"
            }
            return input
        }


        fun formatDateWeb(date: Calendar): String {
            return fillToTwoDigits(date.get(Calendar.MONTH).toString()) +
                    fillToTwoDigits(date.get(Calendar.DAY_OF_MONTH).toString()) +
                    date.get(Calendar.YEAR).toString()
        }


        fun formatDateDb(date: Calendar): String {
            return date.get(Calendar.YEAR).toString() +
                    '-' + date.get(Calendar.MONTH).toString() + '-' +
                    date.get(Calendar.DAY_OF_MONTH).toString()
        }


        fun processLinksInput(messageInput: String): String {
            return messageInput.replace("\n", ";")
        }


        fun processAttachmentInput(messageInput: String): ArrayList<File> {
            val fileList = arrayListOf<File>()

            for (filePath in messageInput.split("\n")) {
                fileList.add(File(filePath))
            }

            return fileList
        }


        fun notNull(variable: Any?): Any {

            if (variable == null) {
                Log.e("NullPointerException", "Detected a null variable, that shouldnt be null in the first place")
                Activities.displayActivity(R.layout.activity_error)
                return -1
            }

            return variable
        }


    }
}