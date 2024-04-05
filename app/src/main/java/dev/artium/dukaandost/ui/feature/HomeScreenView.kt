package dev.artium.dukaandost.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.artium.dukaandost.MainActivity
import dev.artium.dukaandost.ui.theme.AppBackground
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.DukaanDostTheme


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    DukaanDostTheme {
        HomeScreenView(navController, false)
    }
}

@Composable
fun HomeScreenView(navController: NavHostController, isExpandedScreen: Boolean) {
    Scaffold(
        Modifier.background(AppBackground)
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBarWithClear(
                modifier = Modifier
                    .fillMaxWidth(),
                onSendClick = { value ->
                    navController.navigate(MainActivity.Routes.ProductDetailScreen.route + "/$value")
                },
            )
        }
    }
}

@Composable
fun SearchBarWithClear(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textState = remember { mutableStateOf("") }

    Row(modifier = modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                label = { Text("Search") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DividerGrey,
                    focusedLabelColor = DividerGrey,
                    unfocusedBorderColor = DividerGrey,
                    unfocusedLabelColor = DividerGrey,
                    cursorColor = DividerGrey
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    onSendClick(textState.value)
                    keyboardController?.hide()
                })
            )
            if (textState.value.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Clear,
                        tint = DividerGrey,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                textState.value = ""
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}