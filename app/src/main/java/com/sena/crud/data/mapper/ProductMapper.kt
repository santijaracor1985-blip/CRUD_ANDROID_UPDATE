package com.sena.crud.data.mapper

import com.sena.crud.data.remote.dto.req.product.Product
import com.sena.crud.domain.model.ProductModel

fun Product.toDomain(): ProductModel = ProductModel(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    imageUrl = thumbnail.ifBlank { images.firstOrNull().orEmpty() }
)
