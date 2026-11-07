package com.example.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import android.speech.tts.UtteranceProgressListener
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.sin

class SoundManager(context: Context) : TextToSpeech.OnInitListener {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var pendingSpeechText: String? = null
    private var pendingSpeechCallback: (() -> Unit)? = null
    private var toneGenerator: ToneGenerator? = null
    var soundEnabled: Boolean = true
    var isGirl: Boolean = false

    private val utteranceCallbacks = ConcurrentHashMap<String, () -> Unit>()
    private val utteranceIdGenerator = AtomicLong(0)

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error initializing ToneGenerator: ${e.message}")
        }

        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("SoundManager", "Error initializing TTS: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts?.setLanguage(Locale("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                result = tts?.setLanguage(Locale("ar", "SA"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.getDefault())
                }
            }
            // Fine-tune speech rate & pitch dynamically based on boy / girl mode
            applyTtsVoiceSettings()

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    Log.d("SoundManager", "TTS speech started: $utteranceId")
                }

                override fun onDone(utteranceId: String?) {
                    Log.d("SoundManager", "TTS speech completed: $utteranceId")
                    if (utteranceId != null) {
                        utteranceCallbacks.remove(utteranceId)?.invoke()
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    Log.w("SoundManager", "TTS speech error on: $utteranceId")
                    if (utteranceId != null) {
                        utteranceCallbacks.remove(utteranceId)?.invoke()
                    }
                }
            })

            ttsReady = true
            Log.d("SoundManager", "TTS Initialized successfully with speech rate and listener!")
            pendingSpeechText?.let { text ->
                val cb = pendingSpeechCallback
                pendingSpeechText = null
                pendingSpeechCallback = null
                speakArabic(text, cb)
            }
        } else {
            Log.e("SoundManager", "TTS Init failed with status: $status")
        }
    }

    private fun applyTtsVoiceSettings() {
        if (isGirl) {
            // Higher, warmer and cheerful sweet tone for a girl child narrator
            tts?.setPitch(1.24f)
            tts?.setSpeechRate(1.02f)
        } else {
            // Crisp, lively and energetic tone for a boy child narrator
            tts?.setPitch(1.08f)
            tts?.setSpeechRate(1.04f)
        }
    }

    fun speakArabic(text: String, onDone: (() -> Unit)? = null) {
        if (!soundEnabled) {
            onDone?.invoke()
            return
        }
        if (!ttsReady) {
            pendingSpeechText = text
            pendingSpeechCallback = onDone
            Log.d("SoundManager", "TTS not ready yet, pending speech saved: $text")
            return
        }
        applyTtsVoiceSettings()
        val utteranceId = "AnimalTTS_${utteranceIdGenerator.incrementAndGet()}"
        if (onDone != null) {
            utteranceCallbacks[utteranceId] = onDone
        }
        scope.launch {
            try {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            } catch (e: Exception) {
                Log.e("SoundManager", "TTS Speak error: ${e.message}")
                if (onDone != null) {
                    utteranceCallbacks.remove(utteranceId)?.invoke()
                }
            }
        }
    }

    /**
     * Welcome greeting sound effect and voice adapted for boy or girl
     */
    fun playWelcomeSound(childName: String, onDone: (() -> Unit)? = null) {
        if (!soundEnabled) {
            onDone?.invoke()
            return
        }
        playLevelFanfare()
        val defaultTitle = if (isGirl) "يا بطلة" else "يا بطل"
        val nameText = childName.trim().ifEmpty { defaultTitle }
        val greetingText = if (isGirl) {
            "أهلاً $nameText! مرحبًا بكِ في حديقة الحيوانات ومغامرة مطابقة الظلال!"
        } else {
            "أهلاً $nameText! مرحبًا بك في حديقة الحيوانات ومغامرة مطابقة الظلال!"
        }
        speakArabic(greetingText, onDone)
    }

    /**
     * Correct match sound: Speaks animal name and praise "روعة + اسم المستخدم"
     */
    fun playCorrectMatchSound(childName: String, animalName: String, onDone: (() -> Unit)? = null) {
        if (!soundEnabled) {
            onDone?.invoke()
            return
        }
        val nameText = childName.trim()
        val matchPraise = if (nameText.isNotEmpty()) {
            "$animalName! روعة $nameText!"
        } else {
            "$animalName! روعة!"
        }
        speakArabic(matchPraise, onDone)
    }

    /**
     * Stage complete applause sound and voice adapted for boy/girl
     */
    fun playStageCompleteSound(childName: String, onDone: (() -> Unit)? = null) {
        if (!soundEnabled) {
            onDone?.invoke()
            return
        }
        playApplauseSound()
        val defaultTitle = if (isGirl) "يا بطلة" else "يا بطل"
        val nameText = childName.trim().ifEmpty { defaultTitle }
        val stagePraise = if (isGirl) {
            "أحسنتِ يا $nameText! تصفيق حار لمرحلة رائعة!"
        } else {
            "أحسنت يا $nameText! تصفيق حار لمرحلة رائعة!"
        }
        speakArabic(stagePraise, onDone)
    }

    /**
     * Synthesized Applause / Clapping Sound Effect (صوت تصفيق)
     */
    fun playApplauseSound() {
        if (!soundEnabled) return
        playLevelFanfare()
        scope.launch {
            try {
                // Generate clapping white-noise burst simulation using ToneGenerator
                for (i in 0..3) {
                    toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
                    kotlinx.coroutines.delay(120)
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Grand victory sound effect and voice adapted for boy/girl
     */
    fun playVictorySound(childName: String) {
        if (!soundEnabled) return
        playVictoryFanfare()
        val defaultTitle = if (isGirl) "يا بطلة" else "يا بطل"
        val nameText = childName.trim().ifEmpty { defaultTitle }
        val victoryText = if (isGirl) {
            "مبارك لكِ يا $nameText! أحسنتِ! فزتِ بكأس الفوز العظيم في حديقة الحيوانات!"
        } else {
            "مبارك لك يا $nameText! أحسنت! فزت بكأس الفوز العظيم في حديقة الحيوانات!"
        }
        speakArabic(victoryText)
    }

    /**
     * Instant Sound Effect for Card Drag Start / Movement
     */
    fun playDragStartSound() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
            } catch (_: Exception) {}
        }
    }

    /**
     * Congratulatory Bell / Chime Sound (جرس تهنئة عند إتمام المهمة)
     */
    fun playBellChime() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 180)
                playSynthesizedToneSequence(
                    listOf(
                        Tone(659.25f, 100),  // E5
                        Tone(830.61f, 100),  // G#5
                        Tone(987.77f, 120),  // B5
                        Tone(1318.51f, 250)  // E6 bell finish
                    )
                )
            } catch (e: Exception) {
                Log.e("SoundManager", "Bell chime error: ${e.message}")
            }
        }
    }

    /**
     * Snap Match Sound when card lands on target
     */
    fun playMatchSnap() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                playSynthesizedToneSequence(
                    listOf(
                        Tone(523.25f, 80),   // C5
                        Tone(659.25f, 120)   // E5
                    )
                )
            } catch (e: Exception) {
                Log.e("SoundManager", "Snap error: ${e.message}")
            }
        }
    }

    /**
     * Gentle Wrong Drop Sound
     */
    fun playWrongDrop() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 120)
                playSynthesizedToneSequence(
                    listOf(
                        Tone(330.0f, 100),   // E4
                        Tone(261.63f, 150)   // C4
                    )
                )
            } catch (e: Exception) {
                Log.e("SoundManager", "Wrong drop error: ${e.message}")
            }
        }
    }

    /**
     * Level Complete Celebration Fanfare
     */
    fun playLevelFanfare() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 200)
                playSynthesizedToneSequence(
                    listOf(
                        Tone(523.25f, 100),  // C5
                        Tone(659.25f, 100),  // E5
                        Tone(783.99f, 100),  // G5
                        Tone(1046.50f, 300)  // C6
                    )
                )
            } catch (e: Exception) {
                Log.e("SoundManager", "Fanfare error: ${e.message}")
            }
        }
    }

    /**
     * Grand Victory Fanfare (كأس الفوز)
     */
    fun playVictoryFanfare() {
        if (!soundEnabled) return
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_L, 300)
                playSynthesizedToneSequence(
                    listOf(
                        Tone(523.25f, 120),
                        Tone(659.25f, 120),
                        Tone(783.99f, 120),
                        Tone(1046.50f, 180),
                        Tone(880.00f, 120),
                        Tone(1046.50f, 400)
                    )
                )
            } catch (e: Exception) {
                Log.e("SoundManager", "Victory fanfare error: ${e.message}")
            }
        }
    }

    private data class Tone(val freq: Float, val durationMs: Int)

    private fun playSynthesizedToneSequence(tones: List<Tone>) {
        val sampleRate = 22050
        val totalSamples = tones.sumOf { (sampleRate * it.durationMs) / 1000 }
        val pcm = ShortArray(totalSamples)

        var currentIndex = 0
        for (tone in tones) {
            val numSamples = (sampleRate * tone.durationMs) / 1000
            for (i in 0 until numSamples) {
                val angle = 2.0 * Math.PI * i / (sampleRate / tone.freq)
                val fade = 1.0 - (i.toDouble() / numSamples)
                val sampleValue = (sin(angle) * 28000 * fade).toInt().toShort()
                if (currentIndex < pcm.size) {
                    pcm[currentIndex++] = sampleValue
                }
            }
        }

        val bufferSize = totalSamples * 2
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack.play()
            audioTrack.write(pcm, 0, pcm.size)

            scope.launch {
                val totalDurationMs = tones.sumOf { it.durationMs } + 300
                kotlinx.coroutines.delay(totalDurationMs.toLong())
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("SoundManager", "Error playing synthesized tone: ${e.message}")
        }
    }

    fun onDestroy() {
        try {
            toneGenerator?.release()
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}

