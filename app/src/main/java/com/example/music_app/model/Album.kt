package com.example.music_app.model

// ARCHIVO PARA OBTENER LA INFORMACIÓN QUE REGRESA EL API

data class Album(

    val wrapperType: String?,
    val collectionType: String?,

    val artistId: Long?,
    val collectionId: Long?,

    val artistName: String?,
    val collectionName: String?,

    val artistViewUrl: String?,
    val collectionViewUrl: String?,

    val artworkUrl100: String?,

    val collectionPrice: Double?,
    val trackCount: Int?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val currency: String?
){

    // MÉTODO PARA OBTENER LA IMAGEN MÁS GRANDE
    fun obtenerImagenGrande() : String{
        return artworkUrl100?.replace("100x100bb.jpg", "600x600bb.jpg") ?:""

    }




}

