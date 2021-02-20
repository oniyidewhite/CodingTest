package com.oblessing.app

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.oblessing.app.carlisting.database.CarCacheEntity
import com.oblessing.app.carlisting.database.CarCacheMapper
import com.oblessing.app.carlisting.database.CarDao
import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.network.CarListingWebService
import com.oblessing.app.carlisting.network.CarNetworkEntity
import com.oblessing.app.carlisting.network.NetworkMapper
import com.oblessing.app.carlisting.repository.CarListingRepository
import com.oblessing.app.core.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    lateinit var cacheMapper: CarCacheMapper
    lateinit var networkMapper: NetworkMapper
    lateinit var webService: CarListingWebService
    lateinit var carDao: CarDao

    @Before
    fun setUp() {
        cacheMapper = CarCacheMapper()
        networkMapper = NetworkMapper()
        webService = mock()
        carDao = mock()
    }

    @Test
    fun test_NetworkMapper_mapToEntity() {
        val mapper = NetworkMapper()

        var car = Car(
            id = 1,
            description = "none",
            price = 200.0,
            pictures = emptyList(),
            fuel = "gasoline",
            title = "Tesla S"
        )

        var entity = mapper.mapToEntity(car)

        assertEquals("ID isn't the same", entity.id, car.id)
        assertEquals("description isn't the same", entity.description, car.description)
        assertEquals("fuel type isn't the same", entity.fuel, car.fuel)
        assertEquals("Invalid title", "${entity.make} ${entity.model}", car.title)

        entity = CarNetworkEntity(
            id = 7,
            description = "🤔",
            price = 780.0,
            pictures = emptyList(),
            fuel = "petrol",
            model = "Nissan",
            make = "GT"
        )

        car = mapper.mapFromEntity(entity)

        assertEquals("ID isn't the same", entity.id, car.id)
        assertEquals("description isn't the same", entity.description, car.description)
        assertEquals("fuel type isn't the same", entity.fuel, car.fuel)
        assertEquals("Incorrect title", "${entity.make} ${entity.model}", car.title)
    }

    @Test
    fun test_CacheMapper_mapToEntity() {
        val mapper = CarCacheMapper()

        var car = Car(
            id = 1,
            description = "none",
            price = 200.0,
            pictures = emptyList(),
            fuel = "gasoline",
            title = "Tesla S"
        )

        var entity = mapper.mapToEntity(car)

        assertEquals("Incorrect id", entity.id, car.id)
        assertEquals("Incorrect description", entity.description, car.description)
        assertEquals("Incorrect fuel type", entity.fuel, car.fuel)
        assertEquals("Incorrect title", entity.title, car.title)


        entity = CarCacheEntity(
            id = 9,
            title = "Buggati verona",
            fuel = "petrol",
            pictures = emptyList(),
            price = 200.0,
            description = "none"
        )
        car = mapper.mapFromEntity(entity)

        assertEquals("ID isn't the same", entity.id, car.id)
        assertEquals("ID isn't the same", entity.description, car.description)
        assertEquals("ID isn't the same", entity.fuel, car.fuel)
        assertEquals("ID isn't the same", entity.title, car.title)
    }

    @InternalCoroutinesApi
    @Test
    fun test_CarListingRepo() = runBlocking<Unit> {
        val networkEntity = CarNetworkEntity(
            id = 7,
            description = "🤔",
            price = 780.0,
            pictures = emptyList(),
            fuel = "petrol",
            model = "Nissan",
            make = "GT"
        )
        whenever(webService.listCars()).thenReturn(listOf(networkEntity))
        whenever(carDao.get()).thenReturn(
            listOf(
                cacheMapper.mapToEntity(
                    networkMapper.mapFromEntity(
                        networkEntity
                    )
                )
            )
        )

        val repo = CarListingRepository(cacheMapper, networkMapper, webService, carDao)

        repo.cars(forceReload = true).collect(object : FlowCollector<DataState<List<Car>>> {
            // should send out loading before sending data or failure
            var receivedLoading = false
            override suspend fun emit(value: DataState<List<Car>>) {
                if (!receivedLoading) {
                    assert(value = value == DataState.Loading)
                    receivedLoading = true
                } else {
                    assertEquals(
                        (value is DataState.Success),
                        true
                    )
                    val car = (value as DataState.Success).data[0]
                    assertEquals("ID isn't the same", networkEntity.id, car.id)
                    assertEquals("ID isn't the same", networkEntity.description, car.description)
                    assertEquals("ID isn't the same", networkEntity.fuel, car.fuel)
                    assertEquals(
                        "ID isn't the same",
                        "${networkEntity.make} ${networkEntity.model}",
                        car.title
                    )
                }
            }
        })
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}