package com.ieseljust.pmdm.incidenciesv2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ieseljust.pmdm.apac2.AdaptadorIncidencies
import com.ieseljust.pmdm.incidenciesv2.IncidenciesDB
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia
import com.ieseljust.pmdm.incidenciesv2.repository.IncidenciesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncidenciesViewModel(application: Application): AndroidViewModel(application) {

    /* Propietats del ViewModel */

    val repository=IncidenciesRepository.getInstance(application.applicationContext)

    /* Defininció de LiveData */

    // Incidència en edició
    private val _incidenciaEnEdicio= MutableLiveData<Incidencia?>()
    val incidenciaEnEdicio: LiveData<Incidencia?> = _incidenciaEnEdicio

    fun setIncidenciaEnEdicio(incidencia: Incidencia){
        _incidenciaEnEdicio.value=incidencia.copy()
    }
    fun cleanIncidenciaEnEdicio(){
        _incidenciaEnEdicio.value = null
    }

    // Livedata per emmagatzemar temporalment la incidencia a eliminar
    val incidenciaToRemove: MutableLiveData<Incidencia> by lazy {
        MutableLiveData<Incidencia>()
    }

    // Livedata per a la llista d'incidències
    val incidenciaList: LiveData<List<Incidencia>> by lazy {
        repository.getIncidencies()
    }

    // Livedatas per avisar a la interfície quan s'ha fet clic o clic llarg
    // sobre una targeta. Serà el ViewHolder qui detecte estos events,
    // i els callbacks que definirem aci (ClickedManager i LongClickedManager)
    // actualitzaran estos Livedata per notificar a la interfície
    val incidenciaClicked: MutableLiveData<Incidencia> by lazy {
        MutableLiveData<Incidencia>()
    }
    val incidenciaLongClicked: MutableLiveData<Incidencia> by lazy {
        MutableLiveData<Incidencia>()
    }



    // Livedatas per avisar a la interfície quan es produeix alguna
    // operació CRUD
    var incidenciaSaved: MutableLiveData<Boolean> = MutableLiveData()
    var incidenciaUpdated: MutableLiveData<Boolean> = MutableLiveData()
    var deletedPos: MutableLiveData<Int> = MutableLiveData()



    /// Adaptador per al RecyclerView
    private val _adaptador = MutableLiveData<AdaptadorIncidencies>().apply{

        // Mitjançant value (setValue), establim el valor que contindrà el MutableLibeData
        // de tipus AdaptadorIncidencies. Aci també proporcionarem els callbacks que
        // s'han d'invocar quan es produisquen els clicks.

        value= AdaptadorIncidencies(
            { incidencia: Incidencia -> IncidenciaClickedManager(incidencia)},
            { incidencia: Incidencia -> IncidenciaLongClickedManager(incidencia)},
            this@IncidenciesViewModel)
    }

    // Definim la propietat per a l'adaptador, que podrem llegir des
    // de la vista (ja que _adapter és un atribut de suport privat)
    val adaptador:MutableLiveData<AdaptadorIncidencies> =_adaptador


    /* Definició de mètodes */

    // Mètodes per gestionar els clicks
    // (proporcionats com a callbacks a l'adaptador)
    fun IncidenciaClickedManager(incidencia:Incidencia){
        // Clic en Incidencia. Notifiquem a la interfície amb el LiveData
        incidenciaClicked.value=incidencia
        _incidenciaEnEdicio.value=incidencia
    }

    fun IncidenciaLongClickedManager(incidencia:Incidencia):Boolean{
        // Clic llarg Incidencia. Notifiquem a la interfície amb el LiveData
        incidenciaLongClicked.value=incidencia
        incidenciaToRemove.value=incidencia

        return true // Per a que no implique un click curt
    }

    // Mètodes per gestionar les operacions CRUD
    fun storeOrUpdateIncidencia(incidencia:Incidencia){
        viewModelScope.launch(Dispatchers.IO) {
            // Comprovem si s'estava editant o és una nova incidència
            if (_incidenciaEnEdicio.value != null) { // Si existeix fem l'updte
                Log.d("DebugMe", _incidenciaEnEdicio.value.toString())
                repository.updateIncidencia(_incidenciaEnEdicio.value as Incidencia)
                // Activem l'indicador incidenciaUpdated per notificat a la UI
                incidenciaUpdated.postValue(true)
            }
            else { // Si no, l'afegim
                repository.addIncidencia(incidencia)
                // Activem l'indicador contacteSavedm per notificat a la
                // interfície que s'ha afegit un element
                incidenciaSaved.postValue(true)
            }

            // Actualitzem la incidència en edició
            _incidenciaEnEdicio.postValue(incidencia.copy())

            // Notifiquem a l'adaptador que s'ha modificat un element
            /*incidenciaList.value.indexOf(_incidenciaEnEdicio.value)
                ?.let { adaptador.value?.notifyItemChanged(it) }
            // Utilitzem açò en lloc del adaptador.value?.notifyDataSetChanged()
            //  (per eficiència i suggerència de l'IDE)*/
        }
    }

    fun removeIncidencia(it: Incidencia) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteIncidencia(it)
            incidenciaList.value?.indexOf(it)?.let {
                deletedPos.postValue(it)
            }
        }

    }



}