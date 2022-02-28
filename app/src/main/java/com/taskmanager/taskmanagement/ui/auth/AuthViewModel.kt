package com.taskmanager.taskmanagement.ui.auth

import android.text.TextUtils
import androidx.core.text.TextUtilsCompat
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.domain.model.User
import com.taskmanager.taskmanagement.domain.usecases.SignInUseCase
import com.taskmanager.taskmanagement.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _signupFormState = MutableLiveData<SignUpFormState>()
    val signupFormState: LiveData<SignUpFormState> get() = _signupFormState

    private val _signInFormState = MutableLiveData<SignInFormState>()
    val signInFormState: LiveData<SignInFormState> get() = _signInFormState

    fun signUp(
        name: String,
        username: String,
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>{
        return signUpUseCase(name, username, email, password)
    }

    fun signIn(
        email: String,
        password: String
    ): LiveData<NetworkResult<User>>{
        return signInUseCase(email, password)
    }

    fun signUpDataChange(
        name: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ){
        if (!isNameValid(name)){
            _signupFormState.value = SignUpFormState(nameError = R.string.empty_field_error)
        } else if (!isUsernameValid(username)){
            _signupFormState.value = SignUpFormState(usernameError = R.string.empty_field_error)
        } else if (isEmailValid(email)){
            _signupFormState.value = SignUpFormState(emailError = R.string.email_error)
        } else if (isPasswordLengthValid(password)){
            _signupFormState.value = SignUpFormState(passwordLengthError = R.string.password_length_error)
        } else if (isPasswordValid(password, confirmPassword)){
            _signupFormState.value = SignUpFormState(passwordMatchError = R.string.password_error)
        } else{
            _signupFormState.value = SignUpFormState(isDataValid = true)
        }
    }

    fun validateSignInForm(
        email: String,
        password: String
    ): Boolean{
        return when {
            TextUtils.isEmpty(email) -> {
                _signInFormState.value = SignInFormState(emailError = R.string.empty_field_error)
                false
            }
            TextUtils.isEmpty(password) -> {
                _signInFormState.value = SignInFormState(passwordError = R.string.empty_field_error)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun isNameValid(name: String): Boolean{
        return name.isNotBlank()
    }

    private fun isUsernameValid(username: String): Boolean{
        return username.isNotBlank()
    }

    private fun isEmailValid(email: String): Boolean{
        return if (email.contains("@")){
            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        } else{
            email.isNotBlank()
        }
    }

    private fun isPasswordLengthValid(password: String): Boolean{
        return password.length > 5
    }

    private fun isPasswordValid(password: String, confirmPassword: String): Boolean{
        return password == confirmPassword
    }
}