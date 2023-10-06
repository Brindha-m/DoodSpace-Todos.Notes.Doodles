package com.implementing.cozyspace.inappscreens.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.work.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.implementing.cozyspace.FiveHiltMain
import com.implementing.cozyspace.R
import com.implementing.cozyspace.data.local.backup.ExportWorker
import com.implementing.cozyspace.data.local.backup.ImportWorker
import com.implementing.cozyspace.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.cozyspace.inappscreens.settings.viewmodel.SettingsViewModel
import com.implementing.cozyspace.navigation.Screen
import java.util.UUID

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImportExportScreen(
    navController: NavHostController,
) {

    val writeStoragePermission = rememberPermissionState(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val workManager = remember {
        WorkManager.getInstance(FiveHiltMain.appContext)
    }
    val exportRequest by remember {
        derivedStateOf {
            OneTimeWorkRequestBuilder<ExportWorker>().build()
        }
    }
    var importRequestId by remember {
        mutableStateOf<UUID?>(null)
    }

    val exportWorkInfo = workManager.getWorkInfoByIdLiveData(exportRequest.id).observeAsState()
    val importWorkInfo = if (importRequestId != null) {
        workManager.getWorkInfoByIdLiveData(importRequestId!!).observeAsState()
    } else {
        null
    }

    val exportProgress = exportWorkInfo.value?.progress?.getInt("progress", 0)

    val chooseDirectoryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let { uri ->
                val importRequest =
                    OneTimeWorkRequestBuilder<ImportWorker>().setInputData(workDataOf("uri" to uri.toString()))
                        .build()
                importRequestId = importRequest.id
                workManager.enqueueUniqueWork("import", ExistingWorkPolicy.KEEP, importRequest)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.export_import),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        fontSize = 16.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Image(painter = painterResource(id = R.drawable.backarrow_ic), contentDescription = "back")
                    }
                },
            )
        },


    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White ),
                onClick = {
                    if (Build.VERSION.SDK_INT < 29 && !writeStoragePermission.hasPermission) {
                        writeStoragePermission.launchPermissionRequest()
                    } else if (Build.VERSION.SDK_INT < 29 && !writeStoragePermission.hasPermission && !writeStoragePermission.shouldShowRationale) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts(
                            "package",
                            FiveHiltMain.appContext.packageName,
                            null
                        )
                        FiveHiltMain.appContext.startActivity(intent)
                    } else {
                        workManager.enqueueUniqueWork(
                            "export",
                            ExistingWorkPolicy.KEEP,
                            exportRequest
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (Build.VERSION.SDK_INT >= 29 || writeStoragePermission.hasPermission)
                    Icon(painterResource(id = R.drawable.ic_export), null)
                Text(
                    text = stringResource(
                        if (Build.VERSION.SDK_INT < 29 && !writeStoragePermission.hasPermission)
                            R.string.grant_permission_to_export
                        else
                            R.string.export
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(12.dp)
                )
            }

            if (exportProgress != null && exportProgress > 0) {
                LinearProgressIndicator(
                    progress = exportProgress.toFloat() / 100,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                Text(
                    text = "$exportProgress%",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }

            if (exportWorkInfo.value?.outputData?.getBoolean("success", false) == true) {
                Text(
                    text = stringResource(R.string.export_success),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = Color.Green
                )
            } else if (exportWorkInfo.value?.state == WorkInfo.State.FAILED) {
                Text(
                    text = stringResource(R.string.export_failed),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White ),
                onClick = {
                    chooseDirectoryLauncher.launch(arrayOf("text/plain"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(painterResource(id = R.drawable.ic_import), null)
                Text(
                    text = stringResource(R.string.import_data),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(12.dp)
                )
            }

            if (importWorkInfo?.value?.outputData?.getString("success")?.isNotBlank() == true)
                Text(
                    text = stringResource(
                        R.string.import_success,
                        importWorkInfo.value?.outputData?.getString("success") ?: ""
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Start,
                )


            if (importWorkInfo?.value?.state == WorkInfo.State.FAILED) {
                Text(
                    text = stringResource(R.string.import_failed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (importWorkInfo?.value?.state == WorkInfo.State.RUNNING) {
                CircularProgressIndicator(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp)
                )
                Text(
                    text = stringResource(R.string.importing),
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}
