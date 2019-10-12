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
import android.webkit.MimeTypeMap
import android.R
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import android.R.attr.path


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
            shareFilePath(call.arguments)
        } else if (call.method == "shareMultiple") {
            val argsMap = call.arguments as HashMap<String, String>
            val videoPaths = argsMap["videoPaths"] as List<String>
            shareMultiple(videoPaths)
        } else {
            result.notImplemented()
        }
    }

    private fun shareFilePath(arguments: Any) {
        val argsMap = arguments as HashMap<String, String>
        val title = argsMap["title"]
        val filePath = argsMap["filePath"]


        val activeContext = _registrar?.activeContext()

        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.type = filePath?.let { getMimeType(it) }
        val file = File(filePath)


        val fileProviderAuthority = (activeContext?.getPackageName() ?: "dhayaec") + providerAuthExt
        val contentUri = activeContext?.let { FileProvider.getUriForFile(it, fileProviderAuthority, file) }
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        // add optional text

        activeContext?.startActivity(Intent.createChooser(shareIntent, title))
    }

    fun shareMultiple(files: List<String>) {

        val uris = ArrayList<Uri>()
        for (file in files) {
            val file = File(file)
            uris.add(Uri.fromFile(file))
        }
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.type = "*/*"
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)

        val activeContext = _registrar?.activeContext()

        activeContext?.startActivity(Intent.createChooser(intent, "Share to WhatsApp"))
    }

    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}
