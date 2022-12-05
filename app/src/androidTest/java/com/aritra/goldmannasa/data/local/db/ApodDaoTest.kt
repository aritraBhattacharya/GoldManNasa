package com.aritra.goldmannasa.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ApodDaoTest {

    private lateinit var dao: ApodDao
    private lateinit var db: ApodDB

    @Before
    public fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApodDB::class.java).allowMainThreadQueries().build()
        dao = db.getApodDao()
    }

    @After
    public fun tearDown() {
        db.close()
    }

    @Test
    public fun insertAPOD_test() = runTest {
        val apodEntity = APODEntity(date = "20-12-2021",
            explanation = "explanation",
            title = "title",
            url = "url",
            hdurl = "hdUrl",
            media_type = "image",
            service_version = "v1",
            isFavourite = false)
        dao.insertAPOD(apodEntity)
        val localList = dao.getAllLocalAPOD()
        assertThat(localList).isNotEmpty()
        assertThat(localList).contains(apodEntity)
    }

    @Test
    public fun getLocalAPODForDate_test() = runTest {
        val apodEntity = APODEntity(date = "20-12-2021",
            explanation = "explanation",
            title = "title",
            url = "url",
            hdurl = "hdUrl",
            media_type = "image",
            service_version = "v1",
            isFavourite = false)
        dao.insertAPOD(apodEntity)
        val localList = dao.getLocalAPODForDate("20-12-2021")
        assertThat(localList).isNotEmpty()
        assertThat(localList[0].date).isEqualTo("20-12-2021")
    }

    @Test
    public fun makeAPODFavourite_test() = runTest {
        val apodEntity = APODEntity(date = "20-12-2021",
            explanation = "explanation",
            title = "title",
            url = "url",
            hdurl = "hdUrl",
            media_type = "image",
            service_version = "v1",
            isFavourite = false)
        dao.insertAPOD(apodEntity)
        dao.makeAPODFavourite("20-12-2021", true)
        val localList = dao.getLocalAPODForDate("20-12-2021")
        assertThat(localList).isNotEmpty()
        assertThat(localList[0].isFavourite).isTrue()
    }
}