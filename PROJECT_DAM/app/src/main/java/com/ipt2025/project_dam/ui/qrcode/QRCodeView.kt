package com.ipt2025.project_dam.ui.qrcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class QRCodeView : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var messageText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qrcode, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        messageText = view.findViewById(R.id.messageText)

        // Show initial message
        messageText.text = "Preparing camera..."

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start camera immediately when fragment is ready
        startCamera()
    }

    override fun onResume() {
        super.onResume()
        // If we come back to this fragment (e.g., from denied permission dialog),
        // try to start camera again
        if (!isCameraPermissionGranted()) {
            startCamera()
        }
    }

    private fun startCamera() {
        checkCameraPermission()
    }

    private fun startQRCodeScan() {
        // Hide progress bar, show scanning message
        progressBar.visibility = View.GONE
        messageText.text = "Point camera at QR code"

        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // User cancelled scanning, navigate back
                findNavController().navigateUp()
            } else {
                val scannedId = result.contents.trim()
                messageText.text = "Validating code..."
                progressBar.visibility = View.VISIBLE

                // Validate the scanned ID
                if (isValidMongoObjectId(scannedId)) {
                    // Check if device exists in backend
                    checkDeviceExists(scannedId)
                } else {
                    progressBar.visibility = View.GONE
                    messageText.text = "Invalid QR code format"
                    Toast.makeText(
                        requireContext(),
                        "Invalid MongoDB ObjectID format. Please scan a valid device QR code.",
                        Toast.LENGTH_LONG
                    ).show()

                    // Wait a moment and restart camera
                    view?.postDelayed({
                        startQRCodeScan()
                    }, 2000)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isValidMongoObjectId(id: String): Boolean {
        // MongoDB ObjectID is 24 hex characters
        val mongoIdPattern = Pattern.compile("^[0-9a-fA-F]{24}$")
        return mongoIdPattern.matcher(id).matches()
    }

    private fun checkDeviceExists(deviceId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                messageText.text = "Validating device..."

                val exists = withContext(Dispatchers.IO) {
                    try {
                        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                        // This will throw an exception if device doesn't exist (404)
                        apiService.getDeviceDetails(deviceId)
                        true
                    } catch (e: Exception) {
                        false
                    }
                }

                progressBar.visibility = View.GONE

                if (exists) {
                    // Navigate to device details fragment
                    val bundle = Bundle().apply {
                        putString("_id", deviceId)
                    }
                    findNavController().navigate(
                        R.id.action_QRCodeView_to_deviceDetailsFragment,
                        bundle
                    )
                } else {
                    messageText.text = "Device not found"
                    Toast.makeText(
                        requireContext(),
                        "Device with ID $deviceId not found in database",
                        Toast.LENGTH_LONG
                    ).show()

                    // Wait a moment and restart camera
                    view?.postDelayed({
                        startQRCodeScan()
                    }, 2000)
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                messageText.text = "Connection error"
                Toast.makeText(
                    requireContext(),
                    "Error checking device: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

                // Wait a moment and restart camera
                view?.postDelayed({
                    startQRCodeScan()
                }, 2000)
            }
        }
    }

    private fun checkCameraPermission() {
        if (isCameraPermissionGranted()) {
            startQRCodeScan()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRCodeScan()
            } else {
                // Permission denied
                progressBar.visibility = View.GONE
                messageText.text = "Camera permission required"

                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to scan QR codes. Please enable it in settings.",
                    Toast.LENGTH_LONG
                ).show()

                // Optionally, you could show a button to open app settings
                // or navigate back after a delay
                view?.postDelayed({
                    findNavController().navigateUp()
                }, 3000)
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}