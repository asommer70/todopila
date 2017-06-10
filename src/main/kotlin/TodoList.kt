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
	
	fun loadLists() {
		val condition: PultusORMCondition = PultusORMCondition.Builder()
			.eq("archive", 0)
            .sort("createdAt", PultusORMQuery.Sort.ASCENDING)
            .build()
		
		this.all = pultusORM.find(TodoList(), condition)
	}
	
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
	
	fun create(name: String) {
		val newList: TodoList = TodoList()
		newList.name = name
		newList.userId = "0"
		newList.createdAt = (System.currentTimeMillis() / 1000).toString()
		pultusORM.save(newList)
	}
	
	fun selectList(idx: Int): TodoList {
		return this.all[idx] as TodoList
	}
	
	fun getItems(listId: String, archives: Boolean = false): MutableList<Any> {
		var archived = 0
		if (archives != false) archived = 1
		
		val condition: PultusORMCondition = PultusORMCondition.Builder()
            .eq("listId", listId)
            .and()
			.eq("archive", archived)
            .sort("createdAt", PultusORMQuery.Sort.ASCENDING)
            .build()
		
		return pultusORM.find(Item(), condition)
	}
}

