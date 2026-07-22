package org.xiobin

open class Pawn (private val rank: String, private val color: String, private var isFirstMove: Boolean = true){
    private var isEnPassant : Boolean = false
    fun getRank() = rank
    fun getColor() = color
    fun isFirstMove() = isFirstMove
    fun firstStep(){
        isFirstMove = false
    }
    fun getEnPassant(): Boolean{
        return isEnPassant
    }

    fun setEnPassant(){
        isEnPassant = true
    }

    fun resetEnPassant(){
        isEnPassant = false
    }
}