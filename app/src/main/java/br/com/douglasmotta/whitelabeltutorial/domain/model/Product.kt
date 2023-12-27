package br.com.douglasmotta.whitelabeltutorial.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val description: String = "",
    val price: Double = 0.0,
    @get:PropertyName("img_url")
    @set:PropertyName("img_url")
    var imageUrl: String = ""

):Parcelable
