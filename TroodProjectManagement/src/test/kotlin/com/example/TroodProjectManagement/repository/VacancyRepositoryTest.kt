package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Vacancy
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.quality.Strictness
import org.mockito.junit.jupiter.MockitoSettings
import java.util.concurrent.ExecutionException

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VacancyRepositoryTest {

    @Mock
    private lateinit var firestore: Firestore

    @Mock
    private lateinit var collectionReference: CollectionReference

    @Mock
    private lateinit var documentReference: DocumentReference

    @Mock
    private lateinit var querySnapshot: QuerySnapshot

    @Mock
    private lateinit var documentSnapshot: DocumentSnapshot

    @Mock
    private lateinit var apiFutureQuerySnapshot: ApiFuture<QuerySnapshot>

    @Mock
    private lateinit var apiFutureDocumentSnapshot: ApiFuture<DocumentSnapshot>

    @Mock
    private lateinit var apiFutureWriteResult: ApiFuture<WriteResult>

    private lateinit var repository: VacancyRepository

    private val testVacancy = Vacancy(
        id = "1",
        name = "Software Engineer",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Great job",
        projectId = "123"
    )

    @BeforeEach
    fun setup() {
        repository = VacancyRepository(firestore)

        `when`(firestore.collection("vacancies")).thenReturn(collectionReference)
        `when`(collectionReference.document(anyString())).thenReturn(documentReference)

        // Добавляем мок для whereEqualTo
        `when`(collectionReference.whereEqualTo(anyString(), anyString()))
            .thenReturn(mock(Query::class.java))  // Мокируем Query
    }


    @Test
    fun `should return all vacancies for a project`() {
        val queryDocumentSnapshot = mock(QueryDocumentSnapshot::class.java)

        `when`(queryDocumentSnapshot.toObject(Vacancy::class.java)).thenReturn(testVacancy)
        `when`(queryDocumentSnapshot.id).thenReturn("1")

        `when`(querySnapshot.documents).thenReturn(listOf(queryDocumentSnapshot))

        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.whereEqualTo("projectId", "123").get()).thenReturn(apiFutureQuerySnapshot)

        val result = repository.getVacanciesByProjectId("123").get()

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Software Engineer", result[0].name)
    }

    @Test
    fun `should return empty list when no vacancies found`() {
        `when`(querySnapshot.documents).thenReturn(emptyList())
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.whereEqualTo("projectId", "123").get()).thenReturn(apiFutureQuerySnapshot)

        val result = repository.getVacanciesByProjectId("123").get()

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return vacancy by ID`() {
        `when`(documentSnapshot.toObject(Vacancy::class.java)).thenReturn(testVacancy)
        `when`(documentSnapshot.id).thenReturn("1")

        `when`(apiFutureDocumentSnapshot.get()).thenReturn(documentSnapshot)
        `when`(documentReference.get()).thenReturn(apiFutureDocumentSnapshot)

        val result = repository.getVacancyById("1").get()

        assertNotNull(result)
        assertEquals("1", result?.id)
        assertEquals("Software Engineer", result?.name)
    }

    @Test
    fun `should return null when vacancy not found`() {
        `when`(documentSnapshot.toObject(Vacancy::class.java)).thenReturn(null)
        `when`(apiFutureDocumentSnapshot.get()).thenReturn(documentSnapshot)
        `when`(documentReference.get()).thenReturn(apiFutureDocumentSnapshot)

        val result = repository.getVacancyById("non-existent-id").get()

        assertNull(result)
    }

    @Test
    fun `should create a new vacancy with structured ID`() {
        `when`(querySnapshot.documents).thenReturn(emptyList())
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        `when`(documentReference.set(any(Vacancy::class.java)))
            .thenReturn(apiFutureWriteResult)

        val result = repository.createVacancy(testVacancy).get()

        assertNotNull(result)
        assertTrue(result.startsWith("vacancy-"))
    }

    @Test
    fun `should update vacancy successfully`() {
        `when`(documentReference.set(any(Vacancy::class.java)))
            .thenReturn(apiFutureWriteResult)

        val future = repository.updateVacancy("1", testVacancy)

        assertDoesNotThrow { future.get() }
    }

    @Test
    fun `should delete vacancy successfully`() {
        `when`(documentReference.delete())
            .thenReturn(apiFutureWriteResult)

        val future = repository.deleteVacancy("1")

        assertDoesNotThrow { future.get() }
    }

    @Test
    fun `should generate next vacancy ID correctly`() {
        val doc1 = mock(QueryDocumentSnapshot::class.java)
        val doc2 = mock(QueryDocumentSnapshot::class.java)

        `when`(doc1.id).thenReturn("vacancy-1")
        `when`(doc2.id).thenReturn("vacancy-2")

        `when`(querySnapshot.documents).thenReturn(listOf(doc1, doc2))
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        `when`(documentReference.set(any(Vacancy::class.java))).thenReturn(apiFutureWriteResult)

        val result = repository.createVacancy(testVacancy).get()

        assertNotNull(result)
        assertEquals("vacancy-3", result)
    }

    @Test
    fun `should throw ExecutionException when Firestore fails to create a vacancy`() {
        `when`(documentReference.set(any(Vacancy::class.java)))
            .thenThrow(RuntimeException("Firestore write failed"))

        val future = repository.createVacancy(testVacancy)

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle updating non-existent vacancy`() {
        `when`(documentReference.set(any(Vacancy::class.java)))
            .thenThrow(RuntimeException("Document not found"))

        val future = repository.updateVacancy("non-existent-id", testVacancy)

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle deleting non-existent vacancy`() {
        `when`(documentReference.delete())
            .thenThrow(RuntimeException("Document not found"))

        val future = repository.deleteVacancy("non-existent-id")

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle Firestore execution exceptions`() {
        doThrow(RuntimeException("Firestore error")).`when`(collectionReference).get()

        val future = repository.getVacanciesByProjectId("123")

        assertThrows<ExecutionException> { future.get() }
    }
}
