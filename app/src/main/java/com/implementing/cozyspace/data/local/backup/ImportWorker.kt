package com.implementing.cozyspace.data.local.backup


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.implementing.cozyspace.R
import com.implementing.cozyspace.data.local.backup.encryption.decryptData
import com.implementing.cozyspace.data.local.backup.encryption.getSecretKey
import com.implementing.cozyspace.data.local.dao.BookmarkDao
import com.implementing.cozyspace.data.local.dao.DiaryDao
import com.implementing.cozyspace.data.local.dao.NoteDao
import com.implementing.cozyspace.data.local.dao.TaskDao
import com.implementing.cozyspace.getString
import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.util.BackupUtil.listFromJson
import com.implementing.cozyspace.util.BackupUtil.objectFromJson
import com.implementing.cozyspace.util.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream

@HiltWorker
class ImportWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notesDao: NoteDao,
    private val tasksDao: TaskDao,
    private val diaryDao: DiaryDao,
    private val bookmarksDao: BookmarkDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork() = withContext(Dispatchers.IO) {

        val backupDir = inputData.getString("uri") ?: return@withContext Result.failure()

        import(backupDir)

    }

    private suspend fun import(uriString: String?): Result {
        val contentResolver = applicationContext.contentResolver

        val uri = Uri.parse(uriString)
        val fileName = uri.fileName()

        uri?.let {
            val json = contentResolver.readTextFromFile(uri)
            json?.let {
                try {
                    return when (fileName) {
                        Constants.BACKUP_NOTES_FILE_NAME -> {
                            val notesObject =
                                json.objectFromJson<NotesBackUp>() ?: return Result.failure()
                            val folders = notesObject.folders.map { it.copy(id = 0) }
                            val notes = notesObject.notes.map { it.copy(id = 0, folderId = null) }
                            notesDao.insertNoteFolders(folders)
                            notesDao.insertNotes(notes)
                            Result.success(workDataOf("success" to getString(R.string.notes)))
                        }

                        Constants.BACKUP_TASKS_FILE_NAME -> {
                            val tasks = json.listFromJson<Task>()?.map { it.copy(id = 0) }
                                ?: return Result.failure()
                            tasksDao.insertTasks(tasks)
                            Result.success(workDataOf("success" to getString(R.string.tasks)))
                        }

                        Constants.BACKUP_DIARY_FILE_NAME -> {
                            val diaryEntries = json.listFromJson<Diary>()?.map { it.copy(id = 0) }
                                ?: return Result.failure()
                            diaryDao.insertEntries(diaryEntries)
                            Result.success(workDataOf("success" to getString(R.string.diary)))
                        }

                        Constants.BACKUP_BOOKMARKS_FILE_NAME -> {
                            val bookmarks = json.listFromJson<Bookmark>()?.map { it.copy(id = 0) }
                                ?: return Result.failure()
                            bookmarksDao.insertBookmarks(bookmarks)
                            Result.success(workDataOf("success" to getString(R.string.bookmarks)))
                        }

                        else -> Result.failure()
                    }
                } catch (e: Exception) {
                    return Result.failure()
                }
            }
        } ?: return Result.failure()
    }

    private fun ContentResolver.readTextFromFile(uri: Uri?): String? {
        return uri?.let {
            openFileDescriptor(uri, "r")?.use { pfd ->
                FileInputStream(pfd.fileDescriptor).use { inputStream ->
                    inputStream.bufferedReader().use { it.readText() }
                    // Read encrypted data from file
//                    val encryptedData = inputStream.bufferedReader().use { it.readText() }
//
//                    // Log the encrypted data for debugging
////                    Log.d("ImportWorker", "Encrypted data read from file: $encryptedData")
//
////                    // Decrypt the data
////                    val secretKey = getSecretKey()  // Retrieve the key used for encryption
////                    return decryptData(encryptedData, secretKey)

                }
            }
        }
    }

    private fun Uri.fileName(): String {
        val resolver = applicationContext.contentResolver
        val query = resolver.query(
            this,
            null,
            null,
            null,
            null
        )
        query?.use { cursor ->
            val id = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            while (cursor.moveToNext()) {
//                return cursor.getString(id)
//            }
            while (cursor.moveToNext()) {
                val fileName = cursor.getString(id)
//                Log.d("ImportWorker", "File name: $fileName")  // Debug file name
                return fileName
            }
        }
        return ""
    }

}