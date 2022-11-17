package com.ieseljust.pmdm.intentsactivitats

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.ieseljust.pmdm.intentsactivitats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Bindings
    private lateinit var binding: ActivityMainBinding

    // Registre del carregador
    private val activitatSumaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result: ActivityResult ->
        // Codi del resultat (Activity.RESULT_OK si és correcte)
        val resultCode = result.resultCode
        // Dades associades al resultat
        val resultData: Intent? = result.data

        // Comprovem si el resultat és correcte
        if (resultCode== Activity.RESULT_OK){
            // Si té dades
            if(resultData != null) {
                // Si té un paràmetre extra anomenat suma
                if (resultData.hasExtra("suma")) // Per actualitzar la UI
                    binding.textViewHelloWorld.text =
                        resultData.extras?.getInt("suma").toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentASuma = Intent (this, ActivitatSuma::class.java)

        // Definim els números i els afegim a la Intent
        val sum1=10
        val sum2=20

        intentASuma.putExtra("sumand_1", sum1)
        intentASuma.putExtra("sumand_2", sum2)

        // Llancem la Intent per obtenir un resultat
        activitatSumaLauncher.launch(intentASuma)

    }
}