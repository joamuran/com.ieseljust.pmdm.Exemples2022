package com.ieseljust.pmdm.contactes.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ieseljust.pmdm.contactes.model.Contacte
import com.ieseljust.pmdm.contactes.repository.ContacteRepository

// Definim el ViewModel com a AndroidViewModel, per tal de poder accedir al context
class AppContactesViewModel(application: Application): AndroidViewModel(application) {

    // Definición d'atributs

    // Contacte que s'està editant actualment
    // Observeu que el contacte pot ser nul 
    private val _contacteActual=MutableLiveData<Contacte?>()

    // Accés públic
    val contacteActual: LiveData<Contacte?> = _contacteActual

    // Setter
    fun setContacteActual(contacte:Contacte){
        _contacteActual.value=contacte.copy()
    }

    // Setter a null
    fun cleanContacteActual(){
        _contacteActual.value = null
    }

    // Atributs LiveData per gestionar els clicks, de manera que
    // puguen ser observats per un observer en el fragment

    val contacteLongClicked: MutableLiveData<Contacte> by lazy {
        MutableLiveData<Contacte>()
    }
    val contacteClicked: MutableLiveData<Contacte> by lazy {
        MutableLiveData<Contacte>()
    }

    // Livedata per gestionar també l'element a eliminar quan
    // es fa un click llarg.
    // No ens serveix contacteLongClicked perquè este ha de "viure"
    // més temps.
    val contacteToRemove: MutableLiveData<Contacte> by lazy {
        MutableLiveData<Contacte>()
    }

    // LiveData per a l'adaptador, mitjançant un atribut de suport privat:
    // L'adaptador del RecyclerView també serà un LiveData per a poder ser
    // observat des de la vista.
    private val _adaptador = MutableLiveData<AdaptadorContactes>().apply{

        // Mitjançant value (setValue), establim el valor que contindrà el MutableLibeData
        // de tipus AdaptadorContactes. Aci també proporcionarem els callbacks que
        // s'han d'invocar quan es produisquen els clicks.

        value= AdaptadorContactes(
            {contacte:Contacte, v: View -> ContacteClickedManager(contacte, v)},
            {contacte: Contacte, v: View -> ContacteLongClickedManager(contacte, v)},
            getApplication<Application>().applicationContext // Açò ho podem fer gràcies a derivar d'AndroidViewModel
        )
    }

    // Definim la propietat per a l'adaptador, que podrem llegir des
    // de la vista (ja que _adapter és un atribut de suport privat)
    val adaptador:MutableLiveData<AdaptadorContactes> =_adaptador

    // Definició de mètodes

    // Definim les funcions de callback per a la gestió d'events

    // L'event el gestionarà el FirstFragment, però serà avisat pel
    // ViewModel quan es produeixen els clicks sobre cada element
    // a través d'aquestes variables de tipus MutableLiveData
    // que son contacteClicked i contacteLongClicked.

    private fun ContacteClickedManager(contacte: Contacte, v: View){
        // Para establecer el valor del liveData utilizamos value
        contacteClicked.value=contacte
    }

    private fun ContacteLongClickedManager(contacte:Contacte, v: View):Boolean {
        // Per establir el valor del liveData fem ús de la propietat value
        contacteLongClicked.value=contacte
        // Assignem també el LiveData contacteToRemove
        contacteToRemove.value=contacte
        return true
    }

    // Métode pe eliminar un contacte de la llista

    // Modifiquem el model a través del repositori, i es notificarà a
    // l'adaptador per a que es refresque.
    fun removeContact(contacte:Contacte): Int{
        val index= ContacteRepository.getInstance(
            getApplication<Application>().applicationContext)
            .removeContacte(contacte)

        adaptador.value?.notifyItemRemoved(index)
        return index
    }


    // Mètode per guardar el contacte
    // rebem un contacte nou i retornarem un valor lògic que indica si
    // el contacte s'ha guardat correctament.
    fun guardaContacte(contacte: Contacte): Boolean{

        // Obtenim una referència al repositori
        val repositori=ContacteRepository.getInstance(getApplication<Application>()
            .applicationContext)

        // Comprovem si el contacte actual existeix o és nul
        if (_contacteActual.value !=null ) // Si existeix modifiquem
            repositori.replaceContacte(_contacteActual.value as Contacte, contacte)
        else repositori.addContacte(contacte)  // Si no, l'afegim

        // I actualitzem el contacte actual, fent una còpia del contacte
        _contacteActual.value=contacte.copy()

        // finalment, retornem true per indicar que el resultat és correcte
        return true
    }



        /*// Creem un nou contacte a partir de la informació de les vistes
        val nou= Contacte(
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
                Snackbar.LENGTH_LONG).setAction("Close") {
                findNavController().navigateUp()
            }.show()
        } else {
            // Si el contacte actual és nul, el que farem serà afegir un
            // nou contacte
            Contactes.add(nou)
            Snackbar.make(binding.root,
                "Contact has been Added successful",
                Snackbar.LENGTH_LONG).setAction("Close") {
                findNavController().navigateUp()
            }.show()

            // Actualitzem el contacte actual, per si ara es modifica
            contacteActual=nou
        }

    }*/
}