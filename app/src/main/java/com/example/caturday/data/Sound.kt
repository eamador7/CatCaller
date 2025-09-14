package com.example.caturday.data

enum class Sound(val displayName: String, val fileName: String?) {
    MEOW_1("Meow", "meow_1"),
    PURR_LONG("Purr", "purr_long"),
    CAT_CALLING("Cat Calling", "cat_calling"),
    KITTENS("Kittens", "kittens"),
    RANDOM("Random", null)
}
