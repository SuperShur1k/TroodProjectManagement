package com.example.Troodprojectmanagement.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

/**
 * Configures Firebase for the application.
 * Initializes Firebase and provides access to Firestore.
 */
@Configuration
class FirebaseConfig {

    /**
     * Firebase database URL from application properties.
     */
    @Value("\${firebase.database-url}")
    private lateinit var databaseUrl: String

    /**
     * Path to Firebase credentials JSON file.
     */
    @Value("\${firebase.credentials}")
    private lateinit var credentials: String

    /**
     * Initializes Firebase if not already set up.
     *
     * @return FirebaseApp instance.
     */
    @Bean
    fun firebaseApp(): FirebaseApp {
        if (FirebaseApp.getApps().isEmpty()) {
            val resource = ClassPathResource(credentials)
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
                .setDatabaseUrl(databaseUrl)
                .build()
            return FirebaseApp.initializeApp(options)
        }
        return FirebaseApp.getInstance()
    }

    /**
     * Provides Firestore database instance.
     *
     * @param firebaseApp Firebase application instance.
     * @return Firestore instance.
     */
    @Bean
    fun firestore(firebaseApp: FirebaseApp): Firestore {
        return FirestoreClient.getFirestore(firebaseApp)
    }
}