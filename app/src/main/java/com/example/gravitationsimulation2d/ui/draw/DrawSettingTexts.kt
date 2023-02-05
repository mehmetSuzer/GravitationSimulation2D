package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gravitationsimulation2d.data.SettingText
import com.example.gravitationsimulation2d.data_source


@Composable
fun CelestialBodySettingTextList(state: Boolean, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier
        .background(MaterialTheme.colors.background)
    ) {
        items(data_source.loadSettingTexts()) { settingText ->
            CelestialBodySettingText(settingText, state, { input -> settingText.setInput(input) })
        }
    }
}

@Composable
fun CelestialBodySettingText(settingText: SettingText, state: Boolean, onChange: (String) -> Boolean, modifier: Modifier = Modifier) {
    var input by rememberSaveable {
        mutableStateOf(settingText.userInput)
    }
    Card (
        modifier = modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = settingText.firstTextId),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { if (onChange(it)) { input = it } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                label = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = settingText.textFiledHint,
                            fontSize = 10.sp,
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (settingText.prevInput != null) {
                            Text(
                                text = settingText.prevInput!!,
                                fontSize = 10.sp,
                                color = MaterialTheme.colors.onSecondary,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
            Text(
                text = stringResource(id = settingText.secondTextId),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}