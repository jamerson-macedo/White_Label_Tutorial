package br.com.douglasmotta.whitelabeltutorial.data

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.BuildConfig
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_PRODUCTS
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_ROOT
import br.com.douglasmotta.whitelabeltutorial.util.STORAGE_IMAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.coroutines.suspendCoroutine

class FirebaseProductDataSource(
    firebaseFirestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
) : ProductDataSource {
    // vejo qual app esta ativo
    // referencia do docmumento
    private val docRef =
        firebaseFirestore.document("$COLLECTION_ROOT/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/")
    private val storageRef = firebaseStorage.reference
    // ao inves de usar callback é so chamar a corrotina para retornar os dados

    override suspend fun getProducts(): List<Product> {
        return suspendCoroutine { continuation ->
            docRef.collection(COLLECTION_PRODUCTS).get().addOnSuccessListener { documents ->
                val products = mutableListOf<Product>()
                for (document in documents) {
                    document.toObject(Product::class.java).run {
                        products.add(this)
                    }
                }
                continuation.resumeWith(Result.success(products))

            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))

            }
        }

    }

    override suspend fun uploadProductImage(img: Uri): String {
        return suspendCoroutine { continuation ->
            val randomKey = UUID.randomUUID()
            // images/car/id
            val childreference =
                storageRef.child("$STORAGE_IMAGES/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/$randomKey")
            childreference.putFile(img).addOnSuccessListener { taskSnapshot ->
                // pego a url da foto
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val patch = uri.toString()
                    continuation.resumeWith(Result.success(patch))
                    // mando a foto
                }

            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))

            }
        }
    }

    override suspend fun createProduct(product: Product): Product {
        return suspendCoroutine { continuation ->
            docRef.collection(COLLECTION_PRODUCTS).document(System.currentTimeMillis().toString())
                .set(product).addOnSuccessListener {
                    continuation.resumeWith(Result.success(product))

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}