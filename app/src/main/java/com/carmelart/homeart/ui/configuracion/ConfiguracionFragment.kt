package com.carmelart.homeart.ui.configuracion

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker

import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.TimePicker

import com.carmelart.homeart.databinding.FragmentConfiguracionBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException


class ConfiguracionFragment() : Fragment() {

    private lateinit var ajustesViewModel: ConfiguracionViewModel
    private lateinit var dataAjustes: DataEntity
    private var _binding: FragmentConfiguracionBinding? = null
    private val binding get() = _binding!!
    private val timeout = 1000
    private val token = "fe5g8e2a5f4e85d2e85a7c5"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ajustesViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ConfiguracionViewModel::class.java)

        _binding = FragmentConfiguracionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        //val textView: TextView = binding.textConfiguracion
        //ajustesViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataAjustes = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE BAÑO


        // HORARIOS
        binding.editTextEncendidoAlarma.setOnClickListener{selectHoraEncendido()}
        binding.editTextApagadoAlarma.setOnClickListener{selectHoraApagado()}
        // HUMEDAD
        binding.pickerRhMinimo.minValue = 0
        binding.pickerRhMinimo.maxValue = 100
        binding.pickerRhMaximo.minValue = 0
        binding.pickerRhMaximo.maxValue = 100
        binding.pickerRhMinimo.value = 0
        binding.pickerRhMaximo.value = 100
        binding.pickerRhMinimo.setOnValueChangedListener { _, _, newVal ->
            selectMinHuerto(newVal)
        }
        binding.pickerRhMaximo.setOnValueChangedListener { _, _, newVal ->
            selectMaxHuerto(newVal)
        }

        return root
    }

    private fun sendDataToServer(message: String) {
        try {
            var address = "172.20.10.12"    // Actualizar de vez en cuando
            var port = 80

            // Se necesita de un HOST y un PORT, se conecta el SERVERPOCKET al puerto 7777
            val socket = Socket()
            socket.soTimeout = timeout
            socket.connect(InetSocketAddress(address, port), timeout)
            println("CONECTADO")

            // Obtiene el flujo de salida del SOCKET
            val outputStream = socket.getOutputStream()

            // Crea un flujo de salida para sacar los datos
            val dataOutputStream = DataOutputStream(outputStream)
            println("Enviando cadena de datos por el ServerSocket")

            // Escribe el MENSAJE que se quiere enviar
            dataOutputStream.writeUTF(message)
            dataOutputStream.flush() // Envía el mensaje
            dataOutputStream.close() // Cierra el final del flujo de salida cuando se termina

            println("Cerrando socket")
            val activity: ConfiguracionFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: ConfiguracionFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: ConfiguracionFragment = this
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindingManagement() {

        /*binding.editTextMinimoHuerto.setOnClickListener{
            /*val min = binding.editTextMinimoHuerto.text.toString().toInt()
            //binding.editTextMinimoHuerto.setText(" $min %")
            this.dataAjustes.hMinHuerto = min
            dbController.DataDAO().updateData(this.dataAjustes)*/
        }


        binding.editTextMaximoHuerto.setOnClickListener{
            /*val max = binding.editTextMaximoHuerto.text.toString().toInt()
            this.dataAjustes.hMaxHuerto = max
            dbController.DataDAO().updateData(this.dataAjustes)*/
        }*/



        /*binding.switchIluminacionGeneralBano.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGeneralBano.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataBano.luzBano = ledValue
            dbController.DataDAO().updateData(this.dataBano)
            this.generateDataStringAndSend(this.dataBano)
        }

        binding.switchIluminacionEspejoBano.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionEspejoBano.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataBano.luzEspejo = ledValue
            dbController.DataDAO().updateData(this.dataBano)
            this.generateDataStringAndSend(this.dataBano)
        }*/
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vAjustes: MutableList<String> = mutableListOf()

        // Todos los datos de Baño en el orden deseado
        vAjustes.add(data.luzBano.toString())
        //vBano.add(data.luzEspejo.toString())

        sendDataToServer("a;$vAjustes;$token\n") // a[..., ..., ...] + token
    }

    // HORARIOS
    private fun selectHoraEncendido(){
        val hora = TimePicker{mostrarHoraEncendido(it)}
        hora.show(parentFragmentManager, "TimePicker")
    }
    private fun mostrarHoraEncendido(time: String){
        binding.editTextEncendidoAlarma.setText(time)
        this.dataAjustes.tEncendidoAlarma = time
    }
    private fun selectHoraApagado(){
        val hora = TimePicker{mostrarHoraApagado(it)}
        hora.show(parentFragmentManager, "TimePicker")
    }
    private fun mostrarHoraApagado(time: String){
        binding.editTextApagadoAlarma.setText(time)
        this.dataAjustes.tApagadoAlarma = time
    }

    // HUERTO
    private fun selectMinHuerto(nVal: Int){
        val min = binding.pickerRhMinimo.value
        val max = binding.pickerRhMaximo.value
        if(min>=max){
            Toast.makeText(
                activity, "%RH MÍNIMO debe ser MENOR que %RH MÁXIMO",
                Toast.LENGTH_SHORT
            ).show()
            binding.pickerRhMinimo.value = 0
            binding.pickerRhMaximo.value = 100
        }
        this.dataAjustes.rhMinHuerto = nVal
    }
    private fun selectMaxHuerto(nVal: Int){
        val min = binding.pickerRhMinimo.value
        val max = binding.pickerRhMaximo.value
        if(min>=max){
            Toast.makeText(
                activity, "%RH MÍNIMO debe ser MENOR que %RH MÁXIMO",
                Toast.LENGTH_SHORT
            ).show()
            binding.pickerRhMinimo.value = 0
            binding.pickerRhMaximo.value = 100
        }
        this.dataAjustes.rhMinHuerto = nVal
    }

}