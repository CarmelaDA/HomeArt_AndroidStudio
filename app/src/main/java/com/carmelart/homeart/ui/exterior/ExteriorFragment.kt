package com.carmelart.homeart.ui.exterior

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentExteriorBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.tiempo.TiempoFragment
import com.carmelart.homeart.ui.ventanas.VentanasFragment

import java.io.DataOutputStream
import java.io.DataInputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class ExteriorFragment : Fragment() {

    private lateinit var exteriorViewModel: ExteriorViewModel
    private lateinit var dataExt: DataEntity
    private var _binding: FragmentExteriorBinding? = null
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
        exteriorViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ExteriorViewModel::class.java)
        _binding = FragmentExteriorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTendedero
        exteriorViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataExt = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE EXTERIOR
        binding.switchToldoTendederoExt.isChecked = this.dataExt.tTendedero == 1
        binding.switchIluminacionTendederoExt.isChecked = this.dataExt.luzTendedero == 1
        binding.switchIluminacionPorcheExt.isChecked = this.dataExt.luzPorche == 1
        binding.switchIluminacionJardinExt.isChecked = this.dataExt.luzJardin == 1
        binding.switchPuertaParcelaExt.isChecked = this.dataExt.pParcela == 1
        binding.switchModoExt.isChecked = this.dataExt.tenAuto == 1
        binding.switchModoIlumExt.isChecked = this.dataExt.luzAuto == 1

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
            val activity: ExteriorFragment = this
            socket.close()

            //receiveDataFromSTM()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: ExteriorFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: ExteriorFragment = this
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun bindingManagement() {

        // MODO ROPA TENDIDA
        binding.checkBoxRopa.setOnClickListener{

            var ropaValue = 0

            if(binding.checkBoxRopa.isChecked){

                Toast.makeText(
                    getActivity(), "Ropa tendida.",
                    Toast.LENGTH_SHORT
                ).show()

                ropaValue = 1
            }

            else{

                Toast.makeText(
                    getActivity(), "Ropa destendida.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            this.dataExt.tRopa = ropaValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        // MODO MANUAL/AUTOMÁTICO TENDEDERO
        binding.switchModoExt.setOnCheckedChangeListener { _, isChecked ->
            // Añadir lectura de LDR para encendido y apagado automático

            // Bloqueo de Switches
            binding.switchToldoTendederoExt.isEnabled = !isChecked

            Toast.makeText(
                getActivity(), "Toldo en función de la lluvia.",
                Toast.LENGTH_SHORT
            ).show()

            var tenValue = 0
            if (isChecked)
                tenValue = 1
            this.dataExt.tenAuto = tenValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        // MODO MANUAL/AUTOMÁTICO LUCES
        binding.switchModoIlumExt.setOnCheckedChangeListener { _, isChecked ->
            // Añadir lectura de LDR para encendido y apagado automático

            // Bloqueo de Switches
            binding.switchIluminacionJardinExt.isEnabled = !isChecked
            binding.switchIluminacionPorcheExt.isEnabled = !isChecked
            binding.switchIluminacionTendederoExt.isEnabled = !isChecked

            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataExt.luzAuto = ledValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        binding.switchToldoTendederoExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleToldoExt.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataExt.tTendedero = ventValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('R', this.dataExt)
        }

        binding.switchIluminacionTendederoExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionTendederoExt.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataExt.luzTendedero = ventValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        binding.switchIluminacionPorcheExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionPorcheExt.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataExt.luzPorche = ventValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        binding.switchIluminacionJardinExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionJardinExt.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataExt.luzJardin = ventValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('e', this.dataExt)
        }

        binding.switchPuertaParcelaExt.setOnCheckedChangeListener { _, isChecked ->
            binding.togglePuertaParcelaExt.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataExt.pParcela = ventValue
            dbController.DataDAO().updateData(this.dataExt)
            this.generateDataStringAndSend('P', this.dataExt)
        }
    }


    fun generateDataStringAndSend(zona: Char, data: DataEntity) {
        var vExt: MutableList<String> = mutableListOf()

        // Todos los datos de Exterior en el orden deseado
        vExt.add(data.tTendedero.toString())
        vExt.add(data.luzTendedero.toString())
        vExt.add(data.luzPorche.toString())
        vExt.add(data.luzJardin.toString())
        vExt.add(data.pParcela.toString())
        vExt.add(data.tenAuto.toString())
        vExt.add(data.tRopa.toString())

        sendDataToServer("$zona;$vExt;$token\n") // zona[..., ..., ...] + token
    }
}