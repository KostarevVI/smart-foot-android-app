package com.example.smartfootapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.smartfootapp.ble.ConnectionEventListener
import com.example.smartfootapp.ble.FootBleManager
import com.example.smartfootapp.databinding.ActivityBleBinding
import com.example.smartfootapp.databinding.ActivityMainBinding
import com.example.smartfootapp.utils.isIndicatable
import com.example.smartfootapp.utils.isNotifiable
import com.example.smartfootapp.utils.isReadable
import com.example.smartfootapp.utils.isWritable
import com.example.smartfootapp.utils.isWritableWithoutResponse
import com.example.smartfootapp.utils.toHexString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class BleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            ?: error("Missing BluetoothDevice from MainActivity!")

        binding = ActivityBleBinding.inflate(layoutInflater, null, false)

        this.appComponent.inject(this)

        bleConnectionManager.registerListener(connectionEventListener)


        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        testCharateristics()
    }

    private fun testCharateristics() {
        val chars = characteristics
        val duration = chars.firstOrNull { it.uuid == motorDataUUID }
        duration?.let {
            bleConnectionManager.readCharacteristic(device, it)
        }
    }

    override fun onDestroy() {
        bleConnectionManager.unregisterListener(connectionEventListener)
        bleConnectionManager.teardownConnection(device)
        super.onDestroy()
    }

    // BLE
    private lateinit var device: BluetoothDevice

    @Inject
    lateinit var bleConnectionManager: FootBleManager

    private val characteristics by lazy {
        bleConnectionManager.servicesOnDevice(device)?.flatMap { service ->
            service.characteristics ?: listOf()
        } ?: listOf()
    }
    private val characteristicProperties by lazy {
        characteristics.associateWith { characteristic ->
            mutableListOf<CharacteristicProperty>().apply {
                if (characteristic.isNotifiable()) add(CharacteristicProperty.Notifiable)
                if (characteristic.isIndicatable()) add(CharacteristicProperty.Indicatable)
                if (characteristic.isReadable()) add(CharacteristicProperty.Readable)
                if (characteristic.isWritable()) add(CharacteristicProperty.Writable)
                if (characteristic.isWritableWithoutResponse()) {
                    add(CharacteristicProperty.WritableWithoutResponse)
                }
            }.toList()
        }
    }

    private var notifyingCharacteristics = mutableListOf<UUID>()

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onDisconnect = {
                runOnUiThread {
                    // TODO show disconnect
                }
            }

            onCharacteristicRead = { _, characteristic ->
                Log.d(
                    LOG_TAG,
                    "Read from ${characteristic.uuid}: ${characteristic.value.toHexString()}"
                )
            }

            onCharacteristicWrite = { _, characteristic ->
                Log.d(LOG_TAG, "Wrote to ${characteristic.uuid}")
            }

            onMtuChanged = { _, mtu ->
                Log.d(LOG_TAG, "MTU updated to $mtu")
            }

            onCharacteristicChanged = { _, characteristic ->
                Log.d(
                    LOG_TAG,
                    "Value changed on ${characteristic.uuid}: ${characteristic.value.toHexString()}"
                )
            }

            onNotificationsEnabled = { _, characteristic ->
                Log.d(LOG_TAG, "Enabled notifications on ${characteristic.uuid}")
                notifyingCharacteristics.add(characteristic.uuid)
            }

            onNotificationsDisabled = { _, characteristic ->
                Log.d(LOG_TAG, "Disabled notifications on ${characteristic.uuid}")
                notifyingCharacteristics.remove(characteristic.uuid)
            }
        }
    }

    private enum class CharacteristicProperty {
        Readable,
        Writable,
        WritableWithoutResponse,
        Notifiable,
        Indicatable;

        val action
            get() = when (this) {
                Readable -> "Read"
                Writable -> "Write"
                WritableWithoutResponse -> "Write Without Response"
                Notifiable -> "Toggle Notifications"
                Indicatable -> "Toggle Indications"
            }
    }

    companion object {
        private const val LOG_TAG = "BleActivity"

        private val insoleLeftServiceUUID = UUID.fromString("164e3f21-1664-49c8-81ee-16bc6f14d75a")
        private val insoleRightServiceUUID = UUID.fromString("767f3bcf-5464-4b2b-8f37-73391cf0f1f0")
        private val insoleMasterServiceUUID =
            UUID.fromString("8828b254-a426-4cb4-8b5b-35d82e9e511b")

        // master service characteristics
        private val durationUUID = UUID.fromString("0000f00d-0000-1000-8000-00805f9b34fb")
        private val repeatsUUID = UUID.fromString("0dc552e9-9cf3-4cc7-9d5e-7c0e4cc1dba9")
        private val exDataUUID = UUID.fromString("4b4e35ee-799d-4bd7-9e91-1d19a9d1fae0")
        private val vibrationUUID = UUID.fromString("00a26856-8f0a-474a-8ad5-0615baf93cef")
        private val stateUUID = UUID.fromString("8ca2b0d9-acec-4b23-9c30-7b56baecb526")
        private val commandUUID = UUID.fromString("a7236701-cf02-449d-b2bd-dfdde31bee50")

        // Insole service characteristics
        private val imuDataUUID = UUID.fromString("3afe7e11-2590-4c1d-8de7-9840eed05940")
        private val fsrDataUUID = UUID.fromString("38367ab9-3dd6-4cb3-a697-678159344a69")
        private val motorDataUUID = UUID.fromString("88a38dd3-529c-438b-9089-2a806f042664")
        private val insoleCommandUUID = UUID.fromString("6f99f925-cfce-4676-92aa-5aa8f3844c27")
    }
}