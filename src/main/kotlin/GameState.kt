package org.xiobin

object GameState {
    const val BOARD_OFFSET = 2
    const val BOARD_SIZE = 8

    private var isWhiteTurn = true
    private var isRunning = true
    private var isPawnSelected = true

    private var isWhiteStalemate = false
    private var isBlackStalemate = false

    fun isWhiteTurn(): Boolean = isWhiteTurn
    fun changeTurn() {
        isWhiteTurn = !isWhiteTurn
    }

    fun getCurrentColor(): String{
        return if(isWhiteTurn()) "white"
        else "black"
    }

    fun isRunning() = isRunning
    fun end(){
        isRunning = false
        println("Bye!")
    }
    fun isPawnSelected() = isPawnSelected
    fun setPawnSelected(isSelected: Boolean){
        isPawnSelected = isSelected
    }

    fun hasWon(): Boolean{
        var whiteCounter = 0
        var blackCounter = 0
        for(col in 'a'..'h'){
            if(Board.getPawn("$col" + "1") != null){
                println("Black Wins!")
                return true
            }
            else if(!isWhiteTurn() && isWhiteStalemate){
                println("Black Wins!")
                return true
            }
            else if(Board.getPawn("$col" + "8") != null){
                println("White Wins!")
                return true
            }
            else if(isWhiteTurn() && isBlackStalemate){
                println("White Wins!")
                return true
            }
            for(r in 1..8){
                if((Board.getPawn("$col$r")?.getColor() ?: "") == "white"){
                    whiteCounter++
                }
                else if((Board.getPawn("$col$r")?.getColor() ?: "") == "black"){
                    blackCounter++
                }
            }
        }
        if(whiteCounter == 0){
            println("Black Wins!")
            return true
        }
        else if(blackCounter == 0){
            println("White Wins!")
            return true
        }
        return false
    }

    fun isStaleMate(): Boolean{
        isWhiteStalemate = true
        isBlackStalemate = true
        for (c in 'a'..'h'){
            for (i in 1..8){
                if((Board.getPawn("$c$i")?.getColor() ?: "") == "white"){
                    if(i+1 <= 8 && Board.getPawn("$c${i+1}") == null){
                        isWhiteStalemate = false
                    }
                    else if((c.code - 1).toChar() > 'a'
                        && (Board.getPawn("${(c.code - 1).toChar()}${i + 1}")
                            ?.getColor() ?: "") == "black"){
                        isWhiteStalemate = false
                    }
                    else if((c.code + 1).toChar() < 'h'
                        && (Board.getPawn("${(c.code - 1).toChar()}${i + 1}")
                            ?.getColor() ?: "") == "black"){
                        isWhiteStalemate = false
                    }
                    else if((Board.getPawn("$c$i")?.getColor() ?: "") == "white"
                        && (Board.getPawn("$c${i-1}")?.getColor() ?: "") == "black"
                        && Board.getPawn("$c${i-1}")?.getEnPassant() == true
                    ){
                        isWhiteStalemate = false
                    }
                    else if((Board.getPawn("$c$i")?.getColor() ?: "") == "white"
                        && (Board.getPawn("$c${i+1}")?.getColor() ?: "") == "black"
                        && Board.getPawn("$c${i+1}")?.getEnPassant() == true
                    ){
                        isWhiteStalemate = false
                    }
                }
                else if((Board.getPawn("$c$i")?.getColor() ?: "") == "black"){
                    if(i-1 >= 1 && Board.getPawn("$c${i-1}") == null){
                        isBlackStalemate = false
                    }
                    else if((c.code - 1).toChar() > 'a'
                        && (Board.getPawn("${(c.code - 1).toChar()}${i - 1}")
                            ?.getColor() ?: "") == "white"){
                        isBlackStalemate = false
                    }
                    else if((c.code + 1).toChar() < 'h'
                        && (Board.getPawn("${(c.code - 1).toChar()}${i - 1}")
                            ?.getColor() ?: "") == "white"){
                        isBlackStalemate = false
                    }
                    else if((Board.getPawn("$c$i")?.getColor() ?: "") == "black"
                        && (Board.getPawn("$c${i-1}")?.getColor() ?: "") == "white"
                        && Board.getPawn("$c${i-1}")?.getEnPassant() == true
                    ){
                        isBlackStalemate = false
                    }
                    else if((Board.getPawn("$c$i")?.getColor() ?: "") == "black"
                        && (Board.getPawn("$c${i+1}")?.getColor() ?: "") == "white"
                        && Board.getPawn("$c${i+1}")?.getEnPassant() == true
                    ){
                        isBlackStalemate = false
                    }
                }
            }
        }
        if(isWhiteStalemate || isBlackStalemate){
            println("Stalemate!")
            return true
        }
        else{
            return false
        }
    }
}