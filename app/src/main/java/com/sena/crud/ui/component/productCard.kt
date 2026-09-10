package com.sena.crud.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sena.crud.domain.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun ProductCard(product: ProductModel, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            RemoteProductImage(product.imageUrl, Modifier.size(105.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(product.category.uppercase(), style = MaterialTheme.typography.labelSmall)
                Text(product.title, style = MaterialTheme.typography.titleMedium)
                Text("$ ${"%.2f".format(product.price)}", style = MaterialTheme.typography.titleMedium)
                Text("ID #${product.id}", style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    androidx.compose.material3.OutlinedButton(onClick = onEdit) { Text("Editar") }
                    androidx.compose.material3.OutlinedButton(onClick = onDelete) { Text("Eliminar") }
                }
            }
        }
    }
}

@Composable
fun RemoteProductImage(url: String, modifier: Modifier = Modifier) {
    var bitmap by remember(url) { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(url) {
        if (url.isNotBlank()) bitmap = withContext(Dispatchers.IO) { runCatching { URL(url).openStream().use(BitmapFactory::decodeStream) }.getOrNull() }
    }
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
        bitmap?.let { Image(it.asImageBitmap(), null, modifier = Modifier.size(100.dp), contentScale = ContentScale.Crop) }
            ?: Text("🛍️", style = MaterialTheme.typography.headlineMedium)
    }
}
