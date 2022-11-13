package com.ieseljust.pmdm.contactes.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ieseljust.pmdm.contactes.model.db.Contacte
import com.ieseljust.pmdm.contactes.repository.ContacteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Definim el ViewModel com a AndroidViewModel, per tal de poder accedir al context
class AppContactesViewModel(application: Application): AndroidViewModel(application) {

    // Definición d'atributs

    // Referència al repositori
    val repository = ContacteRepository.getInstance(application.applicationContext)

    // Contacte que s'està editant actualment
    // Observeu que el contacte pot ser nul

    private val _contacteActual=MutableLiveData<Contacte?>() // Atribut de suport privat
    val contacteActual: LiveData<Contacte?> = _contacteActual // Accés públic

    // Setter
    fun setContacteActual(contacte: Contacte){
        _contacteActual.value=contacte.copy()
    }

    // Setter a null
    fun cleanContacteActual(){
        _contacteActual.value = null
    }



    // Livedata per a la llista de contactes
    val contacteList: LiveData<List<Contacte>> by lazy {
        Log.d("A", "AAAAA");
        repository.getContactes()

    }
    //val contacteList: LiveData<List<Contacte>>

    // Livedata per comunicar-se amb la interfície en resposta a insercions,
    // actualitzacions o esborrats de la base de dades.
    var contacteSaved: MutableLiveData<Boolean> = MutableLiveData()
    var contacteUpdated: MutableLiveData<Boolean> = MutableLiveData()
    var deletedPos: MutableLiveData<Int> = MutableLiveData()


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
            { contacte: Contacte, v: View -> ContacteClickedManager(contacte, v)},
            { contacte: Contacte, v: View -> ContacteLongClickedManager(contacte, v)},
            this@AppContactesViewModel
            //getApplication<Application>().applicationContext // Açò ho podem fer gràcies a derivar d'AndroidViewModel
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

    private fun ContacteLongClickedManager(contacte: Contacte, v: View):Boolean {
        // Per establir el valor del liveData fem ús de la propietat value
        contacteLongClicked.value=contacte
        // Assignem també el LiveData contacteToRemove
        contacteToRemove.value=contacte
        return true
    }

    fun removeContact(contacte: Contacte) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeContacte(contacte)
            contacteList.value?.indexOf(contacte)?.let {
                deletedPos.postValue(it);
            }
        }
    }

    // Mètode per guardar el contacte
    // rebem un contacte nou i retornarem un valor lògic que indica si
    // el contacte s'ha guardat correctament.
    fun guardaContacte(contacte: Contacte){
        Log.d("Debug meu", "333333333333333333");
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("Debug meu 44 ", _contacteActual.value.toString());
            Log.d("Debug meu 55 ", contacte.toString());


            // Comprovem si el contacte actual existeix o és nul
            if (_contacteActual.value != null) { // Si existeix fem l'updte
                repository.updateContacte(_contacteActual.value as Contacte)

                // Indiquem a l'adaptador que s'ha modificat l'element
                contacteList.value?.indexOf(_contacteActual.value)
                    ?.let { adaptador.value?.notifyItemChanged(it) }

                // Activamos el indicador parkUpdate para notificar a la interfaz
                contacteUpdated.postValue(true)
            }
            else repository.addContacte(contacte)  // Si no, l'afegim
            // I actualitzem el contacte actual, fent una còpia del contacte
            _contacteActual.postValue(contacte.copy())

            // Notifiquem a l'adaptador que s'ha modificat el dataset
            adaptador.value?.notifyDataSetChanged()

            // Activem l'indicador contacteSavedm per notificat a la
            // interfície que s'ha afegit un element
            contacteSaved.postValue(true)

        }
    }
}