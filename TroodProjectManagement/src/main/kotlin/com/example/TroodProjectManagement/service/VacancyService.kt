package com.example.TroodProjectManagement.service

import com.example.TroodProjectManagement.model.Vacancy
import com.example.TroodProjectManagement.repository.VacancyRepository
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class VacancyService(private val vacancyRepository: VacancyRepository) {

    fun getAllVacancies(): CompletableFuture<List<Vacancy>> {
        return vacancyRepository.getAllVacancies()
    }

    fun getVacancyById(id: String): CompletableFuture<Vacancy?> {
        return vacancyRepository.getVacancyById(id)
    }

    fun createVacancy(vacancy: Vacancy): CompletableFuture<String> {
        return vacancyRepository.createVacancy(vacancy)
    }

    fun updateVacancy(id: String, vacancy: Vacancy): CompletableFuture<Void> {
        return vacancyRepository.updateVacancy(id, vacancy)
    }

    fun deleteVacancy(id: String): CompletableFuture<Void> {
        return vacancyRepository.deleteVacancy(id)
    }
}