package com.ieseljust.pmdm.contactes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ieseljust.pmdm.contactes.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    // Definim el contacte actual que estem editant
    private var contacteActual:Contacte? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // Agafem la informació a traves dels arguments
        val contacte: Contacte? = arguments?.getSerializable("contacte") as Contacte?

        contacte?.also {  // Si el contacte no és nul...

            // Actualitzem el contacte actual
            contacteActual=it

            // (En aquesta funció d'àmbit, it farà referència al propi contacte)

            // Actualitzem les vistes de la interfície
            binding.imageViewContacte.setImageResource(it.img)
            binding.editTextTextPersonName.setText(it.name)
            binding.editTextPhone.setText(it.phone)
            binding.editTextTextEmailAddress.setText(it.email)

            // Actualització de l'spinner
            // Recorrem els diferents valors d'aquest, i comparem
            // amb el valor guardat. Si coindiceix, deixem seleccionat el valor.

            for (i in 0..binding.spinnerClasse.adapter.count)
                if (binding.spinnerClasse.adapter.getItem(i).equals(it.classe)) {
                    binding.spinnerClasse.setSelection(i)
                    break
                }

        }


        // Finalment retornem l'arrel del binding

        return binding.root

    }

    fun guardaContacte(){
        // Creem un nou contacte a partir de la informació de les vistes
        val nou=Contacte(
            binding.editTextTextPersonName.text.toString(),
            binding.spinnerClasse.selectedItem.toString(),
            contacteActual?.img?:0, // La imatge serà la mateixa
            binding.editTextPhone.text.toString(),
            binding.editTextTextEmailAddress.text.toString())

        if (contacteActual!=null)  {
            // I  una vegada creat, reemplacem el ContacteActual per aquest
            Contactes.replace(contacteActual!!, nou)
            Snackbar.make(binding.root,
                "Contact has been modified successful",
                Snackbar.LENGTH_LONG).setAction("Close", {
                findNavController().navigateUp()
            }).show()
        } else {
            // Si el contacte actual és nul, el que farem serà afegir un
            // nou contacte
            Contactes.add(nou)
            Snackbar.make(binding.root,
                "Contact has been Added successful",
                Snackbar.LENGTH_LONG).setAction("Close", {
                findNavController().navigateUp()
            }).show()

            // Actualitzem el contacte actual, per si ara es modifica
            contacteActual=nou
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Capturem el click sobre el botó de guardar:
        binding.buttonSave.setOnClickListener {
            guardaContacte()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}