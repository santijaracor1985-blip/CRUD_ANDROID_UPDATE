package com.sena.crud.data.repository

import com.sena.crud.data.mapper.toDomain
import com.sena.crud.data.remote.api.ProductApiService
import com.sena.crud.data.remote.dto.req.product.UpdateProductRequest
import com.sena.crud.domain.model.ProductModel
import com.sena.crud.domain.repository.ProductRepository
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService
) : ProductRepository {
    override suspend fun getProducts(): List<ProductModel> =
        api.getProducts().products.map { it.toDomain() }

    override suspend fun getProductById(id: Int): ProductModel =
        api.getProductById(id).toDomain()

    override suspend fun createProduct(product: ProductModel): ProductModel =
        api.createProduct(product.toRequest()).toDomain()

    override suspend fun updateProduct(product: ProductModel): ProductModel =
        api.updateProduct(product.id, product.toRequest()).toDomain()

    override suspend fun deleteProduct(id: Int): Boolean {
        api.deleteProduct(id)
        return true
    }

    private fun ProductModel.toRequest() = UpdateProductRequest(
        title = title,
        description = description,
        category = category,
        price = price
    )
}
