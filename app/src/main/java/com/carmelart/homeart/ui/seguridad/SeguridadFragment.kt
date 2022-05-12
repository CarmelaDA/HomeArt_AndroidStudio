package com.carmelart.homeart.ui.seguridad

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Switch
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.provider.CalendarContract
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentSeguridadBinding
import androidx.appcompat.app.AppCompatActivity
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.iluminacion.IluminacionFragment
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class SeguridadFragment : Fragment() {

    private lateinit var seguridadViewModel: SeguridadViewModel
    private lateinit var dataSeg: DataEntity
    private var _binding: FragmentSeguridadBinding? = null
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
        seguridadViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(SeguridadViewModel::class.java)
        _binding = FragmentSeguridadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSeguridad
        seguridadViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // Actualizar data
        this.dataSeg = dbController.DataDAO().getData()
        // Todos las variables de Seguridad
        binding.switchSeguridadInt.isChecked = this.dataSeg.segInt == 1
        binding.switchSeguridadExt.isChecked = this.dataSeg.segExt == 1
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val activity: SeguridadFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: SeguridadFragment = this
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun bindingManagement() {

        binding.switchSeguridadInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadInt.isChecked = isChecked
            var segValue = 0
            if (isChecked)
                segValue = 1
            this.dataSeg.segInt = segValue
            dbController.DataDAO().updateData(this.dataSeg)
            this.generateDataStringAndSend(this.dataSeg)
        }

        binding.switchSeguridadExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadExt.isChecked = isChecked
            var segValue = 0
            if (isChecked)
                segValue = 1
            this.dataSeg.segExt = segValue
            dbController.DataDAO().updateData(this.dataSeg)
            this.generateDataStringAndSend(this.dataSeg)
        }
    }

    fun generateDataStringAndSend(data: DataEntity) {
        var vSeg: MutableList<String> = mutableListOf()
        // Todos los datos de Iluminación en el orden deseado
        vSeg.add(data.segInt.toString())
        vSeg.add(data.segExt.toString())
        //vectorDatos.add(data.id.toString())
        /*Toast.makeText(
            requireActivity(),
            type + vectorDatos.toString(),
            Toast.LENGTH_SHORT
        )
            .show()*/
        sendDataToServer("a;$vSeg;$token\n") // a[..., ..., ...] + token
    }
}