package com.bishal.securepasswordmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.bishal.securepasswordmanager.ui.theme.blackDark

/**
 * A customizable button using Jetpack Compose for Android.
 *
 * @param text Text displayed on the button.
 * @param onClick Callback function invoked when the button is clicked.
 * @param modifier Modifier for customizing the button's appearance and behavior.
 * @param backgroundColor Background color of the button.
 * @param contentColor Text color of the button's text.
 * @param shape Shape of the button.
 * @param height Height of the button.
 * @param width Optional width of the button. If null, the button fills the parent width.
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = blackDark,
    contentColor: Color = Color.White,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    height: Int = 56,
    width: Int? = null
) {
    Column(
        modifier = modifier
            .height(height.dp)
            .then(if (width != null) Modifier.width(width.dp) else Modifier.fillMaxWidth())
            .background(backgroundColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = contentColor,
        )
    }
}
