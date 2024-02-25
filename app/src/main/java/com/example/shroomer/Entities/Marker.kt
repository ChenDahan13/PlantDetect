package com.example.shroomer.Entities

class MarkerClass (
    private var marker_id: String,
    private var title: String,
    private var latitude: Double,
    private var longitude: Double,
    private var user_id: String,
) {
    open fun toMap(): Map<String, Any> {
        return mapOf(
            "marker_id" to this.marker_id,
            "title" to this.title,
            "latitude" to this.latitude,
            "longitude" to this.longitude,
            "user_id" to this.user_id
        )
    }
    fun getMarkerID(): String { return this.marker_id }
    fun getTitle(): String {return this.title}
    fun getLatitude(): Double {return this.latitude}
    fun getLongitude(): Double {return this.longitude}
    fun getUserID(): String{return this.user_id}
}