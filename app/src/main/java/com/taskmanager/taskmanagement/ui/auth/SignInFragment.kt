package com.taskmanager.taskmanagement.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        binding = FragmentSignInBinding.bind(view)
        binding.tvSignUp.setOnClickListener {
            navigateToSignUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.signInFormState.observe(viewLifecycleOwner){
            val signInFormState = it ?: return@observe
            if (signInFormState.emailError != null){
                showSnackbar(getString(signInFormState.emailError))
            }
            if (signInFormState.passwordError != null){
                showSnackbar(getString(R.string.empty_field_error))
            }
        }

        binding.btnSignIn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (viewModel.validateSignInForm(email, password)){
                signIn(email, password)
            } else{
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel.signIn(email, password).observe(viewLifecycleOwner){
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

    private fun navigateToSignUp() {
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    private fun navigateToDashboard(){
        (requireActivity() as AuthActivity).navigateToMainActivity()
    }

    // TODO ---> create view extensions for toast and snackbars
    fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}