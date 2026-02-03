package com.ipt2025.project_dam.ui.login

import com.ipt2025.project_dam.ui.qrcode.QRCodeView

/**
 * quthentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: QRCodeView? = null,
    val error: Int? = null
)