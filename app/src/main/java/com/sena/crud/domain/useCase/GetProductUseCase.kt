package com.sena.crud.domain.useCase

import com.sena.crud.domain.model.ProductModel
import com.sena.crud.domain.repository.ProductRepository
import jakarta.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<ProductModel> = repository.getProducts()
    suspend fun byId(id: Int): ProductModel = repository.getProductById(id)
}
