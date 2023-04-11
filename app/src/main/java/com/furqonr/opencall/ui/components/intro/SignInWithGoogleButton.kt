package com.furqonr.opencall.ui.components.intro

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.furqonr.opencall.R
import com.furqonr.opencall.services.rememberSignInWithGoogle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult

@Composable
fun SignInWithGoogleButton(
    onSuccess: (AuthResult) -> Unit = {},
) {
    val launcher = rememberSignInWithGoogle(onSuccess ={
        Log.i("SignInWithGoogleButton", "onSuccess $it")
        if (it?.user != null) {
            Log.i("user", "onSuccess: ${it.user}")
            onSuccess(it)
        }
    }, onError = {
        Log.e("Error", "${it.status} ${it.message}")
    })


    val signInWithGoogle =
        GoogleSignInOptions.Builder()
            .requestEmail()
            .requestIdToken(stringResource(id = R.string.google_web_client_id))
            .build()
    val signInIntent = GoogleSignIn.getClient(LocalContext.current, signInWithGoogle).signInIntent
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                launcher.launch(signInIntent)
            },
        shape = CircleShape,
        backgroundColor = Color.White
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google icon",
            modifier = Modifier
                .padding(8.dp)
                .width(24.dp),
        )
    }
}