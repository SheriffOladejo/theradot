package com.theradot.Models

data class Exercise (
    val image_thumbnail: String,
    val exercise_category: String,
    val exercise_title: String,
    val video_url: String,
    val objectives: String,
    val exercise_description: String,
    val completed_reps: String,
    val creation_date: String,
    val exercise_id: String,
    val number_of_reps: String
)