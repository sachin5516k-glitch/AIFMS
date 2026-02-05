package com.ai.franchise.ui.sales

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ai.franchise.R
import com.ai.franchise.data.repository.SalesRepository
import java.io.File
import java.io.FileOutputStream

class SalesFragment : Fragment() {

    private lateinit var viewModel: SalesViewModel
    private var proofFile: File? = null
    private lateinit var ivProofPreview: ImageView

    // Dummy location for now, would use LocationManager in real implementation
    private val currentLat = 28.7041
    private val currentLng = 77.1025

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            ivProofPreview.setImageBitmap(imageBitmap)
            saveBitmapToFile(imageBitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sales, container, false)

        val repository = SalesRepository()
        // Factory logic omitted for brevity in Phase 2
        viewModel = SalesViewModel(repository) 

        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val btnCapture = view.findViewById<Button>(R.id.btnCaptureProof)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmitSales)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        ivProofPreview = view.findViewById(R.id.ivProofPreview)

        btnCapture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        btnSubmit.setOnClickListener {
            val amount = etAmount.text.toString()
            if (amount.isNotEmpty()) {
                viewModel.submitSales(amount, currentLat, currentLng, proofFile)
            } else {
                Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            btnSubmit.isEnabled = !it
        }

        viewModel.submissionResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                etAmount.text.clear()
                ivProofPreview.setImageResource(0)
                proofFile = null
            }.onFailure {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(takePictureIntent)
    }

    private fun saveBitmapToFile(bitmap: Bitmap) {
        val filesDir = requireContext().cacheDir
        val imageFile = File(filesDir, "proof_${System.currentTimeMillis()}.jpg")
        val os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
        proofFile = imageFile
    }
}
