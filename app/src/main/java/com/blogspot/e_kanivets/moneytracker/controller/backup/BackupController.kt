package com.blogspot.e_kanivets.moneytracker.controller.backup

import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxBackupAsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxFetchBackupListAsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxRemoveBackupAsyncTask
import com.blogspot.e_kanivets.moneytracker.controller.backup.tasks.DropboxRestoreBackupAsyncTask
import com.dropbox.core.v2.DbxClientV2

/**
 * Controller class to encapsulate backup logic.
 * Created on 8/10/16.
 *
 * @author Evgenii Kanivets
 */
class BackupController(private val formatController: FormatController, filesDir: String) {

    var onBackupListener: OnBackupListener? = null

    private val appDbFileName: String = "$filesDir/databases/database"

    fun makeBackup(dbClient: DbxClientV2) {
        val fileName = formatController.formatDateAndTime(System.currentTimeMillis())
        DropboxBackupAsyncTask(dbClient, fileName, appDbFileName, onBackupListener).execute()
    }

    fun restoreBackup(dbClient: DbxClientV2, backupName: String) {
        DropboxRestoreBackupAsyncTask(dbClient, appDbFileName, backupName, onBackupListener).execute()
    }

    fun fetchBackups(dbClient: DbxClientV2) {
        DropboxFetchBackupListAsyncTask(dbClient, onBackupListener).execute()
    }

    fun removeBackup(dbClient: DbxClientV2, backupName: String) {
        DropboxRemoveBackupAsyncTask(dbClient, backupName, onBackupListener).execute()
    }

    interface OnBackupListener {

        fun onBackupsFetched(backupList: List<String>)

        fun onBackupSuccess()

        fun onBackupFailure(reason: String?)

        fun onRestoreSuccess(backupName: String)

        fun onRestoreFailure(reason: String?)

        fun onRemoveSuccess()

        fun onRemoveFailure(reason: String?)

        companion object {
            const val SUCCESS = "success"
            const val ERROR_AUTHENTICATION = "error_authentication"
        }
    }

}
