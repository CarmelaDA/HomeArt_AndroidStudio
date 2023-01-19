package com.carmelart.homeart.ui.huerto

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentHuertoBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.tiempo.TiempoFragment

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class HuertoFragment : Fragment() {

    private lateinit var huertoViewModel: HuertoViewModel
    private lateinit var dataHuerto: DataEntity
    private var _binding: FragmentHuertoBinding? = null
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
        huertoViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HuertoViewModel::class.java)

        _binding = FragmentHuertoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHuertoHuerto
        huertoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataHuerto = dbController.DataDAO().getData()
        // TODAS LA VARIABLES DE HUERTO
        binding.switchRiegoHuertoHuerto.isChecked = this.dataHuerto.rHuerto == 1

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
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            Toast.makeText(
                activity, "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                activity, "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindingManagement() {

        // MODO MANUAL/AUTOMÁTICO
        binding.switchModoHuerto.setOnCheckedChangeListener { _, isChecked ->

            if (binding.switchModoHuerto.isChecked){
                val min = this.dataHuerto.rhMinHuerto
                val max = this.dataHuerto.rhMaxHuerto

                if((!this.dataHuerto.bMinHuerto)||(!this.dataHuerto.bMaxHuerto)){
                    Toast.makeText(
                        activity, "Se requieren acciones en la configuración.",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.switchModoHuerto.isChecked = false
                }
                else {
                    Toast.makeText(
                        activity, "Humedad deseada:\n $min% - $max%",
                        Toast.LENGTH_LONG
                    ).show()
                    Toast.makeText(
                        activity, "Consulte la configuración del huerto para una mejor experiencia.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            // Bloqueo de Switches
            binding.switchRiegoHuertoHuerto.isEnabled = !isChecked
        }

        binding.switchRiegoHuertoHuerto.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleRiegoHuertoHuerto.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataHuerto.rHuerto = ledValue
            dbController.DataDAO().updateData(this.dataHuerto)
            this.generateDataStringAndSend(this.dataHuerto)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vHuerto: MutableList<String> = mutableListOf()

        // Todos los datos de Huerto en el orden deseado
        vHuerto.add(data.rHuerto.toString())

        sendDataToServer("h;$vHuerto;$token\n") // h[..., ..., ...] + token
    }
}