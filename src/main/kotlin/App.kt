package todopila

import ninja.sakib.pultusorm.core.PultusORM
import kotlin.text.regex

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
			
	println("Welcome to ToDo Pila!")
	println("press h for a list of commands.")
	println()
	
	val todolist = TodoList()
	val selectListCheck = Regex("L\\d")
	var q = false
	while (q != true) {
		var input: String? = readLine()
	 	when (input) {
			"h" -> printCommands()
			"L" -> todolist.getLists()
//			selectListCheck.matches(input) -> todolist.selectList(input)
			"q" -> q = true
	 	}  
	}

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

fun printCommands() {
	println("ToDo List Actions:")
	println("\tL to list ToDo Lists.")
	println("\tL[\$NUMBER] to select a ToDo List.")
	println ("\tA to add an Item to the selected ToDo List.")
	println()
	
	println("Item Actions:")
	println("\tl to list Items in the selected ToDo List.")
	println("\tl[\$NUMBER] to select an Item.")
	println("\t+ to mark Item as done.")
	println("\t- to mark Item as not done.")
	println("\tD to delete Item.")
	println()
}


//fun parse(name: String) : Any {
//    val cls = Parser::class.java
//    cls.getResourceAsStream(name)?.let { inputStream ->
//        return Parser().parse(inputStream)
//    }
//}