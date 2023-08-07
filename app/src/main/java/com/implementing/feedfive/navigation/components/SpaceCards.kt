package com.implementing.feedfive.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implementing.feedfive.R


@Composable
fun SpaceRegularCard(
    title: String,
    image: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            Modifier
                .clickable { onClick() }
                .aspectRatio(1.0f)
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.End),
                painter = painterResource(id = image),
                contentDescription = title)

        }
    }
}

@Composable
fun SpaceWideCard(
    title: String,
    image: Int,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(18.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.End),
                painter = painterResource(id = image),
                contentDescription = title)

        }
    }
}

@Preview
@Composable
fun SpaceRegularCardPreview() {
    SpaceRegularCard(
        "Notes",
        R.drawable.notes_img,
        Blue
    )
}