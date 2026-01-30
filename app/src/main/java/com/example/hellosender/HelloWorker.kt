package com.example.hellosender

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class HelloWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString("url").orEmpty()
        val msg = inputData.getString("message").orEmpty()
        val ts = inputData.getLong("ts", 0L)

        if (!url.startsWith("https://")) return@withContext Result.failure()

        val json = """{"message":"$msg","ts":$ts}"""
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val req = Request.Builder().url(url).post(body).build()

        try {
            client.newCall(req).execute().use {
                if (it.isSuccessful) Result.success() else Result.failure()
            }
        } catch (_: Exception) {
            Result.failure()
        }
    }
}
