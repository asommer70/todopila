package todopila

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey
import ninja.sakib.pultusorm.core.PultusORMCondition
import ninja.sakib.pultusorm.core.PultusORMQuery
import ninja.sakib.pultusorm.core.PultusORMUpdater
import java.security.MessageDigest
import java.math.BigInteger
import javax.xml.bind.DatatypeConverter;

class User {
    @PrimaryKey
    @AutoIncrement
    var id: Int = 0
    var firstName = ""
	var lastName = ""
	var email = ""
	var gravatarUrl = ""
    var createdAt = ""
	var updatedAt = ""
	
	fun create(firstName: String, lastName: String, email: String) {
    	val user: User = User()
		user.firstName = firstName
		user.lastName = lastName
		user.email = email
		user.gravatarUrl = getGravatarUrl(email)
		user.createdAt = (System.currentTimeMillis() / 1000).toString()
		user.updatedAt = (System.currentTimeMillis() / 1000).toString()
		pultusORM.save(user)
		println("User saved.")
	}
	
	fun getGravatarUrl(email: String): String {
		val md = MessageDigest.getInstance("MD5")		
		md.reset();
		md.update(email.toByteArray())
		var digest = md.digest()
		var bigInt = BigInteger(1,digest)
		var hashtext = bigInt.toString(16);

		return "https://www.gravatar.com/avatar/${hashtext}"
	}
}
