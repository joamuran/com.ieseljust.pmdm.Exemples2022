package com.ieseljust.pmdm.exempleratingbar

import android.os.Bundle
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ieseljust.pmdm.exempleratingbar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Si volem que el ratingBar no es puga modificar hem de fer
        binding.ratingBar.setIsIndicator(true)

        // Si volem que el ratingBar sí que es puga modificar farem
        binding.ratingBar.setIsIndicator(false)

        // Per donar un valor inicial al RatingBar podem fer (ha de ser un float):
        binding.ratingBar.rating=2.5f // També podem usar .rating per obtenir el valor

        // Amb açò, cada vegada que modifiquem el rating, es dispara l'acció indicada,
        // en aquest cas, un Toast.
        binding.ratingBar.setOnRatingBarChangeListener({ ratingBar, v, b ->
            Toast.makeText(this, "Valoració: "+v.toString(), Toast.LENGTH_SHORT).show()
        })


    }
}