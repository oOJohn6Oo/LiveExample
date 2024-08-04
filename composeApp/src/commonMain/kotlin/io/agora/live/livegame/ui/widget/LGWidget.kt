import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

object LGWidget {
    @Composable
    fun LiftableTopAppBar(
        modifier: Modifier = Modifier,
        navIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
        onClickNavIcon: (() -> Unit)? = null,
        backgroundColor: Color = MaterialTheme.colors.background,
        elevation: Dp = AppBarDefaults.TopAppBarElevation,
        contentPadding: PaddingValues,
        title: String
    ) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = backgroundColor,
            elevation = elevation,
            contentPadding = contentPadding,
        ) {
            Box {
                if (navIcon != null && onClickNavIcon != null) {
                    IconButton(onClick = onClickNavIcon,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp)
                            .size(32.dp),
                        content = {
                            Icon(imageVector = navIcon, contentDescription = "")
                        }
                    )
                }
                Text(
                    title,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                )
            }
        }
    }

    /**
     * 蜜蜂常用输入框
     *
     * @param maxLength 最大字符个数
     * @param hideCount 是否隐藏计数
     */
    @Composable
    fun LGTextField(
        modifier: Modifier = Modifier,
        textFieldModifier: Modifier = Modifier,
        text: String = "",
        enable: Boolean = true,
        placeholder: String = "",
        cursorBrush: Brush = SolidColor(MaterialTheme.colors.primary),
        maxLength: Int = 500,
        maxLines: Int = Int.MAX_VALUE,
        focusOnStart: Boolean = false,
        placeHolderStyle: TextStyle = MaterialTheme.typography.caption,
        placeHolderColor: State<Color> = TextFieldDefaults.textFieldColors()
            .placeholderColor(enable),
        hideCount: Boolean = false,
        countStyle: TextStyle = MaterialTheme.typography.caption,
        countColor: State<Color> = TextFieldDefaults.textFieldColors().placeholderColor(enable),

        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,

        contentAlignment: Alignment = Alignment.CenterStart,
        focusRequester: FocusRequester = remember { FocusRequester() },
        onLengthOverflow: (() -> Unit)? = null,
        onFocusChanged: ((Boolean) -> Unit)? = null,
        onValueChanged: (String) -> Unit,
    ) {
        var isDescTextFieldFocused by remember { mutableStateOf(false) }

        LaunchedEffect(1) {
            if (focusOnStart) {
                focusRequester.requestFocus()
            } else {
                focusRequester.freeFocus()
            }
        }

        Column(modifier) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isDescTextFieldFocused = it.isFocused
                        onFocusChanged?.invoke(it.isFocused)
                    }
                    .then(textFieldModifier),
                value = text,
                maxLines = maxLines,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChanged(it)
                    } else {
                        onLengthOverflow?.invoke()
                        onValueChanged(it.take(maxLength))
                    }
                },
                cursorBrush = cursorBrush,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            ) { rawTextField ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = contentAlignment) {
                    if (text.isEmpty()) {
                        Text(
                            style = placeHolderStyle,
                            text = placeholder,
                            color = placeHolderColor.value,
                            maxLines = maxLines
                        )
                    }
                    rawTextField()
                }
            }
            if (!hideCount) {
                // Count 提示文字
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = (if (isDescTextFieldFocused) (maxLength - text.length).toString() else ""),
                    style = countStyle,
                    color = countColor.value,
                    textAlign = TextAlign.End
                )
            }
        }
    }

    @Composable
    fun LGToast(modifier: Modifier = Modifier, msg: String) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        ) {
            Box(modifier = modifier) {
                Text(msg, color = MaterialTheme.colors.surface)
            }
        }
//        AlertDialog(
//            onDismissRequest = {},
//            buttons = {},
//            text = {
//                Text(msg)
//            },
//            modifier = modifier,
//            shape = MaterialTheme.shapes.large,
//            properties = DialogProperties(
//
//            )
//        )
//        Dialog(
//            onDismissRequest = {},
//            properties = DialogProperties(
//            )
//        ){
//            Box(modifier = modifier){
//                Text(msg)
//            }
//        }
    }

}
