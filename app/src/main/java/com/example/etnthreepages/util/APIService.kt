package com.example.etnthreepages.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// APIService is an object class that provides a singleton instance of Retrofit
// It is used to create and configure an instance if the APIConsumer interface for network operations
object APIService {

    // The base URL of the API server; this will be the root URL for all API calls
    private const val BASE_URL = "base_url"

    // Function to return an instance of APIConsumer
    fun getService(): APIConsumer{

        // Configure the okHttpClient with timeout settings
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        // Build the Retrofit instance with the base URL, HTTP client, and Gson converter
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)              // Set the base URL for the API
            .client(client)                 // Attach the configured OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Add a converter to the handle JSON responses

        // Create the Retrofit object from the builder configuration
        val retrofit: Retrofit = builder.build()

        // Create and return an implementation of the APIConsumer interface using Retrofit
        return retrofit.create(APIConsumer::class.java)
    }
}