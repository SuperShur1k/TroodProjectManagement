package com.example.TroodProjectManagement.repository

import com.example.TroodProjectManagement.model.Vacancy
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

@Repository
class VacancyRepository {

    private val collectionName = "vacancies"
    private val firestore: Firestore = FirestoreClient.getFirestore()

    fun getAllVacancies(): CompletableFuture<List<Vacancy>> {
        return CompletableFuture.supplyAsync {
            val snapshot = firestore.collection(collectionName).get().get()
            snapshot.documents.mapNotNull { it.toObject(Vacancy::class.java)?.copy(id = it.id) }
        }
    }

    fun getVacancyById(id: String): CompletableFuture<Vacancy?> {
        return CompletableFuture.supplyAsync {
            val doc = firestore.collection(collectionName).document(id).get().get()
            doc.toObject(Vacancy::class.java)?.copy(id = doc.id)
        }
    }

    fun createVacancy(vacancy: Vacancy): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val docRef = firestore.collection(collectionName).add(vacancy).get()
            docRef.id
        }
    }

    fun updateVacancy(id: String, vacancy: Vacancy): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).set(vacancy)
        }
    }

    fun deleteVacancy(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            firestore.collection(collectionName).document(id).delete()
        }
    }
}