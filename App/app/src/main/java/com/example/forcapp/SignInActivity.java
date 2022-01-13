package com.example.forcapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignInActivity extends AppCompatActivity {

    Button signInButton;
    EditText emailET, passwordET;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
        getSupportActionBar().setTitle(R.string.auth);

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        setUI();
    }

    private void setUI() {
        signInButton = findViewById(R.id.sign_in_bt_si);
        emailET = findViewById(R.id.email_et_si);
        passwordET = findViewById(R.id.password_et_si);

        awesomeValidation.addValidation(this, R.id.email_et_si, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.password_et_si, ".{6,}", R.string.invaild_password);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailET.getText());
                String password = String.valueOf(passwordET.getText());

                if (awesomeValidation.validate()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                errorToast(errorCode);
                            }
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "Introduce correo e contrasinal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void errorToast(String errorCode) {

        switch (errorCode) {

            /*
            PASAR A GALEGO
             */

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(SignInActivity.this, R.string.ERROR_INVALID_CUSTOM_TOKEN, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(SignInActivity.this, R.string.ERROR_CUSTOM_TOKEN_MISMATCH, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(SignInActivity.this, R.string.ERROR_INVALID_CREDENTIAL, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(SignInActivity.this, R.string.ERROR_INVALID_EMAIL , Toast.LENGTH_LONG).show();
                emailET.setError(String.valueOf(R.string.ERROR_INVALID_EMAIL));
                emailET.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(SignInActivity.this, R.string.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show();
                passwordET.setError(String.valueOf(R.string.contrasinalIncorrecto));
                passwordET.requestFocus();
                passwordET.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(SignInActivity.this, R.string.ERROR_USER_MISMATCH, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(SignInActivity.this, R.string.ERROR_REQUIRES_RECENT_LOGIN, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(SignInActivity.this, R.string.ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(SignInActivity.this, R.string.ERROR_EMAIL_ALREADY_IN_USE , Toast.LENGTH_LONG).show();
                emailET.setError(String.valueOf(R.string.ERROR_EMAIL_ALREADY_IN_USE));
                emailET.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(SignInActivity.this, R.string.ERROR_CREDENTIAL_ALREADY_IN_USE, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(SignInActivity.this, R.string.ERROR_USER_DISABLED, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(SignInActivity.this, R.string.ERROR_USER_TOKEN_EXPIRED, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(SignInActivity.this, R.string.ERROR_USER_NOT_FOUND, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(SignInActivity.this, R.string.ERROR_INVALID_USER_TOKEN, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(SignInActivity.this, R.string.ERROR_OPERATION_NOT_ALLOWED, Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(SignInActivity.this, R.string.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show();
                passwordET.setError(String.valueOf(R.string.ERROR_MIN_6CHARS));
                passwordET.requestFocus();
                break;

        }
    }
}