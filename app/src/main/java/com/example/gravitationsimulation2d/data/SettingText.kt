package com.example.gravitationsimulation2d.data

class SettingText(
    val firstTextId: Int,
    val secondTextId: Int,
    var userInput: String,
    val textFiledHint: String,
    var prevInput: String? = null
) {
    // If the user input is a valid double, then it is set to "userInput" field
    fun setInput(input: String): Boolean {
        if (input.toDoubleOrNull() != null) {
            userInput = input
            return true
        }
        return false
    }
}
