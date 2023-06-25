package com.example.smartfootapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.smartfootapp.ble.ConnectionEventListener
import com.example.smartfootapp.ble.FootBleManager
import com.example.smartfootapp.databinding.ActivityMainBinding
import com.example.smartfootapp.utils.isIndicatable
import com.example.smartfootapp.utils.isNotifiable
import com.example.smartfootapp.utils.isReadable
import com.example.smartfootapp.utils.isWritable
import com.example.smartfootapp.utils.isWritableWithoutResponse
import com.example.smartfootapp.utils.printGattTable
import com.example.smartfootapp.utils.toHexString
import com.example.smartfootapp.viewmodels.FootViewModel
import java.util.UUID
import javax.inject.Inject


@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: FootViewModel by viewModels { appComponent.viewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater, null, false)

        this.appComponent.inject(this)

        bleConnectionManager.registerListener(connectionEventListener)

        setContentView(binding.root)

        setupViews()

    }

    private fun setupViews() {
        with(binding) {
            val navController =
                (supportFragmentManager.findFragmentById(R.id.fcv_main_fragment) as NavHostFragment).navController

            NavigationUI.setupWithNavController(
                bnvNavigation,
                navController
            )

            bSettings.setOnClickListener {
                navController.navigate(R.id.action_global_settingsFragment)
            }

        }
    }

    override fun onResume() {
        super.onResume()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            ) {
                promptEnableBluetooth()
            } else {
                requestMultiplePermissions.launch(
                    arrayOf(
                        android.Manifest.permission.BLUETOOTH_SCAN,
                        android.Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
            }
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    private var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            Log.e("Activity result", "OK")
            // There are no request codes
            val data = result.data
        }
    }

    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityResultLauncher.launch(enableBtIntent)
        }
    }


    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isAllTrue = true
            permissions.entries.forEach {
                if (!it.value) {
                    isAllTrue = false
                }
            }
            if (isAllTrue) {
                promptEnableBluetooth()
            }
        }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var isScanning = false

    fun toggleBleScan() {
        if (isScanning) {
            stopBleScan()
        } else {
            startBleScan()
        }
    }

    private fun startBleScan() {
        bleScanner.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }

    private fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name.equals("smart insole (l)", ignoreCase = true)) { // TODO
                Log.w(LOG_TAG, "Connecting to ${result.device.address}")
                bleConnectionManager.connect(result.device, this@MainActivity)
                stopBleScan()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(LOG_TAG, "onScanFailed: code $errorCode")
        }
    }

    @Inject
    lateinit var bleConnectionManager: FootBleManager

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                bleConnectionManager.unregisterListener(this)
                Intent(this@MainActivity, BleActivity::class.java).also {
                    it.putExtra(BluetoothDevice.EXTRA_DEVICE, gatt.device)
                    startActivity(it)
                }
            }
            onDisconnect = {
                runOnUiThread {
                    // TODO show disconnect
                }
            }
        }
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
    }

}