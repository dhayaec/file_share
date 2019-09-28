package com.dhayanandhan.file_share

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.util.HashMap



class FileSharePlugin : MethodCallHandler {
    private val providerAuthExt = ".fileprovider.github.com/dhayaec/file_share"
    private val _registrar: Registrar? = null
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "github.com/dhayaec/file_share")
            channel.setMethodCallHandler(FileSharePlugin())
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "filePath") {

        } else {
            result.notImplemented()
        }
    }

    private fun file(arguments: Any) {
        val argsMap = arguments as HashMap<String, String>
        val title = argsMap["title"]
        val name = argsMap["name"]
        val mimeType = argsMap["mimeType"]
        val text = argsMap["text"]

        val activeContext = _registrar.activeContext()

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mimeType
        val file = File(activeContext.getCacheDir(), name)
        val fileProviderAuthority = activeContext.getPackageName() + providerAuthExt
        val contentUri = FileProvider.getUriForFile(activeContext, fileProviderAuthority, file)
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        // add optional text
        if (!text!!.isEmpty()) shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        activeContext.startActivity(Intent.createChooser(shareIntent, title))
    }
}
