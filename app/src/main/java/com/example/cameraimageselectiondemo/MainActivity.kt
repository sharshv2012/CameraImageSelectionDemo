package com.example.cameraimageselectiondemo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraimageselectiondemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private lateinit var imageCaptureLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)






        binding?.btnCamera?.setOnClickListener {
            binding?.btnCamera?.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    imageCaptureLauncher.launch(intent)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }


        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                imageCaptureLauncher.launch(intent)
            } else {
                showRationalDialogForPermissions()

            }
        }
        imageCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val thumbNail: Bitmap = result.data?.extras?.get("data")as Bitmap
                binding?.ivImage?.setImageBitmap(thumbNail)
            }
        }




    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("" +
                "It looks like you have turned off permissions required "
                + "for this feature. It can be enabled under the " +
                "Applications Settings")
            .setPositiveButton("GO TO SETTINGS")
            {_ , _ ->  // _ , _ is used because the parameter variables aren't used below.
                try { // in these cases always use try catch blocks.
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package" ,packageName , null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e : ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("Cancel"){dialog , which -> // here which can be replaced by an _ as it isn't used.
                dialog.dismiss()
            }
            .setCancelable(false) // will not allow user to cancel by clicking on the remaining area
            .show()
    }




}