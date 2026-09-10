package com.sena.crud.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sena.crud.ui.section.ProductDetails
import com.sena.crud.ui.viewModel.ProductViewModel

@Composable
fun ProductScreen(viewModel: ProductViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    ProductDetails(
        uiState = state,
        onRetry = viewModel::loadProducts,
        onAdd = viewModel::createProduct,
        onUpdate = viewModel::updateProduct,
        onDelete = viewModel::deleteProduct,
        onSelect = viewModel::selectProduct
    )
}
