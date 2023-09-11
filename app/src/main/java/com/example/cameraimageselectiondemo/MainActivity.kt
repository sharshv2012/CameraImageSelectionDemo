package com.example.cameraimageselectiondemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraimageselectiondemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnCamera?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent , CAMERA_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(this ,
                    arrayOf(Manifest.permission.CAMERA) ,
                    CAMERA_PERMISSION_CODE )
            }
        }




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)  // captures image
                startActivityForResult(intent , CAMERA_REQUEST_CODE)

            }else{
                Toast.makeText(
                    this ,
                    "Permission Denied" ,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST_CODE){
                val thumbNail : Bitmap = data!!.extras!!.get("data")as Bitmap
                binding?.ivImage!!.setImageBitmap(thumbNail)
            }
        }
    }
}