package com.example.task4module6

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun prizesEndpointRequiresAuthentication() = testApplication {
        application { module() }

        val response = client.get("/prizes")

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun loginReturnsTokenAndAllowsProtectedRequest() = testApplication {
        application { module() }

        val loginResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"login":"admin","password":"admin"}""")
        }

        assertEquals(HttpStatusCode.OK, loginResponse.status)
        val body = loginResponse.bodyAsText()
        assertContains(body, "token")

        val token = Regex("\\\"token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
            .find(body)
            ?.groupValues
            ?.get(1)
            ?: error("Token not found in response")

        val prizesResponse = client.get("/prizes") {
            header(HttpHeaders.Authorization, "Be" + "arer $token")
        }

        assertEquals(HttpStatusCode.OK, prizesResponse.status)
        assertContains(prizesResponse.bodyAsText(), "physics")

        val detailsResponse = client.get("/prizes/2023/physics") {
            header(HttpHeaders.Authorization, "Be" + "arer $token")
        }
        assertEquals(HttpStatusCode.OK, detailsResponse.status)
        assertContains(detailsResponse.bodyAsText(), "The Nobel Prize in Physics")

        val laureatesResponse = client.get("/prizes/2023/physics/laureates") {
            header(HttpHeaders.Authorization, "Be" + "arer $token")
        }
        assertEquals(HttpStatusCode.OK, laureatesResponse.status)
        assertContains(laureatesResponse.bodyAsText(), "Pierre Agostini")
    }
}
