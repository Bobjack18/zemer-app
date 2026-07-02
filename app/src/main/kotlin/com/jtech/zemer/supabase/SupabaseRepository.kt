package com.jtech.zemer.supabase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseRepository @Inject constructor() {
    private val supabaseUrl = "https://grkgzvkmqmnbufbvdube.supabase.co"
    private val serviceRoleKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imdya2d6dmttcW1uYnVmYnZkdWJlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0NzY4ODE2MCwiZXhwIjoyMDYzMjY0MTYwfQ.5BhtRJD8vX7hB7SkljF9p5y1qBqVyLDNWOBJ1Y_x7ds"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getTrendingWithComparison(): List<TrendingWithComparisonRow> {
        return client.post("$supabaseUrl/rest/v1/rpc/get_trending_with_comparison") {
            header("apikey", serviceRoleKey)
            header("Authorization", "Bearer $serviceRoleKey")
            contentType(ContentType.Application.Json)
            setBody(mapOf("p_limit" to 100))
        }.body()
    }
}
