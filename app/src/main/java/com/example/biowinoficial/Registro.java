package com.example.biowinoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    //DECLARACION DE VARIABLES
    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegistrar;
    private ProgressDialog progressDialog;
    //Variables de los datos a registrar
    private String name ="";
    private String email ="";
    private String password ="";
    private Integer puntos = 30;
    private Integer libreta = 0;
    private Integer pluma = 0;
    private Integer lapices = 0;
    private Integer hojas = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //INSTANCIARLOS
        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonRegistrar = (Button) findViewById(R.id.btnRegister);

        //INSTANCIAR LOS DE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //DIALOGO
        progressDialog = new ProgressDialog(this);

        //CODIGO PARA EL BOTON DE REGISTRAR
        mButtonRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //INSTANCIARLO A UNA VARIABLE
                name = mEditTextName.getText().toString();
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();
                //SI EL USUARIO Y PASSWORD ESTA VACIO NO TE LO PUEDE GUARDAR
                if(!email.isEmpty() && !password.isEmpty()){
                    if(password.length() >= 6){
                        //MANDAR A LA FUNCION QUE LOS GUARDA
                        registrar176();
                    } else {
                        Toast.makeText(Registro.this, "La contraseña debe tene al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //REGISTRAR LOS USUARIOS QUE SE PRETENDEN GUARDAR
    public void registrar176() {
        //DIALOGO QUE SE ESTA REALIZANDO LA ACCION
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();
        //CREAR EL USUARIO MEDIANTE EL CORREO Y USUARIO
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //SI LA TAREA ESTA COMPLETA PEDIR EL USUARIO
                    final FirebaseUser ur = mAuth.getCurrentUser();
                    //ENVIAR UN CORREO AL USUARIO DEL CORREO
                    ur.sendEmailVerification();
                    //INICIAR LA SIGUIENTE ACTIVITY
                    startActivity(new Intent(Registro.this, Principal.class));
                    //IR A LA FUNCION "GUARDAR DATOS"
                    guardardatosDB();
                } else {
                    //ERROR
                    Toast.makeText(getApplicationContext(), "Error al crear usuario", Toast.LENGTH_LONG).show();
                }
                //SE CIERRA EL DIALOGO
                progressDialog.dismiss();
            }
        });
    }
    //CODIGO PARA GUARDAR LOS DATOS EN LA BASE DE DATOS
    private void guardardatosDB() {
        //MAPA QUE SE ENVIA A FIREBASE
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        // Guardar la contraseña en la base de datos es peligroso ya que puede que Firebase no tenga mucha seguridad
        // map.put("password", password);
        map.put("puntos", puntos);
        //PEDIR EL USUARIO QUE ESTA LOGUEADO
        String id = mAuth.getCurrentUser().getUid();
        //ELEGIR LA BASE DE DATOS. EL ID DEL USUARIO. INGRESAR EL MAPA
        mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                //SI LA TAREA ESTA COMPLETA SE REALIZA LO SIGUIENTE
                if(task2.isSuccessful()){
                    //MANDA UN MENSAJE DE QUE LOS DATOS ESTAN GUARDADOS
                    Toast.makeText(Registro.this, "Datos guardados", Toast.LENGTH_LONG).show();
                } else {
                    //SI NO MANDAR UN MENSAJE DE QUE NO SE PUEDEN GUARDAR
                    Toast.makeText(Registro.this, "No se crearon los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
