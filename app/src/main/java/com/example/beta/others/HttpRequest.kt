package com.example.beta.others



import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest (){

    @Throws(IOException::class)
    fun doHTTP(url: URL, data: JSONObject, method: String): List<String>? {

        var stream: InputStream? = null
        var result :List<String>? = null
        var out: OutputStream? = null
        var connection: HttpURLConnection? = null
        var responseCode: Int?
        var streamRes :String = ""

        try {

            connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 20000
            connection.connectTimeout = 200000
            connection.requestMethod = method
            connection.doInput = true
            connection.setChunkedStreamingMode(0)
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-type", "application/json")

            out = BufferedOutputStream(connection.outputStream)
            out.write(data.toString().toByteArray())
            out.flush()

            responseCode = connection.responseCode

            stream = connection.inputStream

            if (stream != null) {

                streamRes = convertStreamToString(stream)
            }

            result = listOf(responseCode.toString(), streamRes)

        } finally {

            out?.close()
            stream?.close()
            connection?.disconnect()

            return result
        }
    }

    @Throws(IOException::class)
    fun readStream(stream: InputStream, length: Int): String {

        val res: String
        val b: ByteArray? = null

        stream.read(b!!, 0, length)

        res = b.toString()

        return res
    }

    fun convertStreamToString(inputStream: InputStream): String {

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var AllString: String = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    AllString += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: java.lang.Exception) {
        }

        return AllString
    }

    fun isNetworkConnected(context:Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo

            if (ni != null) {
                return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
            }
        } else {
            val n = cm.activeNetwork

            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)

                return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
        }
        return false
    }
}
