import com.aritra.goldmannasa.data.local.db.ApodDao
import com.aritra.goldmannasa.data.local.entities.APODEntity
import com.aritra.goldmannasa.data.remote.dtos.APODDto

class SharedTestUtils {

    companion object {
        val dummyAPOD: APODEntity = APODEntity(date = "20-12-2021",
            explanation = "explanation",
            title = "title",
            url = "url",
            hdurl = "hdUrl",
            media_type = "image",
            service_version = "v1",
            isFavourite = false)
        val dummyAPODDto: APODDto = APODDto(date = "20-12-2021",
            explanation = "explanation",
            title = "title",
            url = "url",
            hdurl = "hdUrl",
            media_type = "image",
            service_version = "v1")
        val dummyDate: String = "2022-12-12"

        val fakeDaoWithData = object : ApodDao {
            override suspend fun insertAPOD(apodEntity: APODEntity) {
                //stub
            }

            override suspend fun getAllLocalAPOD(): List<APODEntity> {
                return listOf(dummyAPOD)
            }

            override suspend fun getLocalAPODForDate(date: String): List<APODEntity> {
                return listOf(dummyAPOD)
            }

            override suspend fun makeAPODFavourite(date: String, isFavourite: Boolean) {
                //stub
            }

        }

        val fakeDaoWithNoData = object : ApodDao {
            override suspend fun insertAPOD(apodEntity: APODEntity) {
                //stub
            }

            override suspend fun getAllLocalAPOD(): List<APODEntity> {
                return listOf()
            }

            override suspend fun getLocalAPODForDate(date: String): List<APODEntity> {
                return listOf()
            }

            override suspend fun makeAPODFavourite(date: String, isFavourite: Boolean) {
                //stub
            }

        }
    }

}