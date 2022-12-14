package com.aritra.goldmannasa.domain.repository

import SharedTestUtils
import com.aritra.goldmannasa.data.remote.NetworkUtils
import com.aritra.goldmannasa.data.remote.dtos.APODDto
import com.aritra.goldmannasa.data.remote.network.NasaApi
import com.aritra.goldmannasa.data.remote.network.utils.Status
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import retrofit2.Response


class ApodRepositoryImplTest {
    @Mock
    lateinit var mockAPI: NasaApi

    @Mock
    lateinit var mockNetworkUtils: NetworkUtils


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getDatedAPOD_no_local_data_network_active_returns_result_from_server`() = runTest {


        `when`(mockAPI.getDatedAPOD(date = anyString(), api_key = anyString())).thenReturn(
            Response.success(SharedTestUtils.dummyAPODDto))
        `when`(mockNetworkUtils.getConnectivityStatus()).thenReturn(true)


        val testRepo =
            ApodRepositoryImpl(mockAPI, SharedTestUtils.fakeDaoWithData, mockNetworkUtils)
        val repoResponse = testRepo.getDatedAPOD(SharedTestUtils.dummyDate)
        Truth.assertThat(repoResponse.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(repoResponse.data).isNotNull()
        Truth.assertThat(repoResponse.data!!.title).isEqualTo(SharedTestUtils.dummyAPODDto.title)

    }

}