package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseArticles
import com.example.kdmeudinheiro.enums.KeysDatabaseBills
import com.example.kdmeudinheiro.model.Articles
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getArticles(): List<Articles> {
        val articlesList = mutableListOf<Articles>()
        val task = db.collection("table_articles").get()
        val result = task.await()
        result?.forEach {
            articlesList.add(
                Articles(
                    it.id,
                    it.data[KeysDatabaseArticles.NAMEARTICLE.key] as String,
                    it.data[KeysDatabaseArticles.IMAGEARTICLE.key] as String,
                    it.data[KeysDatabaseArticles.URLARTICLE.key] as String,
                    it.data[KeysDatabaseArticles.TYPEARTILE.key] as Int
                )
            )
        }
        return articlesList
    }

    suspend fun addArticle(mArticles: Articles): Boolean {
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseArticles.NAMEARTICLE.key, mArticles.name)
        map.put(KeysDatabaseArticles.URLARTICLE.key, mArticles.url)
        mArticles.image?.let { map.put(KeysDatabaseArticles.IMAGEARTICLE.key, it) }
        map.put(KeysDatabaseArticles.TYPEARTILE.key, mArticles.typeArticle)

        val taks = db.collection("table_articles").add(map)
        taks.await()
        return true
    }
}