package com.ieseljust.pmdm.apac2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ieseljust.pmdm.apac2.databinding.ActivityLoginBinding
import com.ieseljust.pmdm.apac2.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    var attempts=0;
    val user="admin"
    val pass="1234"

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var textUserName=binding.editTextTextUsername
        var textPassName=binding.editTextTextPassword
        var textContador=binding.textViewAttempts
        var btLogin=binding.btLogin

        textContador.text="Failed Attempts: "+attempts.toString()

        btLogin.setOnClickListener{
            if(textUserName.text.toString()==user && textPassName.text.toString()==pass && attempts<=3)
            {
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)

            }
            else {
                if (attempts<3) attempts++;
                textContador.text="Failed Attempts: "+attempts.toString()

            }

        }



    }
}