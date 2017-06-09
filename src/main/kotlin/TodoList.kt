package todopila
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMQuery

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
	@Ignore
	public var selected: Any? = null
	
	fun loadLists() { this.all = pultusORM.find(TodoList()) }
	
    fun getLists() {
		println("ToDo Lists:")
		
		for ((idx, it) in this.all.withIndex()) {
		    val todoList = it as TodoList
			println("\t[$idx] ${todoList.name}");
	//	    println(ansi().fg(GREEN).a("\t[$idx] ${todoList.name}").reset())
	//		println("$GREEN\t[$idx] $MAGENTA${todoList.name}$RESET")
		}
	    println()
	    getPrompt()
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
	
	fun selectList(idx: Int): TodoList {
//		this.selected = this.all[idx]
//		this.selected = this.all[idx] as TodoList
//		val name = this.selected
//		val name = this.all[idx].name
//		println("Selected ${this.selected.name}.")
		return this.all[idx] as TodoList
	}
	
	fun getItems(listId: String): MutableList<Any> {
		val condition: PultusORMCondition = PultusORMCondition.Builder()
            .eq("listId", listId)
            .and()
			.eq("archive", 0)
            .sort("createdAt", PultusORMQuery.Sort.ASCENDING)
            .build()
		
		return pultusORM.find(Item(), condition)
	}
}

