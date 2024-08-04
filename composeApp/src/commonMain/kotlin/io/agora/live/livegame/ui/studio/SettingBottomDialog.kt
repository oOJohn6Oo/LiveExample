package io.agora.live.livegame.ui.studio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingBottomDialogScreen(modifier: Modifier = Modifier,
                              showOrHideSettingDialog: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Setting",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.subtitle1
            )
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        FlowRow {

            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
            IconButton(
                onClick = { showOrHideSettingDialog(false) }
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

    }
}