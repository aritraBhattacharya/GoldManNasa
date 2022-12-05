package com.aritra.goldmannasa.data.remote

import com.aritra.goldmannasa.data.remote.network.NasaApi
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val INVALID_DATE = "10-10-2023"
private const val CODE_SUCCESS = 201
private const val CODE_BAD_REQUEST = 400

@RunWith(JUnit4::class)
class NasaApiTest {
    private lateinit var nasaApi: NasaApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        nasaApi = Retrofit.Builder().baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create()).build().create(NasaApi::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    private fun enqueueMockResponse(fineName: String, responseCode: Int) {
        val inputStream =
            javaClass.classLoader!!.getResourceAsStream(fineName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        mockResponse.setResponseCode(responseCode)
        mockWebServer.enqueue(mockResponse)
    }

    @Test
    fun server_returns_successful_result() {
        runTest {
            enqueueMockResponse("FakeResponse.json", CODE_SUCCESS)
            val response = nasaApi.getLatestAPOD()
            assertThat(response.body()).isNotNull()
            assertThat(response.code()).isEqualTo(CODE_SUCCESS)
        }
    }

    @Test
    fun server_returns_failure_due_to_bad_request() {
        runTest {
            enqueueMockResponse("FakeErrorResponse.json", CODE_BAD_REQUEST)
            val response = nasaApi.getDatedAPOD(date = INVALID_DATE)
            assertThat(response.code()).isEqualTo(CODE_BAD_REQUEST)
        }
    }


}