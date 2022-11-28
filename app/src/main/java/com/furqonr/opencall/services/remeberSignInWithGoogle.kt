package com.furqonr.opencall.services

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun rememberSignInWithGoogle(
    onSuccess: (AuthResult?) -> Unit = {},
    onError: (ApiException) -> Unit = {},
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val coroutineScope = rememberCoroutineScope()
    
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                coroutineScope.launch {
                    val authResult = Firebase
                        .auth
                        .signInWithCredential(credential)
                        .await()
                    onSuccess(authResult)
                }
            }
        } catch (e: ApiException) {
            onError(e)
        }
    }
}