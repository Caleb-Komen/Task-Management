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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.taskmanager.taskmanagement.R
import com.taskmanager.taskmanagement.data.remote.NetworkResult
import com.taskmanager.taskmanagement.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    private var _binding: FragmentSignUpBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.signupFormState.observe(viewLifecycleOwner){
            val signUpFormState = it ?: return@observe
            binding.btnSignUp.isEnabled = signUpFormState.isDataValid
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

        binding.etName.afterTextChanged {
            viewModel.signUpDataChange(
                binding.etName.text.toString(), binding.etUsername.text.toString(),
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.toString()
            )
        }
        binding.etUsername.afterTextChanged {
            viewModel.signUpDataChange(
                binding.etName.text.toString(), binding.etUsername.text.toString(),
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.toString()
            )
        }
        binding.etEmail.afterTextChanged {
            viewModel.signUpDataChange(
                binding.etName.text.toString(), binding.etUsername.text.toString(),
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.toString()
            )
        }
        binding.etPassword.afterTextChanged {
            viewModel.signUpDataChange(
                binding.etName.text.toString(), binding.etUsername.text.toString(),
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.toString()
            )
        }
        binding.etConfirmPassword.apply {
            afterTextChanged {
                viewModel.signUpDataChange(
                    binding.etName.text.toString(), binding.etUsername.text.toString(),
                    binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId){
                    EditorInfo.IME_ACTION_DONE -> {
                        binding.progressBar.visibility = View.VISIBLE
                        viewModel.signUp(
                            binding.etName.text.toString(), binding.etUsername.text.toString(),
                            binding.etEmail.text.toString(), binding.etPassword.text.toString()
                        ).observe(viewLifecycleOwner){
                            binding.progressBar.visibility = View.GONE
                            val result = it ?: return@observe
                            when (result){
                                is NetworkResult.Success -> {
                                    navigateToDashboard(result.data.name)
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
                }
                false
            }

            binding.btnSignUp.setOnClickListener {
//                binding.progressBar.visibility = View.VISIBLE
                viewModel.signUp(
                    binding.etName.text.toString(), binding.etUsername.text.toString(),
                    binding.etEmail.text.toString(), binding.etPassword.text.toString()
                ).observe(viewLifecycleOwner){
                    binding.progressBar.visibility = View.GONE
                    val result = it ?: return@observe
                    when (result){
                        is NetworkResult.Success -> {
                            navigateToDashboard(result.data.name)
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
        }
    }

    private fun navigateToDashboard(name: String?){
        val action = SignUpFragmentDirections.actionSignUpFragmentToDashboardFragment(name)
        findNavController().navigate(action)
    }

    // TODO ---> create view extensions for toast and snackbars
    fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit){
        this.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}