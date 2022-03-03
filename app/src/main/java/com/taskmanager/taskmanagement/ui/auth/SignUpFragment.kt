package com.taskmanager.taskmanagement.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentSignUpBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.signupFormState.observe(viewLifecycleOwner){
            val signUpFormState = it ?: return@observe

            if (signUpFormState.nameError != null){
                binding.etName.error = getString(signUpFormState.nameError)
            }

            if (signUpFormState.usernameError != null){
                binding.etUsername.error = getString(signUpFormState.usernameError)
            }

            if (signUpFormState.emailError != null){
                binding.etEmail.error = getString(signUpFormState.emailError)
            }

            if (signUpFormState.passwordLengthError != null){
                binding.etPassword.error = getString(signUpFormState.passwordLengthError)
            }

            if (signUpFormState.passwordMatchError != null){
                binding.etConfirmPassword.error = getString(signUpFormState.passwordMatchError)
            }
        }

        binding.btnSignUp.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val name = binding.etName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if(viewModel.validateSignUpForm(name, username, email, password, confirmPassword)){
                signUp(name, username, email, password)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    }
    private fun signUp(
        name: String,
        username: String,
        email: String,
        password: String
    ){
        viewModel.signUp(
            name, username, email, password
        ).observe(viewLifecycleOwner){
            binding.progressBar.visibility = View.GONE
            val result = it ?: return@observe
            when (result){
                is NetworkResult.Success -> {
                    navigateToDashboard()
                }
                is NetworkResult.GenericError -> {
                    showSnackbar(result.message!!)
                }
                is NetworkResult.NetworkError -> {
                    showSnackbar(getString(R.string.connection_error))
                }
            }
        }
    }

    private fun navigateToDashboard(){
        (requireActivity() as AuthActivity).navigateToMainActivity()
    }

    // TODO ---> create view extensions for toast and snackbars
    fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}