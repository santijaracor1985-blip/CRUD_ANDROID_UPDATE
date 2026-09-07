package com.sena.crud.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sena.crud.domain.model.ProductModel
import com.sena.crud.ui.component.productCard
import com.sena.crud.ui.state.ProductUIState

@Composable
fun ProductDetails(
    uiState: ProductUIState,
    onRetry: () -> Unit,
    onUpdate: (ProductModel) -> Unit
) {
    when {
        uiState.isLoading -> {
            CircularProgressIndicator()
        }

        uiState.errorMessage != null && uiState.product == null -> {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = uiState.errorMessage)
                Button(onClick = onRetry) {
                    Text(text = "Reintentar")
                }
            }
        }

        uiState.product != null -> {
            ProductEditForm(
                product = uiState.product,
                isUpdating = uiState.isUpdating,
                errorMessage = uiState.errorMessage,
                successMessage = uiState.successMessage,
                onUpdate = onUpdate
            )
        }
    }
}

@Composable
private fun ProductEditForm(
    product: ProductModel,
    isUpdating: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onUpdate: (ProductModel) -> Unit
) {
    var title by remember(product.id, product.title) { mutableStateOf(product.title) }
    var description by remember(product.id, product.description) { mutableStateOf(product.description) }
    var category by remember(product.id, product.category) { mutableStateOf(product.category) }
    var price by remember(product.id, product.price) { mutableStateOf(product.price.toString()) }
    var editing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!editing) {
            productCard(product = product)

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { editing = true }
            ) {
                Text("Editar producto")
            }
        } else {
            Text("Editar producto")

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                singleLine = true
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdating &&
                    title.isNotBlank() &&
                    category.isNotBlank() &&
                    description.isNotBlank() &&
                    price.toDoubleOrNull() != null,
                onClick = {
                    onUpdate(
                        product.copy(
                            title = title.trim(),
                            description = description.trim(),
                            category = category.trim(),
                            price = price.toDouble()
                        )
                    )
                    editing = false
                }
            ) {
                if (isUpdating) {
                    CircularProgressIndicator()
                } else {
                    Text("Guardar cambios")
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdating,
                onClick = { editing = false }
            ) {
                Text("Cancelar")
            }
        }

        errorMessage?.let {
            Text(text = it)
        }

        successMessage?.let {
            Text(text = it)
        }
    }
}
