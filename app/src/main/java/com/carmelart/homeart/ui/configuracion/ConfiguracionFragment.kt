package com.carmelart.homeart.ui.configuracion

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
//import android.widget.TimePicker
import android.widget.Toast
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
import javax.net.ssl.ManagerFactoryParameters

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
        //binding.switchIluminacionGeneralBano.isChecked = this.dataBano.luzBano == 1
        //binding.switchIluminacionEspejoBano.isChecked = this.dataBano.luzEspejo == 1

        binding.editTextEncendidoAlarma.setOnClickListener{selectHoraEncendido()}
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

    private fun selectHoraEncendido(){
        val hora = TimePicker{mostrarResultado(it)}
        hora.show(parentFragmentManager, "TimePicker")
    }

    private fun mostrarResultado(time: String){
        Toast.makeText(
            activity, "$time h",
            Toast.LENGTH_SHORT
        ).show()
        binding.editTextEncendidoAlarma.setText(time)
    }
}