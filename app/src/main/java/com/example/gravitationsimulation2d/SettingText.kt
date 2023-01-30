package com.example.gravitationsimulation2d

class SettingText(
    val firstTextId: Int,
    val secondTextId: Int,
    var userInput: String,
    val textFiledHint: String,
    var prevInput: String? = null
) {
    fun setInput(input: String): Boolean {
        if (input.toDoubleOrNull() != null) {
            userInput = input
            return true
        }
        return false
    }
}
