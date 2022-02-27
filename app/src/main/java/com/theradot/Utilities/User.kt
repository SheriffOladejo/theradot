package com.theradot.Utilities

data class User(
    val card_number: String,
    val creation_date: String,
    val date_of_birth: String,
    val eye_color: String,
    val firebase_token: String,
    val firstname: String,
    val gender: String,
    val height: String,
    val lastname: String,
    val middlename: String,
    val phone_number: String,
    var profile_image: String
)
