package com.nomnomapp.nomnom.model

import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoUri: Uri,
    cornerRadius: Float = 16f,
    isVisible: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var videoView: VideoView? by remember { mutableStateOf(null) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> videoView?.pause()
                Lifecycle.Event.ON_RESUME -> if (isVisible) videoView?.start()
                Lifecycle.Event.ON_DESTROY -> {
                    videoView?.stopPlayback()
                    videoView = null
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            videoView?.stopPlayback()
            videoView = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (isVisible) {
        AndroidView(
            modifier = modifier
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(cornerRadius)),
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        start()
                    }
                    videoView = this
                }
            },
            update = { view ->
                videoView = view
            }
        )
    }
}