package com.aritra.goldmannasa.presentation.viewmodel.vm

import SharedTestUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.network.utils.Resource
import com.aritra.goldmannasa.data.remote.network.utils.Status
import com.aritra.goldmannasa.domain.usecase.ApodUsecase
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApodViewModelTest {

    private lateinit var mockUseCase: ApodUsecase

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockUseCase = Mockito.mock(ApodUsecase::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `call to getLestAPOD updates livedata`() = runTest {
        Mockito.`when`(mockUseCase.getLatestAPOD())
            .thenReturn(Resource.success(SharedTestUtils.dummyAPOD))
        val viewModel = ApodViewModel(mockUseCase)
        viewModel.getLatestAPOD()
        val listOfResponses = arrayListOf<Resource<APODEntity>>()
        viewModel.apod.observeForever {
            listOfResponses.add(it)
        }
        Truth.assertThat(listOfResponses).isNotEmpty()
        Truth.assertThat(listOfResponses[0].status).isEqualTo(Status.LOADING)
        Truth.assertThat(listOfResponses[1].status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `call to getDatedAPOD updates livedata`() = runTest {
        Mockito.`when`(mockUseCase.getDatedAPOD(SharedTestUtils.dummyDate))
            .thenReturn(Resource.success(SharedTestUtils.dummyAPOD))
        val viewModel = ApodViewModel(mockUseCase)
        viewModel.getDatedAPOD(SharedTestUtils.dummyDate)
        val listOfResponses = arrayListOf<Resource<APODEntity>>()
        viewModel.apod.observeForever {
            listOfResponses.add(it)
        }
        Truth.assertThat(listOfResponses).isNotEmpty()
        Truth.assertThat(listOfResponses[0].status).isEqualTo(Status.LOADING)
        Truth.assertThat(listOfResponses[1].status).isEqualTo(Status.SUCCESS)
    }

}