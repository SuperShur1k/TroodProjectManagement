package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Project
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

@Repository
class ProjectRepository(private val firestore: Firestore) {

    private val collectionName = "projects"

    fun getAllProjects(): CompletableFuture<List<Project>> {
        return CompletableFuture.supplyAsync {
            val snapshot = firestore.collection(collectionName).get().get()
            snapshot.documents.mapNotNull { it.toObject(Project::class.java)?.copy(id = it.id) }
        }
    }

    fun getProjectById(id: String): CompletableFuture<Project?> {
        return CompletableFuture.supplyAsync {
            val doc = firestore.collection(collectionName).document(id).get().get()
            doc.toObject(Project::class.java)?.copy(id = doc.id)
        }
    }

    fun createProject(project: Project): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val projectId = generateNextProjectId()
            firestore.collection(collectionName).document(projectId).set(project.copy(id = projectId)).get()
            projectId
        }
    }

    fun updateProject(id: String, project: Project): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).set(project)
        }
    }

    fun deleteProject(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).delete()
        }
    }

    private fun generateNextProjectId(): String {
        val collection = firestore.collection(collectionName).get().get()
        val count = collection.size() + 1
        return "project-$count"
    }
}