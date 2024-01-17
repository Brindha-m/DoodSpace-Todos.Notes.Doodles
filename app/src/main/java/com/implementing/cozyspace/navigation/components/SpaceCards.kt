package com.implementing.cozyspace.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.festive_animations.shadow
import com.implementing.cozyspace.ui.theme.TailwindCSSColor


@Composable
fun SpaceRegularCard(
    title: String,
    image: Int,
    backgroundColor: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier.padding(8.dp).background(Color.Transparent),
        shape = RoundedCornerShape(25.dp),
//        colors = CardDefaults.cardColors(containerColor = gradientBrush),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)

    ) {
        Column(
            Modifier
                .background(backgroundColor)
                .clickable { onClick() }
                .aspectRatio(1.2f)
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.padding(top = 3.dp, start = 3.dp)

            )

            Image(
                modifier = Modifier
                    .size(60.dp)
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
    backgroundColor: Brush,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.padding(8.dp).background(Color.Transparent),
        shape = RoundedCornerShape(25.dp),
//        colors = CardDefaults.cardColors(containerColor = Col),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    ) {
        Column(
            Modifier
                .background(backgroundColor)
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(14.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                modifier = Modifier.padding(top = 5.dp, start = 5.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
        "Book Marks",
        R.drawable.bookmarks_img,
        backgroundColor = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color(0xFF0D47A1), // Dark blue at the start
                0.5f to Color(0xFF1976D2), // Lighter blue in the middle
                1f to Color(0xFF2196F3) // Lightest blue at the end
            )
        )
    )
}


@Composable
fun SpaceRegularCardMiddle(
    title: String,
    image: Int,
    backgroundColor: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier.padding(8.dp).background(Color.Transparent),
        shape = RoundedCornerShape(25.dp),
//        colors = CardDefaults.cardColors(containerColor = gradientBrush),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)

    ) {
        Column(
            Modifier
                .background(backgroundColor)
                .fillMaxSize(0.75f)
                .clickable { onClick() }
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
//                modifier = Modifier.padding(top = 3.dp, start = 3.dp)

            )

            Image(
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.End),
                painter = painterResource(id = image),
                contentDescription = title)

        }
    }
}
