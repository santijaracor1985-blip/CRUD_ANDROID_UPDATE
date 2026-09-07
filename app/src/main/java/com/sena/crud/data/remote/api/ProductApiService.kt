package com.sena.crud.data.remote.api

import com.sena.crud.data.remote.dto.req.product.Product
import com.sena.crud.data.remote.dto.req.product.UpdateProductRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProductApiService {
    @GET("products/{id}")
    suspend fun GetProductByid(
        @Path("id") id: Int
    ): Product

    @PATCH("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: UpdateProductRequest
    ): Product
}
