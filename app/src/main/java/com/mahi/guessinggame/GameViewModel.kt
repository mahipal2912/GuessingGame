package com.mahi.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel () {

    private val words = listOf("Android","Activity","Fragment","Navigation")
    private val secretWord = words.random().uppercase()
    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay : LiveData<String>
        get() = _secretWordDisplay

    private var correctGusses = ""

    private val _incorrectGusses = MutableLiveData<String>("")
    val incorrectGusses : LiveData<String>
        get() = _incorrectGusses

    private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft :LiveData<Int>
        get() = _livesLeft

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver:LiveData<Boolean>
        get() = _gameOver


    init {
        _secretWordDisplay.value= deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay() : String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter (str:String) = when (correctGusses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess:String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGusses += guess
                _secretWordDisplay.value = deriveSecretWordDisplay()
            } else{
                _incorrectGusses.value += "$guess "
                _livesLeft.value = _livesLeft.value?.minus(1)
            }
            if (isWon() || isLost()) _gameOver.value = true
        }
    }

    private fun isWon () = secretWord.equals(secretWordDisplay.value,true)

    private fun isLost () = (livesLeft.value ?: 0) <= 0

    fun wonLostMessage () : String {
        var message = ""

        if(isWon()) message = "you Won!"
        else if(isLost()) message = "you Lost!"
        message += " The Word was $secretWord."
        return message
    }


}