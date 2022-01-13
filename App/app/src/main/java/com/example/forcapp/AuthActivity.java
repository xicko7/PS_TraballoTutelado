package com.example.forcapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class AuthActivity extends AppCompatActivity {

    Button logInButton, signInButton, forgetPasswordButton;
    EditText emailET, passwordET;
    private FirebaseAuth firebaseAuth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
        }
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        setContentView(R.layout.log_layout);
        getSupportActionBar().setTitle(R.string.auth);

        setUI();
    }

    private void setUI() {
        logInButton = findViewById(R.id.log_in_bt);
        signInButton = findViewById(R.id.sign_in_bt);
        forgetPasswordButton = findViewById(R.id.forget_password_bt);
        emailET = findViewById(R.id.email_et);
        passwordET = findViewById(R.id.password_et);

        awesomeValidation.addValidation(this, R.id.email_et, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.password_et, ".{6,}", R.string.invaild_password);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailET.getText());
                String password = String.valueOf(passwordET.getText());

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            errorToast(errorCode);
                        }
                    }
                });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signIntent);
            }
        });

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    private void errorToast(String errorCode) {

        switch (errorCode) {

            /*
            PASAR A GALEGO
             */

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(AuthActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(AuthActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(AuthActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(AuthActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                emailET.setError("La dirección de correo electrónico está mal formateada.");
                emailET.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(AuthActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                passwordET.setError("la contraseña es incorrecta ");
                passwordET.requestFocus();
                passwordET.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(AuthActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(AuthActivity.this, "Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(AuthActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(AuthActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                emailET.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                emailET.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(AuthActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(AuthActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(AuthActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(AuthActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(AuthActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(AuthActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(AuthActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                passwordET.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                passwordET.requestFocus();
                break;

        }
    }
}
