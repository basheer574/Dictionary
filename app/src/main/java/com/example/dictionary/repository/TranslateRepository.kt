package com.example.dictionary.repository

import com.example.dictionary.domain.LanguageResponse
import com.example.dictionary.network.DataClient
import com.example.dictionary.ui.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object TranslateRepository {
    fun getTranslateRepo(text: String,source: String,target: String) = flow<DataStatus<LanguageResponse>> {
        emit(DataStatus.Loading)
        emit(DataClient.initRequest(text, source, target))
    }.flowOn(Dispatchers.IO)
}