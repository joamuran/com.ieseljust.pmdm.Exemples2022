package com.ieseljust.pmdm.provesui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView:TextView=findViewById<TextView>(R.id.textView)
        val PlainText:EditText=findViewById<EditText>(R.id.PlainText)
        val editTextTextPassword:EditText=findViewById<EditText>(R.id.editTextTextPassword)
        val checkBox:CheckBox=findViewById<CheckBox>(R.id.checkBox)
        val checkBox2:CheckBox=findViewById<CheckBox>(R.id.checkBox2)
        val radioGroup:RadioGroup=findViewById<RadioGroup>(R.id.radioGroup)
        val radioButton:RadioButton=findViewById<RadioButton>(R.id.radioButton)
        val radioButton2:RadioButton=findViewById<RadioButton>(R.id.radioButton2)
        val toggleButton:ToggleButton=findViewById<ToggleButton>(R.id.toggleButton)
        val switch1:Switch=findViewById<Switch>(R.id.switch1)
        val spinner:Spinner=findViewById<Spinner>(R.id.spinner)
        val button:Button=findViewById<Button>(R.id.button)

        populateSpinner()

        button.setOnClickListener {
            Log.d("debug", "TextView: "+textView.text)
            Log.d("debug", "Entry Text: "+PlainText.text)
            Log.d("debug", "Password: "+editTextTextPassword.text)
            Log.d("debug", "checkBox: " + if (checkBox.isChecked) "checked" else "unchecked")
            Log.d("debug", "checkBox2: " + if (checkBox2.isChecked) "checked" else "unchecked")
            Log.d("debug", "radioButton1: " + if (radioButton.isChecked) "checked" else "unchecked")
            Log.d("debug", "radioButton2: " + if (radioButton2.isChecked) "checked" else "unchecked")
            Log.d("debug", "toggleButton: " + if (toggleButton.isChecked) "checked" else "unchecked")
            Log.d("debug", "switch1: " + if (switch1.isChecked) "checked" else "unchecked")
            Log.d("debug", "Spinner value:"+ spinner.selectedItem.toString())
        }




    }



    private fun populateSpinner() {
        // Creem un ArrayAdapter a partir d'un recurs de tipus array
        // Requereix tres paràmetres: El context, el recurs, i el
        // disseny de l'entrada (utilitzarem el proporcionat per la
        // pròpia plataforma.
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.mesos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Si tenim l'adaptador preparat, seleccionem el disseny
            // per a la llista d'opcions (el proporciona per la plataforma)
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            // I finalment, afegim l'adaptador al spinner
                spinner.adapter = adapter
        }
    }


}




