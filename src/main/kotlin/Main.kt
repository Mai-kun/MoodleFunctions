import kotlin.random.Random

fun main() {
    while (true){
        println("Введите номер задания")
        when(val a = readlnOrNull()){
            "1" -> ex1()
            "2" -> ex2()
            else -> println("Неверный номер")
        }
    }
}

fun ex1(){
    val choices = arrayOf("Камень", "Ножницы", "Бумага")

    while (true) {
        val computerChoice = choices[Random.nextInt(choices.size)]

        println("Выберите ваш ход:")
        println("1 - Камень")
        println("2 - Ножницы")
        println("3 - Бумага")

        val playerChoice = when (readLine()) {
            "1" -> "Камень"
            "2" -> "Ножницы"
            "3" -> "Бумага"
            else -> {
                println("Некорректный выбор. Попробуйте еще раз.")
                continue
            }
        }

        println("Компьютер выбрал: $computerChoice")
        println("Вы выбрали: $playerChoice")

        determineWinner(computerChoice, playerChoice)

        println("Желаете сыграть еще раз? (y/n)")
        val playAgain = readLine()
        if (playAgain?.lowercase() != "y") {
            break
        }
    }
}

fun determineWinner(computerChoice: String, playerChoice: String){
    when {
        (computerChoice == "Камень" && playerChoice == "Ножницы") ||
        (computerChoice == "Ножницы" && playerChoice == "Бумага") ||
        (computerChoice == "Бумага" && playerChoice == "Камень")
        -> println("Поражение")

        (playerChoice == "Камень" && computerChoice == "Ножницы") ||
        (playerChoice == "Ножницы" && computerChoice == "Бумага") ||
        (playerChoice == "Бумага" && computerChoice == "Камень")
        -> println("Победа")

        else -> println("Ничья")
    }
}



fun ex2(){
    val alphabet = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
    val auxiliarySymbol = "Я"
    val messageDelimiter = " "

    println("Выберите тип таблицы:")
    println("1. Типовая таблица")
    println("2. Случайная таблица")
    print("Введите номер: ")

    val tableType = readLine()?.toIntOrNull()
    val cipherTable = when (tableType) {
        1 -> createDefaultCipherTable(alphabet)
        2 -> createRandomCipherTable(alphabet)
        else -> {
            println("Неверный выбор. Используется типовая таблица.")
            createDefaultCipherTable(alphabet)
        }
    }

    print("Введите исходное сообщение: ")
    val originalMessage = readLine()?.replace("Й", "И")?.replace("Ё", "Е")?.replace(" ", "")?.uppercase()
    if (originalMessage == null){
        println("Неверный ввод")
        return
    }

    val normalizedMessage = if (originalMessage.length % 2 == 1) {
        originalMessage + auxiliarySymbol
    } else {
        originalMessage
    }

    val encryptedMessage = encryptMessage(normalizedMessage, cipherTable, alphabet, messageDelimiter)

    println("Исходное сообщение: $normalizedMessage")
    println("Зашифрованное сообщение: $encryptedMessage")
    printCipherTable(cipherTable, alphabet)
}

fun createDefaultCipherTable(alphabet: String): Array<Array<Int>> {
    val tableSize = alphabet.length
    val cipherTable = Array(tableSize) { Array(tableSize) { -1 } }

    for (i in 0..< tableSize) {
        for (j in 0..< tableSize) {
            cipherTable[i][j] = (i * 31 + j + 1)
        }
    }

    return cipherTable
}

fun createRandomCipherTable(alphabet: String): Array<Array<Int>> {
    val tableSize = alphabet.length
    val cipherTable = Array(tableSize) { Array(tableSize) { -1 } }
    val usedNumbers = mutableSetOf<String>()
    val endRange = alphabet.length * alphabet.length

    for (i in 0..< tableSize) {
        for (j in 0..< tableSize) {
            var randomNumber: Int

            do {
                randomNumber = Random.nextInt(1, endRange + 1)
            } while (randomNumber.toString() in usedNumbers)

            usedNumbers.add(randomNumber.toString())
            cipherTable[i][j] = randomNumber
        }
    }

    return cipherTable
}

fun encryptMessage(
    message: String,
    cipherTable: Array<Array<Int>>,
    alphabet: String,
    delimiter: String
): String {
    val encryptedPairs = mutableListOf<String>()

    for (i in message.indices step 2) {
        val rowIndex = alphabet.indexOf(message[i])
        val colIndex = alphabet.indexOf(message[i + 1])
        encryptedPairs.add(cipherTable[rowIndex][colIndex].toString().padStart(3, '0'))
    }

    return encryptedPairs.joinToString(delimiter)
}

fun printCipherTable(cipherTable: Array<Array<Int>>, alphabet: String) {
    println("\nШифровальная таблица:")
    print("   |")
    alphabet.forEach { print("  $it  |") }
    println()
    println("-".repeat((alphabet.length) * 6 + 4))

    for (i in alphabet.indices) {
        print(" ${alphabet[i]} |")

        for (j in alphabet.indices) {
            print(" ${cipherTable[i][j].toString().padStart(3, '0')} |")
        }
        println()
        println("-".repeat((alphabet.length) * 6 + 4))
    }
}
