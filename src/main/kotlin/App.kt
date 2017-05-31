
package todopila
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
				print("List set to:  " + args[0])
		    }
		}	
	}
			
	println("Welcome to ToDo Pila!")
	println("press h for a list of commands.")
	
	// TODO:as Display a list of Lists.
	
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