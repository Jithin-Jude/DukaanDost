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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.artium.dukaandost.DukkanDostUtils.capitalizeFirstLetter
import dev.artium.dukaandost.ui.theme.Purple40
import dev.artium.dukaandost.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptionBottomSheet(
    listOfCategories: List<String>,
    preSelectedFilters: List<String>,
    onSelectFilter: (selectedFilters: List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        modalBottomSheetState.expand()
    }
    val filtersToShow = mutableListOf<String>()
    filtersToShow.addAll(listOfCategories)

    val selectedFilters = remember { preSelectedFilters.toMutableList() }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LazyColumn(Modifier.padding(bottom = 70.dp, start = 24.dp, end = 24.dp)) {
            items(filtersToShow) { filter ->
                FilterOptionItem(
                    filter,
                    selectedFilters.contains(filter),
                    onSelectOption = { option, selected ->
                        if (selected) {
                            selectedFilters.add(option)
                        } else {
                            selectedFilters.remove(option)
                        }
                        onSelectFilter(selectedFilters)
                    })
            }
            item {
                Text(text = "Clear filter",
                    style = Typography.bodyLarge, color = Purple40,
                    modifier = Modifier.clickable {
                        selectedFilters.clear()
                        onSelectFilter(selectedFilters)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun FilterOptionItem(
    option: String,
    selected: Boolean,
    onSelectOption: (option: String, selected: Boolean) -> Unit
) {
    val checkedState = remember { mutableStateOf(selected) }
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                checkedState.value = !checkedState.value
                onSelectOption(option, checkedState.value)
            }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = option.capitalizeFirstLetter(), style = Typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = { isChecked ->
                    checkedState.value = isChecked
                    onSelectOption(option, isChecked)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}