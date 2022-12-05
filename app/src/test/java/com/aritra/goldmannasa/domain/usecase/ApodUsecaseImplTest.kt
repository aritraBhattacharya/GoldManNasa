package com.aritra.goldmannasa.domain.usecase


import SharedTestUtils
import SharedTestUtils.Companion.dummyDate
import com.aritra.goldmannasa.data.remote.network.utils.Resource
import com.aritra.goldmannasa.data.remote.network.utils.Status
import com.aritra.goldmannasa.domain.repository.ApodRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class ApodUsecaseImplTest {

    @Mock
    lateinit var mockRepository: ApodRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getLatestAPOD_repository_returns_success() = runTest {
        Mockito.`when`(mockRepository.getLatestAPOD())
            .thenReturn(Resource.success(SharedTestUtils.dummyAPOD))
        val usecaseResponse = ApodUsecaseImpl(mockRepository).getLatestAPOD()
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(usecaseResponse.data).isNotNull()
    }

    @Test
    fun getLatestAPOD_repository_returns_failure() = runTest {
        Mockito.`when`(mockRepository.getLatestAPOD())
            .thenReturn(Resource.error(msg = "repository returns failure", data = null))
        val usecaseResponse = ApodUsecaseImpl(mockRepository).getLatestAPOD()
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.ERROR)
        Truth.assertThat(usecaseResponse.data).isNull()
    }

    @Test
    fun getDatedAPOD_repository_returns_success() = runTest {
        Mockito.`when`(mockRepository.getDatedAPOD(dummyDate))
            .thenReturn(Resource.success(SharedTestUtils.dummyAPOD))
        val usecaseResponse = ApodUsecaseImpl(mockRepository).getDatedAPOD(dummyDate)
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(usecaseResponse.data).isNotNull()
        Truth.assertThat(usecaseResponse.data!!.date).isEqualTo(dummyDate)
    }

    @Test
    fun getDatedAPOD_repository_returns_failure() = runTest {
        Mockito.`when`(mockRepository.getDatedAPOD(dummyDate))
            .thenReturn(Resource.error(msg = "no data found", data = null))
        val usecaseResponse = ApodUsecaseImpl(mockRepository).getDatedAPOD(dummyDate)
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.ERROR)
        Truth.assertThat(usecaseResponse.data).isNull()
    }

    @Test
    fun saveAPODToFavorites_works() = runTest {
        Mockito.`when`(mockRepository.saveAPODToFavorites(SharedTestUtils.dummyAPOD))
            .thenReturn(Resource.success(true))
        val usecaseResponse =
            ApodUsecaseImpl(mockRepository).saveAPODToFavorites(SharedTestUtils.dummyAPOD)
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(usecaseResponse.data).isTrue()
    }

    @Test
    fun saveAPODToFavorites_fails() = runTest {
        Mockito.`when`(mockRepository.saveAPODToFavorites(SharedTestUtils.dummyAPOD))
            .thenReturn(Resource.error(msg = "failed to update", false))
        val usecaseResponse =
            ApodUsecaseImpl(mockRepository).saveAPODToFavorites(SharedTestUtils.dummyAPOD)
        Truth.assertThat(usecaseResponse.status).isEqualTo(Status.ERROR)
        Truth.assertThat(usecaseResponse.data).isFalse()
    }


}