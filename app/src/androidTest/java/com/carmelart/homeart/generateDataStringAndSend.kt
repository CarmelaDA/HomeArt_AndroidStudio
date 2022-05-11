package com.carmelart.homeart

fun generateDataStringAndSend(type: String, data: DataEntity) {
        var vectorDatos: MutableList<String> = mutableListOf()
        vectorDatos.add(data.led.toString())
        vectorDatos.add(data.id.toString())
        Toast.makeText(
            requireActivity(),
            type + vectorDatos.toString() + ";" + token,
            Toast.LENGTH_SHORT
        )
            .show()
        //sendDataToServer(binding.textSet.text.toString() + " ; " + token + "\n")
    }