package com.lux.zena.barcodescanner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.lux.zena.barcodescanner.R
import com.lux.zena.barcodescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var options:ScanOptions
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.main=this

        binding.btn.setOnClickListener {
            options=ScanOptions().setOrientationLocked(false).setCaptureActivity(BarcodeScanActivity::class.java)
            options.setBarcodeImageEnabled(true)
            options.setBeepEnabled(true)
            barcodeLauncher.launch(options)
        }
    }
    
    val barcodeLauncher:ActivityResultLauncher<ScanOptions> = registerForActivityResult(ScanContract()){
        if (it.contents==null){
            val intent:Intent = it.originalIntent
            if (intent==null) Log.e("MAIN ACTIVITY 36","Cancelled Scan")
            else if (intent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)){
                Log.e("MAIN ACTIVITY 39","Cancelled scan due to missing camera permission")
            }else{
                Log.e("MAIN ACTIVITY 40","Scanned")
                Toast.makeText(this, "Scanned ${it.contents}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}