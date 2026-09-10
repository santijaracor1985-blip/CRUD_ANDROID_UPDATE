package com.sena.crud.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sena.crud.domain.model.ProductModel
import com.sena.crud.ui.component.ProductCard
import com.sena.crud.ui.state.ProductUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(
    uiState: ProductUIState,
    onRetry: () -> Unit,
    onAdd: (ProductModel) -> Unit,
    onUpdate: (ProductModel) -> Unit,
    onDelete: (ProductModel) -> Unit,
    onSelect: (ProductModel?) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<ProductModel?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi tienda", fontWeight = FontWeight.Bold) }) },
        floatingActionButton = { FloatingActionButton(onClick = { onSelect(null); showForm = true }) { Text("+") } }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Catálogo de productos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Administra tu inventario con un CRUD completo.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onSelect(null); showForm = true }, modifier = Modifier.weight(1f)) { Text("Agregar") }
                OutlinedButton(onClick = onRetry, modifier = Modifier.weight(1f)) { Text("Actualizar lista") }
            }
            Spacer(Modifier.height(12.dp))

            if (uiState.isLoading && uiState.products.isEmpty()) {
                CircularProgressIndicator()
            } else if (uiState.errorMessage != null && uiState.products.isEmpty()) {
                Text(uiState.errorMessage)
                Button(onClick = onRetry) { Text("Reintentar") }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 90.dp)) {
                    items(uiState.products, key = { it.id }) { product ->
                        ProductCard(product, onEdit = { onSelect(product); showForm = true }, onDelete = { deleting = product })
                    }
                }
            }
            uiState.successMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp)) }
        }
    }

    if (showForm) {
        ModalBottomSheet(onDismissRequest = { showForm = false; onSelect(null) }, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
            ProductForm(
                product = uiState.selectedProduct,
                saving = uiState.isSaving,
                onCancel = { showForm = false; onSelect(null) },
                onSave = { product -> if (uiState.selectedProduct == null) onAdd(product) else onUpdate(product); showForm = false; onSelect(null) }
            )
        }
    }

    deleting?.let { product ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminar ${product.title}?") },
            confirmButton = { Button(onClick = { onDelete(product); deleting = null }) { Text("Eliminar") } },
            dismissButton = { OutlinedButton(onClick = { deleting = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun ProductForm(product: ProductModel?, saving: Boolean, onCancel: () -> Unit, onSave: (ProductModel) -> Unit) {
    var title by remember(product?.id) { mutableStateOf(product?.title.orEmpty()) }
    var category by remember(product?.id) { mutableStateOf(product?.category.orEmpty()) }
    var description by remember(product?.id) { mutableStateOf(product?.description.orEmpty()) }
    var price by remember(product?.id) { mutableStateOf(product?.price?.toString().orEmpty()) }

    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (product == null) "Nuevo producto" else "Editar producto", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(title, { title = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(category, { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        OutlinedTextField(description, { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(price, { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f), enabled = !saving) { Text("Cancelar") }
            Button(
                onClick = { onSave(ProductModel(product?.id ?: 0, title.trim(), description.trim(), category.trim(), price.toDouble(), product?.imageUrl.orEmpty())) },
                modifier = Modifier.weight(1f), enabled = !saving && title.isNotBlank() && category.isNotBlank() && description.isNotBlank() && price.toDoubleOrNull() != null
            ) { if (saving) CircularProgressIndicator() else Text("Guardar") }
        }
        Spacer(Modifier.height(12.dp))
    }
}
