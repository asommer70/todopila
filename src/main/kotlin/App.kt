package todopila

import ninja.sakib.pultusorm.core.PultusORM

val version = "0.0.1"


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
	
	// TODO:as Display a list of Lists.
//	val obj = parse("/object.json") as JsonObject
	//val pultusORM: PultusORM = PultusORM("~/.todopila/db/todopila.db")
	val pultusORM: PultusORM = PultusORM("todopila.db", "/Users/adam/.todopila/db/")
	
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
//	
	val todoLists = pultusORM.find(TodoList())
	for (it in todoLists) {
	    val todoList = it as TodoList
	    println(todoList.listId)
	    println(todoList.name)
	    println(todoList.userId)
	    println(todoList.createdAt)
	    println()
	}
	
	println("Hello World")
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

//fun parse(name: String) : Any {
//    val cls = Parser::class.java
//    cls.getResourceAsStream(name)?.let { inputStream ->
//        return Parser().parse(inputStream)
//    }
//}