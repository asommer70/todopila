package todopila

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey

class Item {
    @PrimaryKey
    @AutoIncrement
    var itemId: Int = 0
    var content = ""
	var listId = ""
    var userId = ""
	var status = false
	var archive = false
    var createdAt = ""
	var updatedAt = ""
	
	fun create(content: String, listId: String) {
    	val newItem: Item = Item()
		newItem.content = content
		newItem.listId = listId
		newItem.userId = "0"
		newItem.status = false
		newItem.archive = false
		newItem.createdAt = (System.currentTimeMillis() / 1000).toString()
		newItem.updatedAt = (System.currentTimeMillis() / 1000).toString()
		pultusORM.save(newItem)
		println("Item saved.")
	}
}