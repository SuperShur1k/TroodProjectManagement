package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Project
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

/**
 * Repository for managing project data in Firestore.
 */
@Repository
class ProjectRepository(private val firestore: Firestore) {

    private val collectionName = "projects"

    /**
     * Generates the next project ID based on existing projects in Firestore.
     *
     * @return A new unique project ID.
     */
    private fun generateNextProjectId(): String {
        val collection = firestore.collection(collectionName).get().get()

        val maxId = collection.documents
            .mapNotNull { it.id.removePrefix("project-").toIntOrNull() }
            .maxOrNull() ?: 0

        return "project-${maxId + 1}"
    }

    /**
     * Retrieves all projects from Firestore.
     *
     * @return A list of all projects.
     */
    fun getAllProjects(): CompletableFuture<List<Project>> {
        return CompletableFuture.supplyAsync {
            val snapshot = firestore.collection(collectionName).get().get()
            snapshot.documents.mapNotNull { it.toObject(Project::class.java)?.copy(id = it.id) }
        }
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id The project ID.
     * @return The project if found, otherwise null.
     */
    fun getProjectById(id: String): CompletableFuture<Project?> {
        return CompletableFuture.supplyAsync {
            val doc = firestore.collection(collectionName).document(id).get().get()
            doc.toObject(Project::class.java)?.copy(id = doc.id)
        }
    }

    /**
     * Creates a new project in Firestore.
     *
     * @param project The project to create.
     * @return The newly created project's ID.
     */
    fun createProject(project: Project): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val projectId = generateNextProjectId()
            firestore.collection(collectionName).document(projectId).set(project.copy(id = projectId)).get()
            projectId
        }
    }

    /**
     * Updates an existing project in Firestore.
     *
     * @param id The project ID.
     * @param project The updated project data.
     */
    fun updateProject(id: String, project: Project): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).set(project)
        }
    }

    /**
     * Deletes a project from Firestore.
     *
     * @param id The project ID.
     */
    fun deleteProject(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).delete()
        }
    }
}