package com.ps.wefriends.domain.use_case

import com.google.firebase.auth.FirebaseAuth

class SignInAsGuest(
    private var firebaseAuth: FirebaseAuth
) {
    operator fun invoke(onSuccess: () -> Unit, onError: (Exception) -> Unit){
        firebaseAuth.signInAnonymously().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                onSuccess()
            } else {
                result.exception?.let { onError(it) }
            }
        }
    }
}