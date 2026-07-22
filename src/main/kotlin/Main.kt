package org.xiobin

import kotlin.math.abs

fun main() {
    initBoard()
    val (player1, player2) = setup(Board.getBoard())
    while(GameState.isRunning()){
        move(player1, player2)
    }
}

class InvalidInputException : Exception()

fun move(player1: String, player2: String){
    println((if(GameState.isWhiteTurn()) "$player1's" else "$player2's") + " turn:")
    resetEnPassant()
    validateMove(readln())
}

fun initBoard(){
    for ((key,values) in Board.getBoard()){
        if(Regex("[a-h]2").matches(key)){
            Board.setPawn(key, Pawn("Soldier", "white"))
        }
        if(Regex("[a-h]7").matches(key)){
            Board.setPawn(key, Pawn("Soldier", "black"))
        }
    }
}

fun resetEnPassant(){
    val color = GameState.getCurrentColor()
    for((k,v) in Board.getBoard()){
        if((v?.getColor() ?: "") == color && (v?.getEnPassant() == true)){
            v.resetEnPassant()
        }
    }
}

fun setup(board: MutableMap<String, Pawn?>): List<String>{
    val players = mutableListOf<String>()
    println("${" ".repeat(GameState.BOARD_OFFSET-2)}Pawns-Only Chess")
    println("First Player's name: ")
    players.add(readln())
    println("Second Player's name:")
    players.add(readln())
    printBoard(board)
    return players
}

fun printBoard(board: MutableMap<String, Pawn?>){
    try {
        printDivider()
        for (i in GameState.BOARD_SIZE downTo  1){
            printRow(i, Board.getBoard())
        }
        printCharacters()
    }catch (e: ArrayIndexOutOfBoundsException){
        println(e.message)
    }
    catch (e: NullPointerException){
        println(e.message)
    }
    catch (e: Exception)
    {
        println(e.message)
    }
}

fun printDivider(){
    println("${" ".repeat(GameState.BOARD_OFFSET)}+---+---+---+---+---+---+---+---+")
}


