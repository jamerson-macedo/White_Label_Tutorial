package br.com.douglasmotta.whitelabeltutorial.data

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product

class ProductRepository(private val dataSource: ProductDataSource) {
    suspend fun getProducts():List<Product> = dataSource.getProducts()
    suspend fun uploadProductImage(img: Uri):String = dataSource.uploadProductImage(img)
    suspend fun createProduct(product: Product): Product = dataSource.createProduct(product)
}