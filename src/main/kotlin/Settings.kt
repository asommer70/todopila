package todopila

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMQuery
import ninja.sakib.pultusorm.core.PultusORMUpdater

class Settings {
    @PrimaryKey
    @AutoIncrement
    var id: Int = 0
    var key = ""
	var value = ""
    var createdAt = ""
	var updatedAt = ""

	fun create(key: String, value: String) {
    	val setting: Settings = Settings()
		setting.key = key
		setting.value = value
		setting.createdAt = (System.currentTimeMillis() / 1000).toString()
		setting.updatedAt = (System.currentTimeMillis() / 1000).toString()
		pultusORM.save(setting)
		println("Settings saved.")
	}
	
	fun isUrlSet(): Boolean {
		val condition: PultusORMCondition = PultusORMCondition.Builder()
			.eq("key", "url")
            .build()
		
		val url = pultusORM.find(Settings(), condition)
		if (url.size == 1) {
			return true
		}
		return false
	}
	
	fun updateUrl(url: String) {
		val condition: PultusORMCondition = PultusORMCondition.Builder()
	        .eq("key", "url")
	        .build()
				
		val updater: PultusORMUpdater = PultusORMUpdater.Builder()
            .set("value", url)
            .condition(condition)
            .build()
		
		pultusORM.update(Settings(), updater)
		println("Sync URL updated.")
	}
}
