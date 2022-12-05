package com.aritra.goldmannasa.domain.repository

import SharedTestUtils
import com.aritra.goldmannasa.data.remote.NetworkUtils
import com.aritra.goldmannasa.data.remote.network.NasaApi
import com.aritra.goldmannasa.data.remote.network.utils.Status
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
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

    @Test
    fun `getDatedAPOD_no_local_data_no_network_active_returns_error`() = runTest {


        `when`(mockAPI.getDatedAPOD(date = anyString(), api_key = anyString())).thenReturn(
            Response.error(501, null))
        `when`(mockNetworkUtils.getConnectivityStatus()).thenReturn(false)


        val testRepo =
            ApodRepositoryImpl(mockAPI, SharedTestUtils.fakeDaoWithNoData, mockNetworkUtils)
        val repoResponse = testRepo.getDatedAPOD(SharedTestUtils.dummyDate)
        Truth.assertThat(repoResponse.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun getDatedAPOD() {
    }

    @Test
    fun saveAPODToFavorites() {
    }


}