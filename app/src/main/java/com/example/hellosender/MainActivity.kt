package com.example.hellosender

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val input = EditText(this).apply {
            hint = "HELLO_URL (https://.../hello.php)"
        }
        val btn = Button(this).apply { text = "Gửi lời chào" }

        btn.setOnClickListener {
            val url = input.text.toString().trim()
            val data = workDataOf(
                "url" to url,
                "message" to "Xin chào từ Android!",
                "ts" to System.currentTimeMillis()
            )
            val req = OneTimeWorkRequestBuilder<HelloWorker>()
                .setInputData(data)
                .build()
            WorkManager.getInstance(this).enqueue(req)
            Toast.makeText(this, "Đã gửi!", Toast.LENGTH_SHORT).show()
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
