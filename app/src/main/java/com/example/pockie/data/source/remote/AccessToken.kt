package com.example.pockie.data.source.remote

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

object AccessToken {
    private const val SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
    suspend fun getAccessToken(context: Context): String? {
        return withContext(Dispatchers.IO) {
            val stream: InputStream = context.assets.open("service-account.json")
            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(SCOPES))
            googleCredentials.refresh()
            Log.d("ACCESSTOKEN", googleCredentials.accessToken.tokenValue)
            googleCredentials.accessToken.tokenValue

        }
    }
}