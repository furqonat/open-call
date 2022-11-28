package com.furqonr.opencall.ui.components.intro

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.furqonr.opencall.R
import com.furqonr.opencall.services.rememberSignInWithGoogle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Google

@Composable
fun SignInWithGoogleButton(
    onSuccess: () -> Unit = {},
) {
    val launcher = rememberSignInWithGoogle({
        Log.i("SignInWithGoogleButton", "onSuccess $it")
        if (it?.user != null) {
            Log.i("user", "onSuccess: ${it.user}")
            onSuccess()
        }
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
    ) {
        Icon(
            imageVector = FontAwesomeIcons.Brands.Google,
            contentDescription = "Google icon",
            modifier = Modifier
                .padding(8.dp)
                .width(24.dp),
        )
    }
}