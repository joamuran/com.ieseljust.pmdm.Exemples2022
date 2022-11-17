package com.ieseljust.pmdm.intentsactivitats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Definim aquesta activitat com una classe directament
// ja que no necessitem interfície
// El que sí que fem és registrar-la

class ActivitatSuma : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Agafem els sumands de la Intent.
        // Si no tenim sumands, eixim.
        val sumands = intent.extras ?: return

        val sum1 = sumands.getInt("sumand_1")
        val sum2 = sumands.getInt("sumand_2")

        // Creem una nova Intent per al resultat
        val result= Intent()

        // I afegim com a extres el camp "suma" amb
        // la suma dels dos números
        result.putExtra("suma", sum1+sum2)

        // Retornem el resultat amb SetResult, passant-li
        // si l'activitat ha estat exitosa (Activity.RESULT_OK), i
        // la Intent amb el resultat
        setResult(Activity.RESULT_OK,result)

        // I finalment tanquem l'activitat
        finish()
    }
}