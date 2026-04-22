package com.ucb.food

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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

        // --- CÓDIGO PARA EL TOKEN ---
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Error obteniendo el token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN", "Mi Token es: $token")
        }
        // ----------------------------

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            // Bajamos esto a 0 para que durante las pruebas los cambios se vean al instante
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

        // Forzamos la descarga y activación inmediata
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("RemoteConfig", "Configuración actualizada: $updated")
                println("RemoteConfig: ¡Datos descargados y activados correctamente!")
            } else {
                println("RemoteConfig: Error al intentar descargar los datos.")
            }
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                // Si algo cambia en la consola, lo activamos inmediatamente
                remoteConfig.activate().addOnCompleteListener {
                    Log.d("RemoteConfig", "Configuración actualizada en tiempo real")
                    // Esto forzará a que la UI se entere si usas estados reactivos
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                println("RemoteConfig Error en tiempo real: ${error.message}")
            }
        })
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
