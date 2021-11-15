package com.example.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
class MainActivity : AppCompatActivity() {
    var detectedLanguage: String? = null
    var  isDownloaded:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this);

        // Create an English-German translator:
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.HI)
            .build()
        val englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        englishGermanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener { // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                isDownloaded = true
            }
            .addOnFailureListener { // Model couldn’t be downloaded or other internal error.
                // ...
                isDownloaded = false
            }

        submit.setOnClickListener(View.OnClickListener {
            if (isDownloaded) {
                englishGermanTranslator.translate(enter_text.getText().toString())
                    .addOnSuccessListener { text -> // Translation successful.
                        translated_text.setText(text)
                    }
                    .addOnFailureListener {
                        // Error.
                        // ...
                    }
            } else {
                Toast.makeText(this@MainActivity, "model is not downloaded", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }
}