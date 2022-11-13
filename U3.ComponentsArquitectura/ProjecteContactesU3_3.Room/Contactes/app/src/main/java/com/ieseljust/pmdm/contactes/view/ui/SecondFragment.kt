package com.ieseljust.pmdm.contactes.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ieseljust.pmdm.contactes.R
import com.ieseljust.pmdm.contactes.databinding.FragmentSecondBinding
import com.ieseljust.pmdm.contactes.model.db.Contacte
import com.ieseljust.pmdm.contactes.viewmodel.AppContactesViewModel

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    // Definim el contacte actual que estem editant
    private var contacteActual: Contacte? = null

    // Adaptació a MVVM: Referència al ViewModel
    private lateinit var viewModel: AppContactesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // La funcionalitat per inicialitzar la vista l'hem afegir el mètode onViewCreated

        // Finalment retornem l'arrel del binding

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adaptació a MVVM: Instanciem el ViewModel, utilitzant la classe ViewModelProvider
        viewModel = ViewModelProvider(requireActivity())[AppContactesViewModel::class.java]

        // Adaptació a MVVM: Afegim observadors al LiveData
        // contacteActual per a ser notificats dels canvis

        viewModel.contacteActual.observe(viewLifecycleOwner) {
            // I quan aquests es produïsquen, actualitzem les vistes de la interfície
            it?.let {
                // Actualitzem les vistes de la interfície
                // I preparem els TextViews  per actualitzar el ViewModel quan canvien de valor
                binding.imageViewContacte.setImageResource(it.img)

                // TextView per al nom
                binding.editTextTextPersonName.setText(it.name)
                binding.editTextTextPersonName.doOnTextChanged { text, _, _, _ ->
                    viewModel.contacteActual.value?.name=text.toString()
                }

                // TextView per al telèfon
                binding.editTextPhone.setText(it.phone)
                binding.editTextPhone.doOnTextChanged { text, _, _, _ ->
                    viewModel.contacteActual.value?.phone=text.toString()
                }

                // TextView per al correu
                binding.editTextTextEmailAddress.setText(it.email)
                binding.editTextTextEmailAddress.doOnTextChanged { text, _, _, _ ->
                    viewModel.contacteActual.value?.email=text.toString()
                }


                // Actualització de l'spinner
                // Recorrem els diferents valors d'aquest, i comparem
                // amb el valor guardat. Si coindiceix, deixem seleccionat el valor.

                for (i in 0 until binding.spinnerClasse.adapter.count)
                    if (binding.spinnerClasse.adapter.getItem(i).equals(it.classe)) {
                        binding.spinnerClasse.setSelection(i)
                        break
                    }
            }
        }

        // Observadors per saber quan s'ha guardat o modificat un contacte

        viewModel.contacteSaved.observe(viewLifecycleOwner) { saved ->
            saved?.let {
                // Netegem el valor per a no entrar en un bucle infinit
                viewModel.contacteSaved.value=null
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.contacteSaved),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }


        viewModel.contacteUpdated.observe(viewLifecycleOwner) { updated ->
            updated?.let {
                // Netegem el valor per a no entrar en un bucle infinit
                viewModel.contacteUpdated.value=null
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.updated),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // Capturem el click sobre el botó de guardar:
        binding.buttonSave.setOnClickListener {
            guardaContacte()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun guardaContacte(){

        // Creem un nou contacte a partir de la informació de les vistes
        // Recordeu que de moment agafem la imatge actual, que ara s'ubica
        // al contacte emmagatzemat al ViewModel

        // Utilitzem el constructor mitjançant arguments per nom en lloc de posicionals
        val nou= Contacte(
            name=binding.editTextTextPersonName.text.toString(),
            classe=binding.spinnerClasse.selectedItem.toString(),
            img=viewModel.contacteActual?.value?.img?:0, // La imatge serà la mateixa
            phone=binding.editTextPhone.text.toString(),
            email=binding.editTextTextEmailAddress.text.toString())

        Log.d("Debug meu", "11111111111111111")
        viewModel.guardaContacte(nou)
        Log.d("Debug meu","22222222222222222")

    }
}