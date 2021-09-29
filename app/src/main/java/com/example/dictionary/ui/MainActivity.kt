package com.example.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.R
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.domain.LanguageResponse
import com.example.dictionary.network.DataClient
import com.example.dictionary.repository.TranslateRepository
import com.example.dictionary.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var sourceLang: String = ""
    var targetLang: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpinners()

        binding.textToTranslate.doOnTextChanged { text, start, before, count ->
            getTranslateRequest(text.toString(),sourceLang,targetLang)
        }
    }

    private fun initSpinners() {
        val sourceArrayAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.spinner_array,
            R.layout.spinner_item
        )
        val targetArrayAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.spinner_array,
            R.layout.spinner_item
        )
        binding.sourceSpinner.apply {
            adapter = sourceArrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    sourceLang = p0?.getItemAtPosition(p2).toString()
                    Log.i(LOG_TAG,sourceLang)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //Do nothing
                }
            }
        }
        binding.targetSpinner.apply {
            adapter = targetArrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    targetLang = p0?.getItemAtPosition(p2).toString()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //Do nothing
                }
            }
        }
    }
    private fun getTranslateRequest(text: String,source: String,target: String) {
        binding.textToTranslate.doOnTextChanged{ text, start, before, count ->
            dictionaryMap()
            lifecycleScope.launch {
                TranslateRepository.getTranslateRepo(text.toString(), source, target).onCompletion {
                    Toast.makeText(this@MainActivity,"Completed!",Toast.LENGTH_SHORT).show()
                }.catch {
                    Toast.makeText(this@MainActivity,
                        "Cannot access, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }.collect { getTranslate(it) }
            }
        }

    }

    private fun getTranslate(dataResponse: DataStatus<LanguageResponse>) {
        return when(dataResponse){
            is DataStatus.Error -> {
                Toast.makeText(this@MainActivity,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is DataStatus.Loading -> {
                Toast.makeText(this@MainActivity,
                    "Loading...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is DataStatus.Success -> {
                Toast.makeText(this@MainActivity,
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()
                bindData(dataResponse.data)
            }
        }
    }

    private fun bindData(translateData: LanguageResponse) {
        binding.translateResult.text = translateData.text
    }

    private fun dictionaryMap(){
        val map = mapOf(
            "Auto Detect" to "auto",
            "English" to "en",
            "Arabic" to "ar",
            "Chinese" to "zh",
            "French" to "fr",
            "German" to "gr",
            "Hindi" to "hi",
            "Indonesian" to "id",
            "Irish" to "ga",
            "Italian" to "it",
            "Japanese" to "ja",
            "Korean" to "ko",
            "Polish" to "pl",
            "Portuguese" to "pt",
            "Russian" to "ru",
            "Spanish" to "es",
            "Turkish" to "tr",
            "Vietnamese" to "vi"
        )
        sourceLang = map[sourceLang].toString()
        targetLang = map[targetLang].toString()
        Log.i(LOG_TAG,sourceLang)
    }

    companion object {
        const val LOG_TAG = "BASHEER_DRAGON"
    }
}