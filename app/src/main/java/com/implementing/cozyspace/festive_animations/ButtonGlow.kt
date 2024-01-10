package com.implementing.cozyspace.festive_animations

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.ui.theme.TailwindCSSColor
import com.implementing.cozyspace.util.isDarkColor

private val menuItems = listOf(
    MenuItem(
        name = "Animations",
        icon = R.drawable.note_img,
        color = TailwindCSSColor.Green500
    ),
    MenuItem(
        name = "Compositions",
        icon = R.drawable.diary_img,
        color = TailwindCSSColor.Red500,
    ),
    MenuItem(
        name = "UIs",
        icon = R.drawable.calendar_img,
        color = TailwindCSSColor.Blue500,
    ),

)

@Composable
fun LetsTestGradientButtons() {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(24.dp, 8.dp, 24.dp, 24.dp)
    ) {



        items(menuItems) { menu ->
            ModuleButton(
                name = menu.name,
                icon = menu.icon,
                color = menu.color,
                onClick = {

                }
            )
        }

    }
}

@Composable
fun ModuleButton(
    name: String,
    @DrawableRes icon: Int,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .shadow(
//                ambientColor = color,
//                elevation = 8.dp,
//                spotColor = Color.Green
////                ambientColor = color
                spread = 8.dp,
                alpha = .25f,
                color = color,
                radius = 8.dp
            ),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = color
        ),
        onClick = onClick,
        contentPadding = PaddingValues(8.dp),
        elevation = null
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = LocalContentColor.current
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = name,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


data class MenuItem(
    val name: String,
    @DrawableRes val icon: Int,
    val color: Color,
)



@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewGradient(){
    LetsTestGradientButtons()
}


fun Modifier.shadow(
    spread: Dp = 8.dp,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = .25f,
    color: Color = Color.Gray,
    radius: Dp = 8.dp
): Modifier {
    val spreadLayer = spread.value.toInt()

    var modifier = this

    for (x in spreadLayer downTo 1) {
        modifier = modifier
            .border(
                width = 1.dp,
                color = color.copy(alpha / x),
                shape = RoundedCornerShape(radius + x.dp)
            )
            .padding(1.dp)
    }

    return modifier
}