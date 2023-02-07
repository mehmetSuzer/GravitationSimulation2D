package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gravitationsimulation2d.R
import com.example.gravitationsimulation2d.func.strip

const val MAX_CHAR_NUMBER = 20

@Composable
fun RecordSavePopUp(
    saveRecord: (String) -> Unit,
    cancelSaving: () -> Unit,
    modifier: Modifier = Modifier,
    txtState: MutableState<String> = rememberSaveable { mutableStateOf("") }
) {
    Dialog(onDismissRequest = cancelSaving) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = (MAX_CHAR_NUMBER-txtState.value.length).toString(),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    .fillMaxWidth(),
                value = txtState.value,
                singleLine = true,
                onValueChange = { txt ->
                    if (txt.length <= MAX_CHAR_NUMBER) {
                        txtState.value = txt
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { cancelSaving() },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel_button),
                        style = MaterialTheme.typography.button
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { saveRecord(strip(txtState.value)) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.save_button),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}