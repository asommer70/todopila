package todopila
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey

class TodoList {
    @PrimaryKey
    @AutoIncrement
    var listId: Int = 0
    var name = ""
    var userId = ""
	var archive = false
    var createdAt = ""
	@Ignore
    var all = mutableListOf<Any>()
	public var selected: Any? = null
	
    fun getLists() {
		println("ToDo Lists:")
		
		this.all = pultusORM.find(TodoList())
		for ((idx, it) in this.all.withIndex()) {
		    val todoList = it as TodoList
			println("\t[$idx] ${todoList.name}");
	//	    println(ansi().fg(GREEN).a("\t[$idx] ${todoList.name}").reset())
	//		println("$GREEN\t[$idx] $MAGENTA${todoList.name}$RESET")
		}
	    println()
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
	
	fun selectList(idx: Int): Any {
		this.selected = this.all[idx]
		return this.selected!!
	}
}

