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

/**
 * preforms login
 * first fragment to be displayed in the activityMain fragment holder
 */
class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager
    private var _binding: FragmentLoginBinding? = null

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

        // initialize ViewModel
        // manages login UI state (loader, block/unblock login button and data value validation)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        /**
         * AUTO-LOGIN LOGIC:
         * 1. check if a token exists in SharedPreferences (disk).
         * 2. if yes, try to validate it with the API.
         * 3. if valid, skip the login screen and go to Dashboard.
         */
        lifecycleScope.launch {
            try {
                val token = tokenManager.loadToken() ?: ""
                if (token.isNotEmpty()) {
                    RetrofitProvider.updateToken(token)
                    val apiService = RetrofitProvider.create(LoginAPIService::class.java)
                    val userData = apiService.loginToken()
                    // save the user data with the token
                    RetrofitProvider.setLoggedInUser(UserLoginResponse(token = token, user = userData))
                    loginViewModel.navigateToHome.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // token is invalid, clear it
                tokenManager.clearToken()
                RetrofitProvider.clearToken()
            }
        }

        // OBSERVER: watch for the navigation signal
        loginViewModel.navigateToHome.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                // navigate to the Dashboard fragment )
                findNavController().navigate(R.id.action_loginFragment_to_dashboard)

                usernameEditText.setText("")
                passwordEditText.setText("")

                // reset flag so rotating the screen doesn't trigger navigation again
                loginViewModel.doneNavigating()
            }
        })

        // OBSERVER: watch for form validation errors
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                // enable/disable button based on input values
                loginButton.isEnabled = loginFormState.isDataValid

                // show errors on the text fields (if any)
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        // OBSERVER: watch for the final API result
        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->

                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let { // handle error
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    val token = RetrofitProvider.getToken()
                    if(token != null)
                        tokenManager.saveToken(token);
                    updateUIWithUser(it)
                }
            })

        // TEXT WATCHER: listen for typing to trigger LoginViewModel validation
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        // allow pressing "Enter" on keyboard to submit
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                usernameEditText.setText("")
                passwordEditText.setText("")
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