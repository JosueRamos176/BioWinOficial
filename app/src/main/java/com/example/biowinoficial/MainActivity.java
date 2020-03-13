package com.example.biowinoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {
    //DECLARACION DE LAS VARIABLES
    private EditText emailTV, passwordTV;
    private Button loginBtn, adminis;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //INSTANCIARLOS A LOS BOTONES Y EDITTEXT
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);

        loginBtn = findViewById(R.id.login);
        adminis = findViewById(R.id.admin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        //SI HAY UN USUARIO LOGUEADO EN EL MOMENTO Y SE CIERRA LA APP GUARDA SU SESION
        //SI LA APP SE CIERRA Y HAY UN USUARIO SE ABRE EN LA PANTALLA PRINCIPAL
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    if(!user.isEmailVerified()){
                        Toast.makeText(MainActivity.this, "Email NO Verificado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Principal.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    //FUNCION PARA BUSCAR EL USUARIO Y CONTRASEÑA DE FIREBASE Y SI LO ENCUENTRA SE ENVIA A LA
    //ACTIVITY PRINCIPAL
    public void loginUserAccount(View view) {
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();

        final String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Ingrese el correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //INGRESAR MEDIANTE EL EMAIL Y PASSWORD
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, Principal.class));
                    Toast.makeText(getApplicationContext(), "Ingreso Exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    //IR A LA ACTIVITY DE REGISTRO.CLASS
    public void Registrarse(View view){
            Intent Registrarse = new Intent(this, Registro.class);
            startActivity(Registrarse);
    }

    //IR A LA ACTIVIDAD ADMINISTRACION SOLO SI AGREGA LOS DATOS CORRECTOS
    public void Admin(View view) {
        startActivity(new Intent(MainActivity.this, Administracion.class));
    }
}
