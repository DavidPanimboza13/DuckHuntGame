package com.panimbozadavid.duckhunt

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Cambiar tipo de fuente
        val customTypeface = resources.getFont(R.font.pixel)
        editTextNick.typeface = customTypeface
        buttonStart.typeface = customTypeface

        //Reproducción de sonido
        MediaPlayer.create(this, R.raw.title_screen).start()

        //Eventos clic
        buttonStart.setOnClickListener {
            val nick = editTextNick.getText().toString()
            if (nick.isEmpty()) {
                editTextNick.setError("El nombre de usuario es obligatorio")
            } else if (nick.length < 3) {
                editTextNick.setError("Debe tener al menos 3 caracteres")
            } else {
                ProcesarUsuarioFirestore(nick)
            }
        }

        buttonHallDeFama.setOnClickListener{
            val i = Intent(this, HallDeFamaActivity::class.java)
            startActivity(i)
        }
    }

    fun ProcesarUsuarioFirestore(nick:String){
        //instancia de la base de datos
        val db = FirebaseFirestore.getInstance()
        var  idDocumentReference = FirebaseFirestore.getInstance().collection("users").document().id
        db.collection("users")
            .whereEqualTo("nick",nick)
            .get()
            .addOnSuccessListener { documents ->
                if( documents.documents.size == 0) {
                    db.collection("users").document(idDocumentReference).set(User(nick, 0))
                }
                else{
                    idDocumentReference = documents.documents[0].id
                }
                val i = Intent(this@LoginActivity, GameActivity::class.java)
                i.putExtra(EXTRA_NICK, nick)
                i.putExtra(EXTRA_ID, idDocumentReference)
                startActivity(i)
            }

    }

}