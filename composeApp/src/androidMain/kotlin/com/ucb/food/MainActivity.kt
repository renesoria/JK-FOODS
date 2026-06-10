package com.ucb.food

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.ucb.food.work.EventWorker
import com.ucb.food.work.ConfigWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setupProcessObserver()
        launchInitialSync()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Error obteniendo el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN", "Mi Token es: $token")
        }

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val valoresPorDefecto = mapOf(
            "imagen_bool" to false,
            "texto_bienvenida" to "Hola desde local",
            "color_boton_principal" to "Azul",
            "limite_intentos_login" to 3
        )

        remoteConfig.setDefaultsAsync(valoresPorDefecto)

        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("RemoteConfig", "Configuración actualizada: $updated")
            }
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate()
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e("RemoteConfig", "Error: ${error.message}")
            }
        })
        setContent {
            App()
        }
    }

    private fun launchInitialSync() {
        val syncRequest = OneTimeWorkRequestBuilder<ConfigWorker>().build()
        WorkManager.getInstance(this).enqueue(syncRequest)
    }

    private fun setupProcessObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> scheduleEventWorker("OPEN")
                Lifecycle.Event.ON_STOP -> scheduleEventWorker("CLOSE")
                else -> {}
            }
        })
    }

    private fun scheduleEventWorker(type: String) {
        val data = Data.Builder()
            .putString("EVENT_TYPE", type)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<EventWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
