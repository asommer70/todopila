package todopila

import ninja.sakib.pultusorm.core.PultusORM
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi.*;
import org.fusesource.jansi.Ansi.Color.*;

val version = "0.0.1"
val pultusORM: PultusORM = PultusORM("todopila.db", System.getenv("HOME") + "/.todopila/db")

val GREEN = "\u001B[32m"
val MAGENTA = "\u001B[35m"
val RESET = "\u001B[0m"

fun main(args: Array<String>) {	
	//
	// Handle command line arguments.
	//
	if (args.size != 0) {
		when (args[0]) {
			"--help" -> printHelp()
			"-h" -> printHelp()
			"--version" -> printVersion()
			"-v" -> printVersion()
			else -> {
				// TODO:as set active List as arg[1]...
				println("List set to:  " + args[0])
		    }
		}	
	}
	
	AnsiConsole.systemInstall();

			
	println("Welcome to ToDo Pila!")
	println("press h for a list of commands.")
	println()
	
	// TODO:as Display a list of Lists.
	getLists()
//	test()
	
	
//	var input = readLine()
//	println("You said: " + input)
	println()
}

fun printHelp() {
	println("Usage: todopila [options] [list_name]")
	println("where options include:")
	println("\t--version, -v\t\tprint product version and exit")
	println("\t--help, -h\t\tprint this help message and exit")
	System.exit(0)
}

fun printVersion() {
	println("Todo Pila! version: " + version)
	System.exit(0)
}

fun createList() {
	//	val daily: TodoList = TodoList()
//	daily.name = "Daily"
//	daily.userId = "0"
//	daily.createdAt = (System.currentTimeMillis() / 1000).toString()
//	pultusORM.save(daily)
//	val pila: TodoList = TodoList()
//	pila.name = "ToDo Pila"
//	pila.userId = "0"
//	pila.createdAt = (System.currentTimeMillis() / 1000).toString()
//	pultusORM.save(pila)
//	pultusORM.close()
}

fun getLists() {
	println("ToDo Lists:")
	
	val todoLists = pultusORM.find(TodoList())
	for ((idx, it) in todoLists.withIndex()) {
	    val todoList = it as TodoList
//		AnsiConsole.out.println("\t[$idx] ${todoList.name}");
//	    println(ansi().fg(GREEN).a("\t[$idx] ${todoList.name}").reset())
		print("$GREEN\t[$idx] $MAGENTA${todoList.name}$RESET\n")
	}
    println()
}

const val ESC = "\u001B"
const val NORMAL = ESC + "[0"
const val BOLD   = ESC + "[1"
const val BLINK  = ESC + "[5"      // not working on my machine
const val BLACK  = ESC + "[0;40m"  // black background
const val WHITE  = ESC + "[0;37m"  // normal white foreground
 
fun test() {
    print("${ESC}c") // clear terminal first
    print(BLACK)     // set background color to black
    val foreColors = listOf(
        ";31m" to "red",
        ";32m" to "green",
        ";33m" to "yellow",
        ";34m" to "blue",
        ";35m" to "magenta",
        ";36m" to "cyan",
        ";37m" to "white"
    )
    for (attr in listOf(NORMAL, BOLD, BLINK)) {
        for (color in foreColors) println("$attr${color.first}${color.second}")
    }
    println(WHITE)  // set foreground color to normal white
}

//fun parse(name: String) : Any {
//    val cls = Parser::class.java
//    cls.getResourceAsStream(name)?.let { inputStream ->
//        return Parser().parse(inputStream)
//    }
//}