package com.example.troodprojectmanagement.repository

import com.example.TroodProjectManagement.model.Project
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

@Repository
class ProjectRepository(private val firestore: Firestore) { // ✅ Firestore через конструктор

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
            val docRef = firestore.collection(collectionName).add(project).get()
            docRef.id
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
}