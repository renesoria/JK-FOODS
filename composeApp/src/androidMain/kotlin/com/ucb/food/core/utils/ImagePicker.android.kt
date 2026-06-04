package com.ucb.food.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bytes = inputStream?.readBytes()
            bytes?.let { b ->
                // Optimize image size before base64 conversion
                val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                val optimizedBytes = outputStream.toByteArray()
                
                val base64 = "data:image/jpeg;base64," + bytesToBase64(optimizedBytes)
                onImagePicked(base64)
            }
        }
    }
    return { launcher.launch("image/*") }
}
