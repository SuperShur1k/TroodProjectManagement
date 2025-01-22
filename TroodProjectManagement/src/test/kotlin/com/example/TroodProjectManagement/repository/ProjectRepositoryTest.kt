package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Project
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
class ProjectRepositoryTest {

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

    private lateinit var repository: ProjectRepository

    private val testProject = Project(
        id = "test-id",
        name = "Test Project",
        field = "IT",
        experience = "3 years",
        deadline = "2025-12-31",
        description = "Test description"
    )

    @BeforeEach
    fun setup() {
        repository = ProjectRepository(firestore)

        `when`(firestore.collection("projects")).thenReturn(collectionReference)
        `when`(collectionReference.document(anyString())).thenReturn(documentReference)
    }

    @Test
    fun `should return all projects`() {
        val queryDocumentSnapshot = mock(QueryDocumentSnapshot::class.java)

        `when`(queryDocumentSnapshot.toObject(Project::class.java)).thenReturn(testProject)
        `when`(queryDocumentSnapshot.id).thenReturn("test-id")

        `when`(querySnapshot.documents).thenReturn(listOf(queryDocumentSnapshot))

        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        val result = repository.getAllProjects().get()

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("test-id", result[0].id)
        assertEquals("Test Project", result[0].name)
    }

    @Test
    fun `should return project by ID`() {
        `when`(documentSnapshot.toObject(Project::class.java)).thenReturn(testProject)
        `when`(documentSnapshot.id).thenReturn("test-id")

        `when`(apiFutureDocumentSnapshot.get()).thenReturn(documentSnapshot)
        `when`(documentReference.get()).thenReturn(apiFutureDocumentSnapshot)

        val result = repository.getProjectById("test-id").get()

        assertNotNull(result)
        assertEquals("test-id", result?.id)
        assertEquals("Test Project", result?.name)
    }

    @Test
    fun `should return empty list when no projects found`() {
        `when`(querySnapshot.documents).thenReturn(emptyList())
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        val result = repository.getAllProjects().get()

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return null when project not found`() {
        `when`(documentSnapshot.toObject(Project::class.java)).thenReturn(null)
        `when`(apiFutureDocumentSnapshot.get()).thenReturn(documentSnapshot)
        `when`(documentReference.get()).thenReturn(apiFutureDocumentSnapshot)

        val result = repository.getProjectById("non-existent-id").get()

        assertNull(result)
    }

    @Test
    fun `should create a new project with structured ID`() {
        `when`(querySnapshot.documents).thenReturn(emptyList())
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        `when`(documentReference.set(any(Project::class.java)))
            .thenReturn(apiFutureWriteResult)

        val result = repository.createProject(testProject).get()

        assertNotNull(result)
        assertTrue(result.startsWith("project-"))
    }

    @Test
    fun `should update project successfully`() {
        `when`(documentReference.set(any(Project::class.java)))
            .thenReturn(apiFutureWriteResult)

        val future = repository.updateProject("test-id", testProject)

        assertDoesNotThrow { future.get() }
    }

    @Test
    fun `should delete project successfully`() {
        `when`(documentReference.delete())
            .thenReturn(apiFutureWriteResult)

        val future = repository.deleteProject("test-id")

        assertDoesNotThrow { future.get() }
    }

    @Test
    fun `should generate next project ID correctly`() {
        val doc1 = mock(QueryDocumentSnapshot::class.java)
        val doc2 = mock(QueryDocumentSnapshot::class.java)

        `when`(doc1.id).thenReturn("project-1")
        `when`(doc2.id).thenReturn("project-2")

        `when`(querySnapshot.documents).thenReturn(listOf(doc1, doc2))
        `when`(apiFutureQuerySnapshot.get()).thenReturn(querySnapshot)
        `when`(collectionReference.get()).thenReturn(apiFutureQuerySnapshot)

        `when`(documentReference.set(any(Project::class.java))).thenReturn(apiFutureWriteResult)

        val result = repository.createProject(testProject).get()

        assertNotNull(result)
        assertEquals("project-3", result)
    }

    @Test
    fun `should throw ExecutionException when Firestore fails to create a project`() {
        `when`(documentReference.set(any(Project::class.java)))
            .thenThrow(RuntimeException("Firestore write failed"))

        val future = repository.createProject(testProject)

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle updating non-existent project`() {
        `when`(documentReference.set(any(Project::class.java)))
            .thenThrow(RuntimeException("Document not found"))

        val future = repository.updateProject("non-existent-id", testProject)

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle deleting non-existent project`() {
        `when`(documentReference.delete())
            .thenThrow(RuntimeException("Document not found"))

        val future = repository.deleteProject("non-existent-id")

        assertThrows<ExecutionException> { future.get() }
    }

    @Test
    fun `should handle Firestore execution exceptions`() {
        doThrow(RuntimeException("Firestore error")).`when`(collectionReference).get()

        val future = repository.getAllProjects()

        assertThrows<ExecutionException> { future.get() }
    }
}
