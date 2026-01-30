package com.example.hellosender

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val input = EditText(this).apply {
            hint = "Webhook URL"
            setText("https://fb2fa.com/apitest/webhook_hello.php")
        }

        val btn = Button(this).apply { text = "Gửi test" }

        btn.setOnClickListener {
            val url = input.text.toString().trim()

            // Hiện lỗi ngay nếu URL sai
            if (!url.startsWith("http")) {
                Toast.makeText(this, "URL không hợp lệ: $url", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Gửi trực tiếp để thấy lỗi ngay (không im lặng)
            Thread {
                try {
                    val client = OkHttpClient()

                    val json = """{"from":"android","message":"Xin chào","time":${System.currentTimeMillis()}}"""
                    val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

                    val req = Request.Builder()
                        .url(url)
                        .post(body)
                        .build()

                    val resp = client.newCall(req).execute()
                    val code = resp.code
                    val respText = resp.body?.string().orEmpty()

                    runOnUiThread {
                        Toast.makeText(this, "HTTP $code\n$respText", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Lỗi: ${e::class.java.simpleName}: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }

        setContentView(
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(40, 60, 40, 40)
                addView(input)
                addView(btn)
            }
        )
    }
}
