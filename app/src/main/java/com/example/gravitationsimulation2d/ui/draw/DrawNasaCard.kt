package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gravitationsimulation2d.R
import com.example.gravitationsimulation2d.data.remote.NasaDto


@Composable
fun DrawNasaCard(
    nasaDto: NasaDto?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (nasaDto != null) {
                Text(
                    text = nasaDto.title,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(nasaDto.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = nasaDto.explanation,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = nasaDto.copyright,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.align(Alignment.End)
                )
            } else {
                Text(
                    text = stringResource(R.string.astronomy_picture_msg),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}