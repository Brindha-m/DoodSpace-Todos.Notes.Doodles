package com.implementing.feedfive.inappscreens.settings

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsBasicLinkItem(
    @StringRes
    title: Int,
    subtitle: String = "",
    @DrawableRes
    icon: Int,
    link: String = "",
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    SettingsItemCard(
        cornerRadius = 16.dp,
        onClick = {
            if (link.isNotBlank()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)
                context.startActivity(intent)
            } else onClick()
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = title)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}