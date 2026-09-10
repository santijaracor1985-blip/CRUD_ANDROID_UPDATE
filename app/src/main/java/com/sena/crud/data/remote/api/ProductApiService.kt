package com.sena.crud.data.remote.api

import com.sena.crud.data.remote.dto.req.product.Product
import com.sena.crud.data.remote.dto.req.product.UpdateProductRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @POST("products/add")
    suspend fun createProduct(@Body product: UpdateProductRequest): Product

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: UpdateProductRequest
    ): Product

    @PATCH("products/{id}")
    suspend fun patchProduct(
        @Path("id") id: Int,
        @Body product: UpdateProductRequest
    ): Product

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Product
}

data class ProductListResponse(
    val products: List<Product> = emptyList()
)
