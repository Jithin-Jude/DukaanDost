package dev.artium.dukaandost.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.artium.dukaandost.DukkanDostUtils.capitalizeFirstLetter
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.Typography

@Composable
fun CategoryLabelView(product: ProductModel?) {
    Box(
        modifier = Modifier.border(
            border = BorderStroke(1.dp, DividerGrey),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Text(
            text = product?.category?.capitalizeFirstLetter().toString(),
            style = Typography.labelSmall,
            color = DividerGrey,
            modifier = Modifier.padding(4.dp)
        )
    }
}