package todopila

import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMUpdater
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMQuery
import java.lang.System
import java.io.InputStream
import java.io.ByteArrayInputStream

val version = "0.0.1"
val pultusORM: PultusORM = PultusORM("todopila.db", System.getenv("HOME") + "/.todopila/db")

val GREEN = "\u001B[32m"
val MAGENTA = "\u001B[35m"
val RESET = "\u001B[0m"

var selectedList: TodoList? = null
var listItems: MutableList<Any> = mutableListOf<Any>()
val selectListCheck = Regex("""L\d""")
val markDoneCheck = Regex("""\+\d""")
val markNotDoneCheck = Regex("""\-\d""")
val archiveItemCheck = Regex("""D\d""")
val editItemCheck = Regex("""e\d""")

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
			
	// Print welcome message.
	println("Welcome to ToDo Pila!")
	println("press h, or ?, for a list of commands.")
	println()
	getPrompt()
	
	// Setup variables.
	val todolist = TodoList()
	todolist.loadLists()
	var q = false
	
	// Main loop.
	while (q != true) {
		var input: String = readLine().toString()

		if (input.isEmpty()) { 	getPrompt() }
		else if (input == "q") { q = true }
			
		else if (input == "h" || input == "?") { printCommands() }
			
		else if (input == "L") { todolist.getLists() }
			
		else if (selectListCheck.containsMatchIn(input)) {
			selectedList = todolist.selectList(input.removeRange(0, 1).toInt())
			listItems = todolist.getItems(selectedList!!.listId.toString())
			println("Selected List: ${selectedList!!.name}")
			getPrompt()
		}
		
		else if (input == "a") {
			println("Add Item to ${selectedList!!.name}:")
			var itemInput: String = readLine().toString()
			Item().create(itemInput, selectedList!!.listId.toString())
			
			println("Items in ${selectedList!!.name}:")
			listItems = todolist.getItems(selectedList!!.listId.toString())
			printItems()		}
			
	    else if (input == "l") {
			if (selectedList != null) {
				println("Items in ${selectedList!!.name}:")
				listItems = todolist.getItems(selectedList!!.listId.toString())
				printItems()
			} else { println("Please select a ToDo List first.") }
			
		}
			
		else if (markDoneCheck.containsMatchIn(input) ||
				markNotDoneCheck.containsMatchIn(input) ||
				archiveItemCheck.containsMatchIn(input) ||
				editItemCheck.containsMatchIn(input))
		{
			var item = listItems[input.removeRange(0, 1).toInt()] as Item
			
			var updateValue: Any = 1
			var updateField = "status"

			if (input.first().toString() == "-") updateValue = 0
			
			else if (archiveItemCheck.containsMatchIn(input)) updateField = "archive"
			
			else if (editItemCheck.containsMatchIn(input)) {
				updateField = "content"
				
				println("Old Item: ${item.content}")
				updateValue = readLine().toString()
				if (updateValue == "") updateValue = item.content
			}
			
			val condition: PultusORMCondition = PultusORMCondition.Builder()
			            .eq("itemId", item.itemId)
			            .eq("updatedAt", (System.currentTimeMillis() / 1000).toString())
			            .build()
			
			val updater: PultusORMUpdater = PultusORMUpdater.Builder()
			            .set(updateField, updateValue)
			            .condition(condition)   // condition is optional
			            .build()
			
			pultusORM.update(Item(), updater)
			
			println("Items in ${selectedList!!.name}:")
			listItems = todolist.getItems(selectedList!!.listId.toString())
			printItems()
		}
			
		else {
			println("Sorry, I don't recognize that command.")
			getPrompt()
		 }
	}

	println("ToDo Pila! salutes you! And good bye...")
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
	println("\tA to add a new ToDo List.")
	println("\tL[\$NUMBER] to select a ToDo List.")
	println ("\ta to add an Item to the selected ToDo List.")
	println("\tE[\$NUMBER] to edit the selected ToDo List.")
    println("\tDD to archive the selected ToDo List.")
	println()
	
	println("Item Actions:")
	println("\tl to list Items in the selected ToDo List.")
	println("\te[\$NUMBER] to edit an Item.")
	println("\t+[\$NUMBER] to mark Item as done.")
	println("\t-[\$NUMBER] to mark Item as not done.")
	println("\tD[\$NUMBER] to archive Item.")
	println()
	getPrompt()
}

fun getPrompt() {
	if (selectedList != null) {
		print("todopila -> ${selectedList!!.name}> ")
	} else {
		print("todopila> ")
	}
}

fun printItems() {
	for ((idx, it) in listItems.withIndex()) {
	    val item = it as Item
		var done = " "
		if (item.status) done = "\u2713"
	    println("\t${idx}. [$done] ${item.content}")
	}
	println()
	getPrompt()
}