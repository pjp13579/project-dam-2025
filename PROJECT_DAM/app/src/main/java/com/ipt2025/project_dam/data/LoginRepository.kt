package com.ipt2025.project_dam.data

import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.UserLoginResponse
import retrofit2.Retrofit

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

/**
 * middleman between the ui and data source, holds the user state
 */
class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: UserLoginResponse? = null
        private set

    // check if we are currently logged in
    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    // tries to login via the datasource
    suspend fun login(username: String, password: String): Result<UserLoginResponse> {
        // // preform login network API request
        val result = dataSource.login(username, password)

        // if it worked, save the user to our local cache
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    // saves user info to the provider so other calls can use the token
    private fun setLoggedInUser(loggedInUser: UserLoginResponse) {
        this.user = loggedInUser
        RetrofitProvider.setLoggedInUser(loggedInUser);
    }
}