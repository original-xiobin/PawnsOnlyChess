package org.xiobin

object Board {
    private val board = mutableMapOf<String,Pawn?>()
    init {
        for(n in 1..8){
            for(c in 'a'..'h') {
                val k = "$c$n"
                board[k] = null
            }
        }
    }

    fun getBoard(): MutableMap<String, Pawn?> {
        return board
    }

    fun setPawn(position: String, pawn: Pawn?){
        if(Regex("[a-h][1-8]").matches(position)){
            board[position] = pawn
        }
        else println("Invalid position")
    }

    fun getPawn(position: String): Pawn?{
        return board[position]
    }

    fun remove(position: String){
        try {
            if (board[position] != null){
                board[position] = null
            }
        }catch (e: Exception){
            // println(e.message)
            println("Invalid position")
        }
    }
}