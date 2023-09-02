package com.implementing.cozyspace.data.local.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Added note folders
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`title` TEXT NOT NULL," +
                "`description` TEXT NOT NULL," +
                "`priority` INTEGER NOT NULL," +
                "`dueDate` INTEGER NOT NULL," +
                "`is_completed` INTEGER NOT NULL," +
                "`created_date` INTEGER NOT NULL," +
                "`updated_date` INTEGER NOT NULL," +
                "`sub_tasks` TEXT NOT NULL" +
                ")")


        database.execSQL("CREATE TABLE IF NOT EXISTS `alarms` (\n" +
                "    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    `time` INTEGER NOT NULL )"
        )


        database.execSQL("CREATE TABLE IF NOT EXISTS `diary` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `created_date` INTEGER NOT NULL, `updated_date` INTEGER NOT NULL, `mood` INTEGER NOT NULL)")

//        database.execSQL("CREATE TABLE note_folders (name TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")


// Create the new table
//        database.execSQL("CREATE TABLE IF NOT EXISTS `notes_new` (" +
//                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                "`folder_id` INTEGER, " +
//                "`title` TEXT NOT NULL, " +
//                "`content` TEXT NOT NULL, " +
//                "`created_date` INTEGER NOT NULL, " +
//                "`updated_date` INTEGER NOT NULL, " +
//                "`pinned` INTEGER NOT NULL)")
//
//        // Copy data from the old table to the new table
//        database.execSQL("INSERT INTO `notes_new` (`id`, `folder_id`, `title`, `content`, " +
//                "`created_date`, `updated_date`, `pinned`) " +
//                "SELECT `id`, `folder_id`, `title`, `content`, " +
//                "`created_date`, `updated_date`, `pinned` FROM `notes`")
//
//        // Drop the old table
//        database.execSQL("DROP TABLE `notes`")
//
//        // Rename the new table to the original table name
//        database.execSQL("ALTER TABLE `notes_new` RENAME TO `notes`")
//
//        // Create the foreign key constraint if necessary
//        database.execSQL("CREATE INDEX IF NOT EXISTS `index_notes_folder_id` ON `notes` (`folder_id`)")

    }
}
