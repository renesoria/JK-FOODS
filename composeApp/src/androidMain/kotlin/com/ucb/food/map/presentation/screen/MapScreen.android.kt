package com.ucb.food.map.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.viewinterop.AndroidView
import com.ucb.food.map.presentation.state.MapEffect
import com.ucb.food.map.presentation.state.MapEvent
import com.ucb.food.map.presentation.state.toMapState
import com.ucb.food.map.presentation.viewmodel.MapViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.foundation.layout.*
import android.view.ViewGroup
import androidx.compose.ui.layout.onSizeChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun MapScreen(
    viewModel: MapViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val json = remember { Json { ignoreUnknownKeys = true } }
    var isJsReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MapEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Mapa de Restaurantes") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        viewModel.onEvent(MapEvent.OnBackClicked)
                    }
                ) {
                    Text("⬅️")
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onSizeChanged { size ->
                    Log.d(
                        "MapLayout",
                        "Contenedor Compose: ${size.width}x${size.height}"
                    )
                }
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        configureMapSettings()

                        webChromeClient = object : WebChromeClient() {
                            override fun onConsoleMessage(
                                message: ConsoleMessage?
                            ): Boolean {
                                Log.d(
                                    "MapJS",
                                    "${message?.message()} -- line ${message?.lineNumber()}"
                                )
                                return true
                            }
                        }

                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun getRestaurants(): String {
                                    val data = state.restaurants.map {
                                        it.toMapState()
                                    }
                                    return json.encodeToString(data)
                                }

                                @JavascriptInterface
                                fun onMapReady() {
                                    isJsReady = true
                                }
                            },
                            "AndroidBridge"
                        )

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(
                                view: WebView?,
                                url: String?
                            ) {
                                Log.d(
                                    "MapWebView",
                                    "HTML cargado correctamente"
                                )
                                injectRestaurants(
                                    view as? WebView,
                                    state,
                                    json
                                )
                            }
                        }

                        var htmlLoaded = false

                        addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
                            val webViewWidth = right - left
                            val webViewHeight = bottom - top

                            Log.d(
                                "MapWebViewSize",
                                "WebView nativo: ${webViewWidth}x${webViewHeight}"
                            )

                            if (!htmlLoaded && webViewWidth > 0 && webViewHeight > 0) {
                                htmlLoaded = true

                                Log.d(
                                    "MapWebViewSize",
                                    "Cargando HTML después de recibir tamaño válido"
                                )

                                loadUrl("file:///android_asset/leaflet_map.html")
                            }
                        }
                    }
                },
                update = { webView ->
                    if (isJsReady && !state.isLoading) {
                        injectRestaurants(webView, state, json)
                    }
                }
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureMapSettings() {
    WebView.setWebContentsDebuggingEnabled(true)
    settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        loadsImagesAutomatically = true
        allowFileAccess = true
        allowContentAccess = true
        cacheMode = WebSettings.LOAD_DEFAULT
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        userAgentString = "${userAgentString} JK-FOODS/1.0"
    }
}

private fun injectRestaurants(webView: WebView?, state: com.ucb.food.map.presentation.state.MapState, json: Json) {
    if (state.restaurants.isNotEmpty()) {
        val data = state.restaurants.map { it.toMapState() }
        val jsonString = json.encodeToString(data)
        val safeJson = jsonString.replace("'", "\\'")
        webView?.post {
            // Llamamos a setRestaurants que es la función definida en el HTML
            webView.evaluateJavascript("if(window.setRestaurants) { window.setRestaurants('$safeJson'); }", null)
        }
    }
}
