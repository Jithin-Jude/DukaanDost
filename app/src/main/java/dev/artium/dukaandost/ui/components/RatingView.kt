package dev.artium.dukaandost.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gowtham.ratingbar.RatingBar
import dev.artium.dukaandost.model.ProductModel
import dev.artium.dukaandost.ui.theme.DividerGrey
import dev.artium.dukaandost.ui.theme.PureBlack
import dev.artium.dukaandost.ui.theme.Typography

@Composable
fun RatingView(selectedProduct: ProductModel?, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = selectedProduct?.rating?.rate.toString(),
                style = Typography.bodyMedium,
                color = PureBlack
            )
            Spacer(modifier = Modifier.width(2.dp))
            RatingBar(
                value = selectedProduct?.rating?.rate?.toFloat() ?: 0f,
                onValueChange = {},
                onRatingChanged = {}
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "(${selectedProduct?.rating?.count?.toInt()})",
                style = Typography.bodyMedium,
                color = DividerGrey
            )
        }
    }
}