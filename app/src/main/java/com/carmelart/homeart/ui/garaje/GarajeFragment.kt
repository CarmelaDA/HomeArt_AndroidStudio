package com.carmelart.homeart.ui.garaje

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentGarajeBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class GarajeFragment : Fragment() {

    private lateinit var garajeViewModel: GarajeViewModel
    private lateinit var dataGaraje: DataEntity
    private var _binding: FragmentGarajeBinding? = null
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
        garajeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(GarajeViewModel::class.java)

        _binding = FragmentGarajeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textIluminacionGarajeGaraje
        garajeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataGaraje = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE COCINA
        binding.switchIluminacionGeneralGaraje.isChecked = this.dataGaraje.luzGaraje == 1
        binding.switchPuertaGarajeGaraje.isChecked = this.dataGaraje.pGaraje == 1

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
            val activity: GarajeFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: GarajeFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: GarajeFragment = this
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

        binding.switchIluminacionGeneralGaraje.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGeneralGaraje.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataGaraje.luzGaraje = ledValue
            dbController.DataDAO().updateData(this.dataGaraje)
            this.generateDataStringAndSend('j', this.dataGaraje)
        }

        binding.switchPuertaGarajeGaraje.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaGarajeGaraje.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataGaraje.pGaraje = ledValue
            dbController.DataDAO().updateData(this.dataGaraje)
            this.generateDataStringAndSend('J', this.dataGaraje)
        }
    }

    private fun generateDataStringAndSend(zona: Char, data: DataEntity) {
        var vGaraje: MutableList<String> = mutableListOf()

        // Todos los datos de Cocina en el orden deseado
        vGaraje.add(data.luzGaraje.toString())
        vGaraje.add(data.pGaraje.toString())

        sendDataToServer("$zona;$vGaraje;$token\n") // zona[..., ..., ...] + token
    }
}