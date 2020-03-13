package com.example.biowinoficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcercaDe extends AppCompatActivity {
    //declarar el boton de manera privada
    private ImageButton back;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        //encontrar el boton mediante el id
        back = (ImageButton)findViewById(R.id.imageButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    //Boton para ir a la pagina de Facebook
    public void onFacebook(){
        ImageButton entry = (ImageButton)findViewById(R.id.imageButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.facebook.com/josueramos176/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    private void cerrarSesion() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //CREAR EL MENU FLOTANTE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //OPCIONES DE MENU FLOTANTE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //ACTUALIZAR LA PANTALLA PRINCIPAL
        if(id==R.id.inicio){
            startActivity(new Intent(this, Principal.class));
        }

        //MANDAR A GOOGLE MAPS
        if(id==R.id.centros176){
            Uri gmmIntentUri = Uri.parse("geo:25.691785,-100.3191684");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if(mapIntent.resolveActivity(getPackageManager())!= null){
                startActivity(mapIntent);
            }
        }

        //MANDAR A LA ACTIVITY UTILES
        if(id==R.id.utiles){
            startActivity(new Intent(this, Utiles.class));
        }

        //MANDAR A LA ACTIVITY ACERCA DE
        if(id==R.id.acerca){
            return true;
        }

        //CERRAR LA SESION
        if(id==R.id.logOut){
            cerrarSesion();
        }

        return super.onOptionsItemSelected(item);
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
}
