package br.com.douglasmotta.whitelabeltutorial.domain.usecase

import android.net.Uri

interface UploadProductImageUseCase {
    // insere uma imaegm e retorna o caminho dela
    suspend operator fun invoke(imageUri: Uri):String
}