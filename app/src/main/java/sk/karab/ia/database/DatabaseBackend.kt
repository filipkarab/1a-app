package sk.karab.ia.database

import sk.karab.ia.util.Utility
import java.io.File
import java.sql.Connection
import java.util.Calendar
import java.util.Properties

class DatabaseBackend {
    companion object {


        fun craftQuerysForAddingHomework(fromDate: Calendar, toDate: Calendar, subject: String, message: String): Array<String> {
            println("Crafting the 3 querys for adding homework")

            val newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(fromDate) +
                    "-new` (lesson, type, message, deadline) VALUES ('" +
                    subject + "', 'homework', " +
                    "'$message', '${Utility.formatDateDb(toDate)}" +
                    "');"

            val uptoQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(toDate) +
                    "-upto` (lesson, type, message, deadline) VALUES ('" +
                    subject + "', 'homework', " +
                    "'$message', '${Utility.formatDateDb(fromDate)}" +
                    "');"

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(fromDate) +
                    "-new'), ('" +
                    Utility.formatDateWeb(toDate) +
                    "-upto');"

            println("Successfully crafted the three needed querys:")
            println(newQuery)
            println(uptoQuery)
            println(changesQuery)
            return arrayOf(newQuery, uptoQuery, changesQuery)
        }


        fun craftQuerysForAddingTest(fromDate: Calendar, toDate: Calendar, subject: String, message: String): Array<String> {
            println("Crafting the 3 querys for adding test")

            val newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(fromDate) +
                    "-new` (lesson, type, message, deadline) VALUES ('" +
                    subject + "', 'test', " +
                    "'$message', '${Utility.formatDateDb(toDate)}" +
                    "');"

            val uptoQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(toDate) +
                    "-upto` (lesson, type, message, deadline) VALUES ('" +
                    subject + "', 'test', " +
                    "'$message', '${Utility.formatDateDb(fromDate)}" +
                    "');"

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(fromDate) +
                    "-new'), ('" +
                    Utility.formatDateWeb(toDate) +
                    "-upto');"

            println("Successfully crafted the three needed querys:")
            println(newQuery)
            println(uptoQuery)
            println(changesQuery)
            return arrayOf(newQuery, uptoQuery, changesQuery)
        }


        fun craftQuerysForAddingLesson(date: Calendar, subject: String, message: String): Array<String> {
            println("Crafting the 2 querys for adding lesson")

            val newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(date) +
                    "-new` (lesson, type, message) VALUES ('" +
                    subject + "', 'lesson', " +
                    "'$message');"

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(date) +
                    "-new');"

            println("Successfully crafted the two needed querys:")
            println(newQuery)
            println(changesQuery)
            return arrayOf(newQuery, changesQuery)
        }


        fun craftQuerysForAddingNote(date: Calendar, subject: String, message: String): Array<String> {
            println("Crafting the 2 querys for adding a note")

            val newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(date) +
                    "-new` (lesson, type, message) VALUES ('" +
                    subject + "', 'note', " +
                    "'$message');"

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(date) +
                    "-new');"

            println("Successfully crafted the two needed querys:")
            println(newQuery)
            println(changesQuery)
            return arrayOf(newQuery, changesQuery)
        }


        fun craftQuerysForAddingLinks(date: Calendar, subject: String, links: String): Array<String> {
            println("Crafting the 2 querys for adding a note")

            val newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(date) +
                    "-new` (lesson, type, links) VALUES ('" +
                    subject + "', 'links', " +
                    "'$links');"

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(date) +
                    "-new');"

            println("Successfully crafted the two needed querys:")
            println(newQuery)
            println(changesQuery)
            return arrayOf(newQuery, changesQuery)
        }


        fun craftQuerysForAddingAttachment(date: Calendar, subject: String, newImageCount: Int, latestImageId: Int): Array<String> {
            println("Crafting the 2 querys for adding an attachment")

            var newQuery = "INSERT INTO `" +
                    Utility.formatDateWeb(date) +
                    "-new` (lesson, type, `image-ids`) VALUES ('" +
                    subject + "', 'attachment', " +
                    "'%s');"

            var additionStr = ""
            for (i in 1..newImageCount) {
                if (i == newImageCount) {
                    additionStr += (latestImageId + i)
                } else {
                    additionStr += (latestImageId + i).toString() + ";"
                }
            }

            newQuery = newQuery.format(additionStr)

            val changesQuery = "INSERT INTO `changes` VALUES ('" +
                    Utility.formatDateWeb(date) +
                    "-new');"

            println("Successfully crafted the two needed querys:")
            println(newQuery)
            println(changesQuery)
            return arrayOf(newQuery, changesQuery)
        }


        fun craftQueryForUploadingFiles(fileCount: Int): String {
            var query = """INSERT INTO `images` (image) VALUES ("""

            for (i in 0 ..< fileCount) {
                query += if (i == 0) {
                    """?"""
                } else {
                    """,?"""
                }
            }
            query += ");"

            return query
        }


        fun craftTableNames(fromDate: Calendar?, toDate: Calendar?): ArrayList<String> {
            val tables = ArrayList<String>()

            if (fromDate != null) {
                tables.add(
                    Utility.formatDateWeb(fromDate) + "-new"
                )
            }

            if (toDate != null) {
                tables.add(
                    Utility.formatDateWeb(toDate) + "-upto"
                )
            }

            return tables
        }


        fun craftTableCreate(table: String): String {
            return """CREATE TABLE `$table` (`lesson` VARCHAR(22) NOT NULL, `type` VARCHAR(16) NOT NULL, `message` VARCHAR(512), `deadline` DATE, `image-ids` VARCHAR(72), `links` VARCHAR(512));"""
        }


        fun verifyConnection(connection: Connection, timeout: Int): Boolean {
            println("Verifying the database connection with timeout set to $timeout ms")

            return if (connection.isValid(timeout)) {
                println("Successfully virified the database connection with timeout set to $timeout ms")
                true
            } else {
                println("Could't verify the database connection")
                false
            }
        }


        fun prepareProperties(user: String, password: String): Properties {
            println("Preparing database properties")
            val properties = Properties()

            properties["user"] = user
            properties["password"] = password

            println("Successfully perpared database properties")
            return properties
        }


        fun prepareConnectionURL(adress: String, port: Int, database: String): String {
            println("Preparing the connection URL")
            return "jdbc:mysql://" +
                    adress + ':' + port +
                    '/' + database
        }

    }
}