fun printRow(row: Int, board: MutableMap<String, Pawn?>){
    println("${" ".repeat(GameState.BOARD_OFFSET-2)}$row "
            + "| ${board["a$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["b$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["c$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["d$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["e$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["f$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["g$row"]?.getColor()?.get(0)?.uppercase() ?: " "} "
            + "| ${board["h$row"]?.getColor()?.get(0)?.uppercase() ?: " "} |")
    printDivider()
}

fun printCharacters(){
    println("${" ".repeat(GameState.BOARD_OFFSET+2)}a   b   c   d   e   f   g   h  ")
}

fun isAttack(input: String): Boolean{
    val enPassantPos1 = input[2] + "${(input[3].code-1).toChar()}"
    val enPassantPos2 = input[2] + "${(input[3].code+1).toChar()}"
    if(GameState.isWhiteTurn() && input[0].code == input[2].code - 1
        && input[1] == input[3] - 1 && isEnemyPawn(input.substring(2,4))){
        return normalAttack(input)
    }
    else if(GameState.isWhiteTurn() && input[0].code == input[2].code + 1
        && input[1] == input[3] - 1 && isEnemyPawn(input.substring(2,4))){
        return normalAttack(input)
    }
    else if(!GameState.isWhiteTurn() && input[0].code == input[2].code - 1
        && input[1] == input[3] + 1 && isEnemyPawn(input.substring(2,4))){
        return normalAttack(input)
    }
    else if(!GameState.isWhiteTurn() && input[0].code == input[2].code + 1
        && input[1] == input[3] + 1 && isEnemyPawn(input.substring(2,4))){
        return normalAttack(input)
    }
    else if(GameState.isWhiteTurn() && input[0].code == input[2].code - 1
        && input[1] == input[3] - 1
        && isEnemyPawn(enPassantPos1)  && Board.getPawn(enPassantPos1)!!.getEnPassant()){
        return enPassantAttack(input)
    }
    else if((GameState.isWhiteTurn() && input[0].code == input[2].code + 1
                && input[1] == input[3] - 1
                && isEnemyPawn(enPassantPos1)) && Board.getPawn(enPassantPos1)!!.getEnPassant()
    ){
        return enPassantAttack(input)
    }
    else if(!GameState.isWhiteTurn() && input[0].code == input[2].code - 1
        && input[1] == input[3] + 1
        && isEnemyPawn(enPassantPos2)  && Board.getPawn(enPassantPos2)!!.getEnPassant()){
        return enPassantAttack(input)
    }
    else if(!GameState.isWhiteTurn() && input[0].code == input[2].code + 1
        && input[1] == input[3] + 1
        && isEnemyPawn(enPassantPos2)  && Board.getPawn(enPassantPos2)!!.getEnPassant()){
        return enPassantAttack(input)
    }
    return false
}

fun enPassantAttack(input: String): Boolean{
    if(GameState.isWhiteTurn()){
        Board.setPawn("${input[2]}${input[3]}",
            Board.getPawn("${input[0]}${input[1]}"))
        Board.remove("${input[0]}${input[1]}")
        Board.remove("${input[2]}${input[3]-1}")
    }
    else{
        Board.setPawn("${input[2]}${input[3]}",
            Board.getPawn("${input[0]}${input[1]}"))
        Board.remove("${input[0]}${input[1]}")
        Board.remove("${input[2]}${input[3]+1}")
    }
    return true
}

fun normalAttack(input: String): Boolean{
    Board.setPawn("${input[2]}${input[3]}",
        Board.getPawn("${input[0]}${input[1]}"))
    Board.remove("${input[0]}${input[1]}")
    return true
}

fun validateMove(input: String){
    if(input == "exit") end()
    else if(checkRanges(input) && isAttack(input)){
        isEnPassant(input)
        printBoard(Board.getBoard())
        if(GameState.hasWon() || GameState.isStaleMate()) GameState.end()
        else GameState.changeTurn()
    }
    else if(checkRanges(input) && checkMove(input)){
        Board.setPawn("${input[2]}${input[3]}",
            Board.getPawn("${input[0]}${input[1]}"))
        Board.remove("${input[0]}${input[1]}")
        isEnPassant(input)
        printBoard(Board.getBoard())
        if(GameState.hasWon() || GameState.isStaleMate()) GameState.end()
        else GameState.changeTurn()
    }
    else{
        if(!GameState.isPawnSelected() || !isYourPawn(input.substring(0,2))){
            println("No ${if(GameState.isWhiteTurn()) "white"
            else "black"} pawn at ${input[0]}${input[1]}")
        }
        else println("Invalid Input")
    }
}

fun isEnPassant(input: String): Boolean{
    if(input[2] != 'a' &&
        Board.getPawn("${(input[2].code-1).toChar()}${input[3]}") != null){
        Board.getPawn("${(input[2])}${input[3]}")!!.setEnPassant()
        return true
    }
    else if(input[2] != 'h' &&
        Board.getPawn("${(input[2].code+1).toChar()}${input[3]}") != null){
        Board.getPawn("${(input[2])}${input[3]}")!!.setEnPassant()
        return true
    }
    else return false
}

fun isEnemyPawn(input: String): Boolean{
    val playerColor = if(GameState.isWhiteTurn()) "white" else "black"
    val color = Board.getPawn("${input[0]}${input[1]}")?.getColor() ?: ""
    return color != "" && playerColor != color
}

fun isYourPawn(input: String): Boolean{
    val playerColor = if(GameState.isWhiteTurn()) "white" else "black"
    val color = Board.getPawn("${input[0]}${input[1]}")?.getColor() ?: ""
    return playerColor == color
}

fun checkRanges(input: String): Boolean {
    return Regex("[a-h][1-8][a-h][1-8]").matches(input)
}

fun checkMove(input: String): Boolean{
    return (input[0] == input[2] || input[1] == input[3]
            || isDiagonal(input)) && isStepValid(input)
}

fun isStepValid(input: String): Boolean{
    GameState.setPawnSelected(true)
    val p = Board.getPawn("${input[0]}${input[1]}")
    val moveDistance = input[3].digitToInt() - input[1].digitToInt()
    if(p != null) {
        if(p.isFirstMove() && abs(moveDistance) == 2){
            return firstMove(input, moveDistance, p)
        }
        else if(checkBoardEnd(input,p)) return false
        else if(abs(moveDistance) == 1){
            return normalMove(input, moveDistance, p)
        }
        else return false
    }
    else if(isDiagonal(input)) return false
    else{
        GameState.setPawnSelected(false)
        return false
    }
}

fun isDiagonal(input: String): Boolean{
    val numberArray = arrayOf<Int>(input[0].code, input[1].code,
        input[2].code, input[3].code)
    return numberArray[0] - numberArray[2] == numberArray[1] - numberArray[3]
            || numberArray[0] - numberArray[2] == (numberArray[1] - numberArray[3])*-1
}

fun checkBoardEnd(input: String, p: Pawn): Boolean{
    return p.getColor()[0].uppercase() == "W" && input[1].digitToInt() + 1 > 8
            || p.getColor()[0].uppercase() == "B" && input[1].digitToInt() - 1 < 0
}

fun normalMove(input: String, moveDistance: Int, p: Pawn): Boolean{
    return if(p.getColor()[0].uppercase() == "W" && GameState.isWhiteTurn()
        && input[0] == input[2] && moveDistance == 1
        && Board.getPawn("${input[0]}${input[1]+1}") == null) {
        if(p.isFirstMove()) p.firstStep()
        true
    }
    else if(p.getColor()[0].uppercase() == "B" && !GameState.isWhiteTurn()
        && input[0] == input[2] && moveDistance == -1
        && Board.getPawn("${input[0]}${input[1]-1}") == null) {
        if(p.isFirstMove()) p.firstStep()
        true
    }
    else false
}

fun firstMove(input: String, moveDistance: Int, p: Pawn): Boolean{
    return if(p.getColor()[0].uppercase() == "W" && GameState.isWhiteTurn()
        && input[0] == input[2] && moveDistance == 2
        && Board.getPawn("${input[0]}${input[1]+1}") == null
        && Board.getPawn("${input[0]}${input[1]+2}") == null) {
        p.firstStep()
        true
    }
    else if(p.getColor()[0].uppercase() == "B" && !GameState.isWhiteTurn()
        && input[0] == input[2] && moveDistance == -2
        && Board.getPawn("${input[0]}${input[1]-1}") == null
        && Board.getPawn("${input[0]}${input[1]-2}") == null) {
        p.firstStep()
        true
    }
    else false
}

fun end(){
    println("Bye!")
    GameState.end()
}
