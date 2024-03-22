package sk.karab.ia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import sk.karab.ia.database.Database
import sk.karab.ia.util.Activities
import sk.karab.ia.util.Utility
import java.io.File

@Suppress("OVERRIDE_DEPRECATION")
class Main : AppCompatActivity() {


    companion object {
        var i: Main? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assignInstance()

        setContentView(R.layout.activity_loading_database)

        Utility.runAsync {
            Database.connect()
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        println("Showing main activity cause the back button was pressed")

        Activities.displayActivity(R.layout.activity_main)

        println("Successfully went back to the main activity, cuase of the back button")
    }


    // SPECIFIC CAUSE IM LAZY TO IMPLEMENT A BETTER SOLUTION
    // DO NOT TOUCH
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 121 && resultCode == RESULT_OK) {

            val messageInput = findViewById<TextView>(R.id.attachment_message_input)

            val selectedFile = Utility.notNull(data?.data) as Uri // The URI with the location of the file
            Database.filesToBeUploaded.add(selectedFile)

            if (messageInput.text == "Klikni pre výber súboru") {
                messageInput.text = "Obrázok"
            } else {
                messageInput.text = messageInput.text.toString() + "\n" + "Obrázok"
            }

            val color = Color.parseColor("#FFFFFF")
            messageInput.setTextColor(color)
        }

    }


    private fun assignInstance() {
        println("Assigning the main instance")
        i = this
        println("Successfully assigned the main instance")
    }


}