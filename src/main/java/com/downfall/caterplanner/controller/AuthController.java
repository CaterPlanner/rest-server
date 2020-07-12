package com.downfall.caterplanner.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("google")
    @ResponseStatus(HttpStatus.OK)
    public String authGoogle(@RequestBody String idToken){

        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
//            Google
            //java.util.concurrent.ExecutionException: com.google.firebase.auth.FirebaseAuthException: Failed to parse Firebase ID token. Make sure you passed a string that represents a complete and valid JWT. See https://firebase.google.com/docs/auth/admin/verify-id-tokens for details on how to retrieve an ID token.
            FirebaseAuth auth;
            GoogleIdToken t;
            GoogleCredential googleCredential;



//            auth.createUser()
            System.out.println(token.getUid());
            System.out.println(token.getEmail());
            System.out.println(token.getPicture());
            System.out.println(token.getIssuer());
            System.out.println(token.getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
