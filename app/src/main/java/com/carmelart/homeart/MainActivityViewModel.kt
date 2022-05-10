package com.carmelart.homeart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmelart.homeart.database.DataEntity

class MainActivityViewModel : ViewModel() {

    /**
     * Con by lazy indicamos que se inicializa más tarde cuando la variable es
     * de tipo val (inmutable), utilizamos lateinit para variable var (mutables),
     * dentro va el tipo.
     */
    val productList: MutableLiveData<List<DataEntity>> by lazy {
        MutableLiveData<List<DataEntity>>()
    }
    val isLoading = MutableLiveData<Boolean>()

    /**
     * Indicando el scope dónde se ejecuta la corrutina,
     * habitualmente IO para llamadas a internet y base de datos,
     * actualizamos la variable LiveData
     * con la petición que realizamos desde nuestro repositorio, tratando el resultado.
     * viewModelScope es un CoroutineScope predefinido que se incluye con las extensiones KTX de ViewModel.
     * Ten en cuenta que todas las corrutinas deben ejecutarse en un alcance.
     * launch es una función que crea una corrutina y despacha la ejecución de sus funciones al despachador correspondiente.
     */


    /**
     * Compartir datos entre fragmentos.
     */

    val num = MutableLiveData<String>()

    fun setNum(newNum: String) {
        num.value = newNum
    }

}