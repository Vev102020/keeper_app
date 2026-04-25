package com.example.keeper_app.presentation.ui.theme.custom


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.LocalColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    text: String,
    icon: Painter,
    iconText: String?,
    onClick: () -> Unit
){
    Column {
        TopAppBar(

            title = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onClick
                ){
                    Icon(
                        painter = icon,
                        contentDescription = iconText ?: "",
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_large_super))
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.LocalColors.appBar.shape))
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomAppBarPreview(){
    CustomAppBar(
        text = "Заголовок",
        icon = painterResource(R.drawable.ic_arrow_back),
        iconText = "Назад",
        onClick = {}
    )
}