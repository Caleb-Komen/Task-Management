package com.taskmanager.taskmanagement.ui.auth

data class SignUpFormState(
    val nameError: Int? = null,
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val passwordLengthError: Int? = null,
    val passwordMatchError: Int? = null,
    val isDataValid: Boolean = false
)
