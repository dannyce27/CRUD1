package modelo

import android.service.credentials.BeginGetCredentialOption

data class dataClassMascotas(
    val uuid: String,
    val nombreMascota: String,
    var peso: Int,
    var edad: Int
)
