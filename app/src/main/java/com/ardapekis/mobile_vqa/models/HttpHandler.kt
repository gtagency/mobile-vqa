package com.ardapekis.mobile_vqa.models

import android.graphics.Bitmap
import android.util.Base64
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.io.*




/**
 * Created by igolo on 27.02.2018.
 */
class HttpHandler (val serverUrl: String) {

    fun getAnswer(uuid: UUID, question: String): String? {

        val urlStr = "$serverUrl?uuid=$uuid&q=$question"
        val urlConnection: HttpURLConnection

        return try {
            urlConnection = URL(urlStr).openConnection() as HttpURLConnection
            //urlConnection.connect()
            urlConnection.requestMethod = "GET"
            val rd = BufferedReader(InputStreamReader(urlConnection.inputStream))
            var line: String
            var resp = ""
            while (true) {
                try {
                    line = rd.readLine()
                    if (line == null)
                        break
                    resp += line
                }
                catch (e: Throwable) {
                    break
                }
            }
            rd.close()
            urlConnection.disconnect()
            resp
        } catch (e: Throwable) {
            e.message
        }


    }

    // For POST: uuid - semicolon - image
    fun postImageData(data: ImageData): Int {
        val bos = ByteArrayOutputStream()
        data.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bytes = bos.toByteArray()
        val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)

        val urlStr = serverUrl
        val urlConnection: HttpURLConnection

        return try {
            urlConnection = URL(urlStr).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.doInput = true
            urlConnection.doOutput = true

            val out = BufferedOutputStream(urlConnection.outputStream)

            val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))

            writer.write("${data.uuid};$encoded")

            writer.flush()

            writer.close()

            out.close()

            urlConnection.connect()

            val resp = urlConnection.responseCode
            resp
        } catch (e: Throwable) {
            -1
        }
    }
}