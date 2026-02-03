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

/**
 *  fragment that handles the camera to scan device qr codes and process result
 */
class QRCodeView : Fragment() {

    private lateinit var messageText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qrcode, container, false)

        messageText = view.findViewById(R.id.messageText)

        // show a status update while zxing loads things up
        messageText.text = "Preparing camera"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // start camera
        startCamera()
    }

    override fun onResume() {
        super.onResume()
        // // if we came back from settings or a dialog, try to start the camera again
        if (!isCameraPermissionGranted()) {
            startCamera()
        }
    }

    /**
     * checks permissions before actually launching the scanner
     */
    private fun startCamera() {
        checkCameraPermission()
    }

    /**
     * configures the zxing scanner options
     */
    private fun startQRCodeScan() {

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

    /**
     * handles what happens after the camera closes
     * validates the read object
     * validates if the read object is a valid mongo object id
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // null reading. go back to previous fragment. exit camera fragment
                findNavController().navigateUp()
            } else {
                val scannedId = result.contents.trim()
                messageText.text = "Validating code..."

                // validate the scanned string format
                if (isValidMongoObjectId(scannedId)) {
                    // check if device exists in backend
                    // if exists navigate to device details fragment
                    // if not exists, display invalid reading and camera will reopen shortly (or it should at least lol)
                    checkDeviceExists(scannedId)
                } else {
                    messageText.text = "Invalid QR code format"
                    Toast.makeText(
                        requireContext(),
                        "Invalid MongoDB ObjectID format. Please scan a valid device QR code.",
                        Toast.LENGTH_LONG
                    ).show()

                    // invalid reading. wait a 2 seconds and reopen camera for new reading
                    view?.postDelayed({
                        startQRCodeScan()
                    }, 2000)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * validates if string if a valid mongo object id
     */
    private fun isValidMongoObjectId(id: String): Boolean {
        // MongoDB ObjectID is 24 hex characters
        val mongoIdPattern = Pattern.compile("^[0-9a-fA-F]{24}$")
        return mongoIdPattern.matcher(id).matches()
    }

    /**
     * verifies if mongo id matches a existing device
     * if not valid, don't navigate user to device details fragment
     */
    private fun checkDeviceExists(deviceId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                messageText.text = "Validating device..."

                val exists = withContext(Dispatchers.IO) {
                    try {
                        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                        // getDeviceDetails() will throw an exception if device doesn't exist (404)
                        apiService.getDeviceDetails(deviceId)
                        true
                    } catch (e: Exception) {
                        false
                    }
                }


                if (exists) {
                    // Device exists, navigate to device details fragment
                    val bundle = Bundle().apply {
                        putString("_id", deviceId)
                    }
                    findNavController().navigate(
                        R.id.action_QRCodeView_to_deviceDetailsFragment,
                        bundle
                    )
                } else {
                    // id format was right, but device isn't in db
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
                // general failure. something somewhere broke
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

    /**
     * open camera if app has been granted camera access permission
     */
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

    /**
     * verify if app has access grant to camera
     */
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}