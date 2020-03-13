package com.example.biowinoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Administracion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private EditText correo, nombre, puntos;
    private TextView mensaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);

        correo = (EditText)findViewById(R.id.editText11);
        nombre = (EditText)findViewById(R.id.editText12);
        puntos = (EditText)findViewById(R.id.editText13);

        mensaje = (TextView)findViewById(R.id.textView25);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void Search (View view){
        final String UID = mDatabase.getKey();
        mDatabase.child("Users").child(UID).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue().toString();
                String pts = dataSnapshot.getValue().toString();

                nombre.setText(name);
                puntos.setText(pts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Administracion.this, "Error al buscar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Actualizar (View view){
        String pts = puntos.getText().toString();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String UID = user.getUid();
        //String email1 = correo.getText().toString();

        Map<String, Object> newmap = new HashMap<>();
        newmap.put("puntos", pts);

        mDatabase.child("Users").child(UID).updateChildren(newmap).isSuccessful();
        Toast.makeText(Administracion.this, "Datos Actualizados", Toast.LENGTH_SHORT).show();
    }
}
