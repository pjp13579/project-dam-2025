package com.ipt2025.project_dam.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ipt2025.project_dam.R
import com.google.zxing.integration.android.IntentIntegrator

/**
 * User details post authentication that is exposed to the UI
 */
class LoggedInUserView : Fragment() {

    private lateinit var text: TextView
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        text = view.findViewById(R.id.text)
        button = view.findViewById(R.id.button)

        // Check if button is found
        if (button == null) {
            Toast.makeText(requireContext(), "button not found", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "button found", Toast.LENGTH_SHORT).show()
        }

        // FIX: Call permission check instead of direct scan
        button.setOnClickListener {
            Toast.makeText(requireContext(), "QRCode Button clicked", Toast.LENGTH_SHORT).show()
            checkCameraPermission()
        }

        return view
    }

    private fun startQRCodeScan() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                text.text = "Cancelled"
            } else {
                text.text = "${result.contents}"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startQRCodeScan()
        }
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
                Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}