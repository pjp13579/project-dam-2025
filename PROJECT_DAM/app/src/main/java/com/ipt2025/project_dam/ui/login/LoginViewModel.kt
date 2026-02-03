package com.ipt2025.project_dam.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.ipt2025.project_dam.data.LoginRepository
import com.ipt2025.project_dam.data.Result

import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.ui.qrcode.QRCodeView
import kotlinx.coroutines.launch


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    // a boolean flag used to signal the Fragment to navigate.
    // it's nullable so we can set it to false/null after navigation is done.
    val navigateToHome = MutableLiveData<Boolean>()

    // tracks if the username/password formats are valid (e.g., email format, length > 5).
    // used to enable/disable the "Login" button in real-time
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    // tracks the outcome of the API call (Success with user data OR Error with message ID)
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * triggered when the user clicks the Login button.
     * uses a Coroutine to perform the network request off the main thread.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            // blocks here until the Repository returns a result (Success or Error)
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                // valid login
                _loginResult.value =
                    LoginResult(success = QRCodeView())
                navigateToHome.value = true
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    /**
     * listener to input fields, keeps _loginForm state updated with what the user inputs
     * updates UI error messages in real time according to validation
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun doneNavigating() {
        navigateToHome.value = false
    }

    /**
     * A placeholder username validation check
     */
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    /**
     * A placeholder password validation check
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}