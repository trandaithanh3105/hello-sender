package com.example.hellosender

import android.app.Activity
import android.os.Bundle
import android.widget.*
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

        val btn = Button(this).apply {
            text = "Gửi test"
        }

        btn.setOnClickListener {
            val url = input.text.toString().trim()

            if (!url.startsWith("http")) {
                Toast.makeText(this, "URL không hợp lệ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Thread {
                try {
                    val client = OkHttpClient()

                    val json = """
                        {
                          "from": "android",
                          "message": "Xin chào từ app Android",
                          "time": ${System.currentTimeMillis()}
                        }
                    """.trimIndent()

                    val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

                    val request = Request.Builder()
                        .url(url)
                        .post(body)
                        .build()

                    val response = client.newCall(request).execute()
                    val code = response.code

                    runOnUiThread {
                        Toast.makeText(this, "HTTP $code", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
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
