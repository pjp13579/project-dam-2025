package com.ipt2025.project_dam.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.databinding.FragmentLoginBinding

import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.LoginDataSource
import com.ipt2025.project_dam.data.TokenManager
import com.ipt2025.project_dam.data.api.LoginAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.data.api.UserLoginResponse
import com.ipt2025.project_dam.data.api.UserLoginTokenRequest
import com.ipt2025.project_dam.ui.qrcode.QRCodeView
import com.ipt2025.project_dam.ui.site.SiteRecyclerViewAdapter
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())


        val apiService = RetrofitProvider.create(LoginAPIService::class.java)

        lifecycleScope.launch {
            try{
                val token = tokenManager.loadToken() ?: ""
                RetrofitProvider.updateToken(token)
                val response = apiService.loginToken()
                RetrofitProvider.setLoggedInUser(UserLoginResponse(token = token, response))
                loginViewModel.navigateToHome.value = true;
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        // Assume you have initialized your ViewModel, e.g., using by viewModels()
        // loginViewModel = ...

        // Observe the navigation signal from the ViewModel
        loginViewModel.navigateToHome.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                // Navigate to the next fragment (e.g., HomeFragment)
                findNavController().navigate(R.id.action_loginFragment_to_dashboard)

                // IMPORTANT: Reset the event flag in the ViewModel to prevent
                // navigation from re-triggering (e.g., on config change/rotation)
                loginViewModel.doneNavigating()
            }
        })
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->

                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    val token = RetrofitProvider.getToken()
                    if(token != null)
                        tokenManager.saveToken(token);
                    updateUIWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun updateUIWithUser(model: QRCodeView) {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}