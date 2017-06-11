package todopila

import java.util.Date;
import java.io.File
import java.io.Reader
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMUpdater
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMQuery
import ninja.sakib.pultusorm.exceptions.PultusORMException
import ninja.sakib.pultusorm.callbacks.Callback
import ninja.sakib.pultusorm.core.log
import org.ocpsoft.prettytime.PrettyTime;
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable

val version = "0.0.1"
val dbPath = System.getenv("HOME") + "/.todopila/db"
val pultusORM: PultusORM = PultusORM("todopila.db", dbPath)

val CLEAR = "\u001Bc"
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
	
	// Add client Settings.
	Settings().createClientSettings()
			
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
			
		else if (input == "A") {
			println("Enter a name for the new List:")
			var name: String = readLine().toString()
			if (name.isEmpty()) { getPrompt() }
			else {
				TodoList().create(name)
			
				println("List $name created.")
				todolist.loadLists()
				selectedList = todolist.selectList(todolist.all.size - 1)
				todolist.getLists()
			}
		}
			
	   else if (input == "E") {
			if (selectedList == null) { println("Please select a List first.") }
			else {
				println("Old name: ${selectedList!!.name}")
				var newName: String = readLine().toString()
				if (newName.isEmpty()) { getPrompt() }
				else {
					val condition: PultusORMCondition = PultusORMCondition.Builder()
				            .eq("listId", selectedList!!.listId)
				            .build()
							
					val updater: PultusORMUpdater = PultusORMUpdater.Builder()
					            .set("name", newName)
					            .condition(condition)
					            .build()
					
					pultusORM.update(TodoList(), updater)
					todolist.loadLists()
					selectedList!!.name = newName
					todolist.getLists()
				}
			}
		}
			
		else if (input == "DD") {
			if (selectedList == null) { println("Please select a List first.") }
			else {
				val condition: PultusORMCondition = PultusORMCondition.Builder()
			            .eq("listId", selectedList!!.listId)
			            .build()
						
				val updater: PultusORMUpdater = PultusORMUpdater.Builder()
				            .set("archive", 1)
				            .condition(condition)
				            .build()
								
				pultusORM.update(TodoList(), updater)
				todolist.loadLists()
				todolist.getLists()
			}
		}
		
		else if (input == "a") {
			println("Add Item to ${selectedList!!.name}:")
			var itemInput: String = readLine().toString()
			Item().create(itemInput, selectedList!!.listId.toString())
			
			println("Items in ${selectedList!!.name}:")
			listItems = todolist.getItems(selectedList!!.listId.toString())
			printItems()
		}
			
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
			            .build()
						
			val updater: PultusORMUpdater = PultusORMUpdater.Builder()
			            .set(updateField, updateValue)
			            .set("updatedAt", (System.currentTimeMillis() / 1000).toString())
			            .condition(condition)
			            .build()
			
			pultusORM.update(Item(), updater)
			
			println("Items in ${selectedList!!.name}:")
			listItems = todolist.getItems(selectedList!!.listId.toString())
			printItems()
		}
			
	    else if (input == "lx") {
			if (selectedList != null) {
				println("Archived Items in ${selectedList!!.name}:")
				listItems = todolist.getItems(selectedList!!.listId.toString(), true)
				printItems()
			} else { println("Please select a ToDo List first.") }
			
		}
			
	    else if (input == "c") {
			print(CLEAR)
			getPrompt()
		}
			
		else if (input == "SA") {
			println("Set Sync URL:")
			var url: String = readLine().toString()
			if (Settings().isUrlSet()) {
				println("URL is set...")
				Settings().updateUrl(url)
			} else {
				Settings().create("url", url)
			}
			
			getPrompt()
		}
			
		else if (input == "PS") {
			println("Settings:")
			var settings = pultusORM.find(Settings())
			for (it in settings) {
				val setting = it as Settings
				println("\t[${setting.id}] ${setting.key}: ${setting.value}")
			}
			println()
			getPrompt()
		}
			
		else if (input == "PU") {
			println("User:")
			val users = pultusORM.find(User())
			for (it in users) {
				val user = it as User
				var updatedAt = Date(user.updatedAt.toLong() * 1000L);
				val updated = PrettyTime().format(updatedAt)
				println("\t[${user.id}] ${user.firstName} ${user.lastName} <${user.email}>, ${user.gravatarUrl}")
				println("\tLast updated: $updated")
			}
			println()
			getPrompt()
		}
			
		else if (input == "AU") {
			val foundUser = pultusORM.find(User())
			if (foundUser.size == 1) {
				println("User already created.")
				getPrompt()
				continue
			}
			
			println("Add User by typing the following fields separated by commas \",\":")
			println("\tfirstName, lastName, email")
			
			var userInput = readLine().toString().split(",")
			if (userInput.size != 3) {
				println("User not created.")
				getPrompt()						
			} else {
				User().create(userInput[0].trim(), userInput[1].trim(), userInput[2].toLowerCase().trim())
	
				println()
				getPrompt()
			}
		}
			
		else if (input == "EU") {
			val foundUser = pultusORM.find(User())
			if (foundUser.size != 1) {
				println("User not created, please use AU to add a user first.")
				getPrompt()
			}
			else {
				println("Update User by typing the following fields separated by commas \",\":")
				println("firstName, lastName, email")
			
				var userInput = readLine().toString()
				var inputs = userInput.split(",")
				if (userInput.isEmpty() || inputs.size != 3) {
					println("User not udpated.")
					getPrompt()						
				} else {
					
					val condition: PultusORMCondition = PultusORMCondition.Builder()
				            .eq("id", (foundUser[0] as User).id)
				            .build()
							
					val updater: PultusORMUpdater = PultusORMUpdater.Builder()
					            .set("firstName", inputs[0].trim())
							    .set("lastName", inputs[1].trim())
					            .set("email", inputs[2].trim().toLowerCase())
					            .set("gravatarUrl", User().getGravatarUrl(inputs[2].toLowerCase().trim()))
					            .set("updatedAt", (System.currentTimeMillis() / 1000).toString())
					            .condition(condition)
					            .build()
									
					pultusORM.update(User(), updater, ResponseCallback())
					println("User updated.")
					println()
					getPrompt()
				}
			}
		}
			
		else if (input == "S") {
			val url = Settings().getSyncUrl()
			if (url.isEmpty()) {
				println("Sync URL is not set, please set it before attempting sync.")
				getPrompt()
			} else {
				val clientName = Settings().getClient()
				Fuel.upload(url + "?client=$clientName", parameters = listOf("client" to clientName))
					.source { request, url ->
			            File(dbPath, "todopila.db")
			        }
					.responseString { request, response, result ->
			            println("result: ${result}")
						println()
						getPrompt()
			        }
			}
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
	println("\tE to edit the selected ToDo List.")
    println("\tDD to archive the selected ToDo List.")
	println()
	
	println("Item Actions:")
	println("\tl to list Items in the selected ToDo List.")
	println("\te[\$NUMBER] to edit an Item.")
	println("\t+[\$NUMBER] to mark Item as done.")
	println("\t-[\$NUMBER] to mark Item as not done.")
	println("\tD[\$NUMBER] to archive Item.")
	println()
	
	println("Global Actions:")
	println("\tc clear the screen.")
	println("\tS sync to URL.")
	println("\tSA configure the Sync URL.")
	println("\tPS print Settings.")
	println("\tPU print User.")
	println("\tAU add User.")
	println("\tEU edit User.")
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
		var createdAt = Date(item.createdAt.toLong() * 1000L);
		val created = PrettyTime().format(createdAt)
	    println("\t${idx} ($created) [$done]\t${item.content}")
	}
	println()
	getPrompt()
}

class ResponseCallback : Callback {
    override fun onSuccess(type: PultusORMQuery.Type) {
        log("${type.name}", "Success")
    }

    override fun onFailure(type: PultusORMQuery.Type, exception: PultusORMException) {
        log("${type.name}", "Failure")
        exception.printStackTrace()
    }
}