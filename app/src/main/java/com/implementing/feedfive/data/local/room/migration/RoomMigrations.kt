package com.implementing.feedfive.data.local.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Added note folders
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `diary` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `created_date` INTEGER NOT NULL, `updated_date` INTEGER NOT NULL, `mood` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE note_folders (name TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
// Create the new table
        database.execSQL("CREATE TABLE IF NOT EXISTS `notes_new` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`folder_id` INTEGER, " +
                "`title` TEXT NOT NULL, " +
                "`content` TEXT NOT NULL, " +
                "`created_date` INTEGER NOT NULL, " +
                "`updated_date` INTEGER NOT NULL, " +
                "`pinned` INTEGER NOT NULL)")

        // Copy data from the old table to the new table
        database.execSQL("INSERT INTO `notes_new` (`id`, `folder_id`, `title`, `content`, " +
                "`created_date`, `updated_date`, `pinned`) " +
                "SELECT `id`, `folder_id`, `title`, `content`, " +
                "`created_date`, `updated_date`, `pinned` FROM `notes`")

        // Drop the old table
        database.execSQL("DROP TABLE `notes`")

        // Rename the new table to the original table name
        database.execSQL("ALTER TABLE `notes_new` RENAME TO `notes`")

        // Create the foreign key constraint if necessary
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_notes_folder_id` ON `notes` (`folder_id`)")




//        database.execSQL("CREATE TABLE IF NOT EXISTS `notes` (" +
//                "`title` TEXT NOT NULL, " +
//                "`content` TEXT NOT NULL, " +
//                "`created_date` INTEGER NOT NULL, " +
//                "`updated_date` INTEGER NOT NULL, " +
//                "`pinned` INTEGER NOT NULL, " +
//                "`folder_id` INTEGER, " +
//                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                "FOREIGN KEY (`folder_id`) REFERENCES `note_folders` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE" +
//                ")")

//        database.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`title` TEXT NOT NULL, `content` TEXT NOT NULL, `created_date` INTEGER NOT NULL, `updated_date` INTEGER NOT NULL, `pinned` INTEGER NOT NULL, `folder_id` INTEGER, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY (folder_id) REFERENCES note_folders (id) ON UPDATE NO ACTION ON DELETE CASCADE)")
//        database.execSQL("INSERT INTO notes(title, content, created_date, updated_date, pinned, id) SELECT title, content, created_date, updated_date, pinned, id FROM notes")
//        database.execSQL("DROP TABLE notes")
//        database.execSQL("ALTER TABLE notes RENAME TO note")
    }
}
