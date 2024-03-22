package sk.karab.ia.database

import android.net.Uri
import com.mysql.jdbc.Blob
import sk.karab.ia.Main
import sk.karab.ia.R
import sk.karab.ia.configurable.DatabaseAuth
import sk.karab.ia.util.Activities
import sk.karab.ia.util.Utility
import java.io.ByteArrayInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import java.util.Calendar

class Database {
    companion object {


        private var connection: Connection? = null
        var filesToBeUploaded = arrayListOf<Uri>()


        fun connect() {
            println("Connecting to the database")

            val properties = DatabaseBackend.prepareProperties(
                DatabaseAuth.getUser(),
                DatabaseAuth.getPassword()
            )
            val connectionString = DatabaseBackend.prepareConnectionURL(
                DatabaseAuth.getAdress(),
                DatabaseAuth.getPort(),
                DatabaseAuth.getDatabase()
            )

            try {

                Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance()
                connection = DriverManager.getConnection(connectionString, properties)

                println("Made first contact with the database, veryfing the connection")

                if (connection?.let {
                        DatabaseBackend.verifyConnection(it, DatabaseAuth.getTimeout())
                    } == true) {

                    println("Successfully connected and verified the database connection")
                    Activities.displayActivity(R.layout.activity_main)

                } else {
                    println("Couldn't verify the connection, displaying an error screen")
                    Activities.displayActivity(R.layout.activity_database_error)
                }

            } catch (exception: SQLException) {
                println("Caught an exception while connecting to the database, displaying an error screen")
                Activities.displayActivity(R.layout.activity_database_error)
                exception.printStackTrace()
            }

        }


        fun sendRequest(type: RequestType, fromDate: Calendar, toDate: Calendar?, subject: String, message: String?) {
            println("Sending a request:")
            println("type.... $type")
            println("from.... ${fromDate.get(Calendar.YEAR)}-${fromDate.get(Calendar.MONTH) + 1}-${fromDate.get(Calendar.DAY_OF_MONTH)}")
            println("to.... ${toDate?.get(Calendar.YEAR)}-${toDate?.get(Calendar.MONTH)?.plus(1)}-${toDate?.get(Calendar.DAY_OF_MONTH)}")
            println("subject.... $subject")
            println("message.... $message")

            when (type) {
                RequestType.ADD_HOMEWORK -> toDate?.let { message?.let { it1 ->
                    addHomework(fromDate, it, subject,
                        it1
                    )
                } }
                RequestType.ADD_TEST -> toDate?.let { message?.let { it1 ->
                    addTest(fromDate, it, subject,
                        it1
                    )
                } }
                RequestType.ADD_LESSON -> message?.let { addLesson(fromDate, subject, it) }
                RequestType.ADD_NOTE -> message?.let { addNote(fromDate, subject, it) }
                RequestType.ADD_LINKS -> message?.let { addLinks(fromDate, subject, it) }
                RequestType.ADD_ATTACHMENT -> addAttachment(fromDate, subject)
            }

            Activities.displayActivity(R.layout.activity_main)
            Utility.showSnackbar("Úspešne spracovaná požiadavka")

            println("Successfully sent the previus request")
        }


        private fun addHomework(fromDate: Calendar, toDate: Calendar, subject: String, message: String) {
            println("Adding homework to the database")

            val tables = DatabaseBackend.craftTableNames(fromDate, toDate)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingHomework(fromDate, toDate, subject, message)
            for (query in querys) {

                val statement = connection?.createStatement()
                try {

                    statement?.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement?.close()
                }
            }

            println("Successfully added homework to the database")
        }


        private fun addTest(fromDate: Calendar, toDate: Calendar, subject: String, message: String) {
            println("Adding a test to the database")

            val tables = DatabaseBackend.craftTableNames(fromDate, toDate)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingTest(fromDate, toDate, subject, message)
            for (query in querys) {

                val statement = connection?.createStatement()
                try {

                    statement?.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement?.close()
                }
            }

            println("Successfully added a test to the database")
        }


        private fun addLesson(date: Calendar, subject: String, message: String) {
            println("Adding a lesson to the database")

            val tables = DatabaseBackend.craftTableNames(date, null)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingLesson(date, subject, message)
            for (query in querys) {

                val statement = connection?.createStatement()
                try {

                    statement?.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement?.close()
                }
            }

            println("Successfully added a lesson to the database")
        }


        private fun addNote(date: Calendar, subject: String, message: String) {
            println("Adding a note to the database")

            val tables = DatabaseBackend.craftTableNames(date, null)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingNote(date, subject, message)
            for (query in querys) {

                val statement = connection?.createStatement()
                try {

                    statement?.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement?.close()
                }
            }

            println("Successfully added a note to the database")
        }


        private fun addLinks(date: Calendar, subject: String, message: String) {
            println("Adding links to the database")

            val links = Utility.processLinksInput(message)

            val tables = DatabaseBackend.craftTableNames(date, null)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingLinks(date, subject, links)
            for (query in querys) {

                val statement = Utility.notNull(connection?.createStatement()) as Statement
                try {

                    statement.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement.close()
                }
            }

            println("Successfully added links to the database")
        }


        private fun addAttachment(date: Calendar, subject: String) {
            println("Adding attachment to the database")

            val tables = DatabaseBackend.craftTableNames(date, null)
            createTablesIfDontExist(tables)

            val querys = DatabaseBackend.craftQuerysForAddingAttachment(date, subject, filesToBeUploaded.count(), getLatestImageId())
            for (query in querys) {

                val statement = Utility.notNull(connection?.prepareStatement(query)) as PreparedStatement
                try {

                    statement.executeUpdate(query)

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                } finally {
                    statement.close()
                }
            }

            val uploadQuery = DatabaseBackend.craftQueryForUploadingFiles(filesToBeUploaded.count())
            val statement = Utility.notNull(
                connection?.prepareStatement(uploadQuery)
            ) as PreparedStatement

            for (i in 1 .. filesToBeUploaded.count()) {
                val file = filesToBeUploaded[i - 1]
                statement.setBlob(i, Main.i?.contentResolver?.openInputStream(file))
            }

            try {
                statement.executeUpdate()
            } catch (exception: SQLException) {
                exception.printStackTrace()
                println("An error occurred while executing an query in the database, thoring an error activity")
                Activities.displayActivity(R.layout.activity_database_error)
                return
            }

            println("Successfully added attachment to the database")
        }


        private fun getLatestImageId(): Int {

            val statement = Utility.notNull(
                connection?.prepareStatement("""SELECT id FROM images ORDER BY id DESC LIMIT 0, 1;""")
            ) as PreparedStatement

            return try {

                val results = statement.executeQuery()
                results.next()
                results.getInt("id")

            } catch (exception: SQLException) {

                println("Couldn't get the latest index of the images table, throwing an database error acitvity")
                Activities.displayActivity(R.layout.activity_database_error)
                exception.printStackTrace()
                -1

            }
        }


        private fun createTablesIfDontExist(tableList: ArrayList<String>) {
            for (table in tableList) {
                println("Checking if table $table exists")

                val statement = Utility.notNull(connection?.createStatement()) as Statement
                try {

                    var query = """SELECT EXISTS (SELECT * FROM information_schema.tables
                        | WHERE table_schema = 's7402_dsbot' AND table_name = '$table');""".trimMargin()
                    statement.executeQuery(query)

                    val result = statement.resultSet
                    query = query.replace("SELECT EXISTS", "EXISTS")
                        .replace(";", "")

                    result?.next()
                    if (result?.getInt(query) == 0) {
                        println("It doesn't, creating now")

                        val newStatement = Utility.notNull(connection?.createStatement()) as Statement
                        try {

                            val newQuery = DatabaseBackend.craftTableCreate(table)
                            newStatement.executeUpdate(newQuery)

                        } catch (innerException: SQLException) {
                            innerException.printStackTrace()
                            println("An error occurred while executing an query in the database, throwing an error activity")
                            Activities.displayActivity(R.layout.activity_database_error)
                            return
                        }

                        println("Successfully created table $table")
                    } else {
                        println("It does, all good")
                    }

                } catch (exception: SQLException) {
                    exception.printStackTrace()
                    println("An error occurred while executing an query in the database, throwing an error activity")
                    Activities.displayActivity(R.layout.activity_database_error)
                    return
                }

            }
        }


    }
}