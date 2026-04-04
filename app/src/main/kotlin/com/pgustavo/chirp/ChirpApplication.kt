package com.pgustavo.chirp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChirpApplication

fun main(args: Array<String>) {
	runApplication<ChirpApplication>(*args)
}

//@Component
//class Demo(
//    private val repository: UserRepository
//) {
//    @PostConstruct
//    fun int() {
//        repository.save(
//            UserEntity(
//                email = "gus@test.com",
//                username = "myTest",
//                hashedPassword = "1234"
//            )
//        )
//    }
//}
