package com.carmelart.homeart.ui.cocina

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

import com.carmelart.homeart.databinding.FragmentCocinaBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.FragmentSalonBinding
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.salon.SalonFragment
import com.carmelart.homeart.ui.salon.SalonViewModel

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class CocinaFragment : Fragment() {

    private lateinit var cocinaViewModel: CocinaViewModel
    private lateinit var dataCocina: DataEntity
    private var _binding: FragmentCocinaBinding? = null
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
        cocinaViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(CocinaViewModel::class.java)

        _binding = FragmentCocinaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textIluminacionCocina
        cocinaViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataCocina = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE ILUMINACIÓN
        binding.switchIluminacionGeneralCocina.isChecked = this.dataCocina.luzCocina == 1
        binding.switchIluminacionFregaderoCocina.isChecked = this.dataCocina.luzFregadero == 1

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
            val activity: CocinaFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: CocinaFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: CocinaFragment = this
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

        binding.switchIluminacionGeneralCocina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGeneralCocina.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataCocina.luzCocina = ledValue
            dbController.DataDAO().updateData(this.dataCocina)
            this.generateDataStringAndSend(this.dataCocina)
        }

        binding.switchIluminacionFregaderoCocina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionFregaderoCocina.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataCocina.luzFregadero = ledValue
            dbController.DataDAO().updateData(this.dataCocina)
            this.generateDataStringAndSend(this.dataCocina)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vCocina: MutableList<String> = mutableListOf()

        // Todos los datos de Iluminación en el orden deseado
        vCocina.add(data.luzCocina.toString())
        vCocina.add(data.luzFregadero.toString())

        sendDataToServer("c;$vCocina;$token\n") // c[..., ..., ...] + token
    }
}