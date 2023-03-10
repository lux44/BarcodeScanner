package com.lux.zena.barcodescanner.ui.activity

import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.lux.zena.barcodescanner.R
import com.lux.zena.barcodescanner.databinding.ActivityBarcodeScanBinding
import java.util.*

class BarcodeScanActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {
    lateinit var binding:ActivityBarcodeScanBinding
    lateinit var capture:CaptureManager
    lateinit var sp:SharedPreferences
    lateinit var flashStatus:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_barcode_scan)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_barcode_scan)
        binding.scan = this

        sp = getSharedPreferences(packageName, MODE_PRIVATE)
        binding.zxingBarcodeScanner.setTorchListener(this)
        if (!hasFlash()){
            binding.btnFlash.visibility=View.GONE
        }
        capture = CaptureManager(this, binding.zxingBarcodeScanner)
        capture.initializeFromIntent(intent,savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(true)
        capture.decode()

        changeMaskColor(null)
        changeLaserVisibility(true)

        binding.btnFlash.setOnClickListener {

        }


    }

    fun hasFlash(): Boolean {
         return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun changeMaskColor(view: View?){
        var rnd:Random = Random( )
        var color = android.graphics.Color.argb(100,rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        binding.zxingBarcodeScanner.viewFinder.setMaskColor(color)
    }

    fun changeLaserVisibility(visible:Boolean){
        binding.zxingBarcodeScanner.viewFinder.setLaserVisibility(visible)
    }

    fun switchFlashlight(view: View){
        if (sp.getBoolean("flashOn",true)){
            binding.zxingBarcodeScanner.setTorchOn()
        }else {
            binding.zxingBarcodeScanner.setTorchOff()
        }
    }

    override fun onTorchOn() {
        flashStatus = sp.edit()
        flashStatus.putBoolean("flashOn",false)
        flashStatus.commit()

        binding.zxingBarcodeScanner.background = getDrawable(R.drawable.ic_baseline_flash_off_24)
    }

    override fun onTorchOff() {
        flashStatus = sp.edit()
        flashStatus.putBoolean("flashOn",true)
        flashStatus.commit()

        binding.btnFlash.background = getDrawable(R.drawable.ic_baseline_flash_on_24)
    }
}