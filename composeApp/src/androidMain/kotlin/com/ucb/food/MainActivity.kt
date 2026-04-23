package com.ucb.food

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // --- TOKEN PARA PRUEBAS ---
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM_TOKEN", "Mi Token es: ${task.result}")
            }
        }

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val valoresPorDefecto = mapOf("texto_bienvenida" to "Hola local")
        remoteConfig.setDefaultsAsync(valoresPorDefecto)
        remoteConfig.fetchAndActivate()

        // ESCUCHADOR DE NOTIFICACIONES (Lo que ya te daba puntos)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    if (configUpdate.updatedKeys.contains("texto_bienvenida")) {
                        val nuevoTexto = remoteConfig.getString("texto_bienvenida")
                        showLocalNotification("Food App Update", "Nuevo mensaje: $nuevoTexto")
                        Log.d("RemoteConfig", "Notificación enviada: $nuevoTexto")
                    }
                }
            }
            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e("RemoteConfig", "Error: ${error.message}")
            }
        })

        setContent {
            App()
        }
    }

    private fun showLocalNotification(title: String, content: String) {
        val channelId = "food_updates"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notificaciones", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
