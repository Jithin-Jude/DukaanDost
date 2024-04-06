package dev.artium.dukaandost.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.artium.dukaandost.DukaanDostConstants
import dev.artium.dukaandost.DukkanDostUtils.capitalizeFirstLetter
import dev.artium.dukaandost.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOptionBottomSheet(
    preSelectedOption: String,
    onSelectOption: (option: String) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val sortOptionsToShow = listOf(
        DukaanDostConstants.SORT_RATING,
        DukaanDostConstants.SORT_PRICE_LOW_TO_HIGH,
        DukaanDostConstants.SORT_PRICE_HIGH_TO_HIGH
    )
    LaunchedEffect(Unit) {
        modalBottomSheetState.expand()
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LazyColumn(Modifier.padding(bottom = 70.dp, start = 24.dp, end = 24.dp)) {
            items(sortOptionsToShow) { option ->
                SortOptionItem(option, preSelectedOption == option, onSelectOption = onSelectOption)
            }
        }
    }
}

@Composable
fun SortOptionItem(
    option: String,
    selected: Boolean,
    onSelectOption: (option: String) -> Unit
) {
    val checkedState = remember { mutableStateOf(selected) }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                checkedState.value = true
                onSelectOption(option)
            }) {
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = option.capitalizeFirstLetter(), style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = checkedState.value,
                onClick = {
                    checkedState.value = true
                    onSelectOption(option)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}