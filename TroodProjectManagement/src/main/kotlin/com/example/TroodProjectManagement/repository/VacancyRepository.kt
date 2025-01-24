package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Vacancy
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

/**
 * Repository for managing vacancy data in Firestore.
 */
@Repository
class VacancyRepository(private val firestore: Firestore) {
    private val collectionName = "vacancies"

    /**
     * Retrieves a list of vacancies for a given project ID.
     *
     * @param projectId The project ID.
     * @return A list of vacancies associated with the project.
     */
    fun getVacanciesByProjectId(projectId: String): CompletableFuture<List<Vacancy>> {
        return CompletableFuture.supplyAsync {
            val snapshot = firestore.collection(collectionName)
                .whereEqualTo("projectId", projectId)
                .get().get()
            snapshot.documents.mapNotNull { it.toObject(Vacancy::class.java)?.copy(id = it.id) }
        }
    }

    /**
     * Generates a unique vacancy ID based on existing vacancies.
     *
     * @return A new unique vacancy ID.
     */
    private fun generateNextVacancyId(): String {
        val collection = firestore.collection(collectionName).get().get()

        val maxId = collection.documents
            .mapNotNull { it.id.removePrefix("vacancy-").toIntOrNull() }
            .maxOrNull() ?: 0

        return "vacancy-${maxId + 1}"
    }

    /**
     * Creates a new vacancy in Firestore.
     *
     * @param vacancy The vacancy to create.
     * @return The newly created vacancy ID.
     */
    fun createVacancy(vacancy: Vacancy): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val vacancyId = generateNextVacancyId()
            firestore.collection(collectionName).document(vacancyId).set(vacancy.copy(id = vacancyId)).get()
            vacancyId
        }
    }

    /**
     * Updates an existing vacancy.
     *
     * @param id The vacancy ID.
     * @param vacancy The updated vacancy data.
     */
    fun updateVacancy(id: String, vacancy: Vacancy): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).set(vacancy)
        }
    }

    /**
     * Deletes a vacancy by its ID.
     *
     * @param id The vacancy ID.
     */
    fun deleteVacancy(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).delete()
        }
    }

    /**
     * Retrieves a vacancy by its ID.
     *
     * @param id The vacancy ID.
     * @return The vacancy if found, otherwise null.
     */
    fun getVacancyById(id: String): CompletableFuture<Vacancy?> {
        return CompletableFuture.supplyAsync {
            val doc = firestore.collection(collectionName).document(id).get().get()
            doc.toObject(Vacancy::class.java)?.copy(id = doc.id)
        }
    }
}