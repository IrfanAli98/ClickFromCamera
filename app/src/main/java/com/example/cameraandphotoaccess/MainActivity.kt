package com.example.cameraandphotoaccess

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.cameraandphotoaccess.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding
    private var requestPermissionList = arrayOf(android.Manifest.permission.CAMERA)
    private val CAMERA_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        dataBinding.btnSubmit.setOnClickListener {
            when (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
                PackageManager.PERMISSION_GRANTED -> {
                    //Todo: if permission granted then perform the action
                    actionCamera()
                }
                else -> {
                    //Todo: if permission is denied then Request for Permission
                    requestForPermission()
                }
            }
        }
    }

    //TODO; this is helping to manger the response after the permission granted or denied
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            actionCamera()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
        }

    }

    private fun actionCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }

    private fun requestForPermission() {
        ActivityCompat.requestPermissions(this, requestPermissionList, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE && data != null) {
            dataBinding.ivPhoto.setImageBitmap(data.extras!!.get("data") as Bitmap)
        }
    }
}