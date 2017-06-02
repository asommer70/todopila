package todopila
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey

//class List(val id: Int, var name: String, var userId: Int, val createdAt: Int)
class TodoList {
    @PrimaryKey
    @AutoIncrement
    var listId: Int = 0
    var name = ""
    var userId = ""
    var createdAt = ""
}

