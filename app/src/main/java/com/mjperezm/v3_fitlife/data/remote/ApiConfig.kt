package com.mjperezm.v3_fitlife.data.remote

object ApiConfig {
    // ⚠️ REEMPLAZA CON TU URL DE RENDER
    // Ejemplo: https://fitlife-api-xxxx.onrender.com
    const val BASE_URL = "https://fitlife-api-v2-1.onrender.com/api/"

    // Si tu API está en el puerto 3008 como en el Dockerfile:
    // const val BASE_URL = "https://fitlife-api-xxxx.onrender.com:3008/api/"

    // Timeouts
    const val CONNECT_TIMEOUT = 30L // segundos
    const val READ_TIMEOUT = 30L // segundos
    const val WRITE_TIMEOUT = 30L // segundos
}