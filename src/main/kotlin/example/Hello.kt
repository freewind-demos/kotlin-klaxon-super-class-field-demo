package example

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

fun main(args: Array<String>) {
    render()
    parse()
}

private fun render() {
    val child = Child("Hello")
    val json = klaxon.toJsonString(child)
    println(json)
}

private fun parse() {
    val json = """{"name" : "Hello", "id" : -99}"""
    val child = klaxon.parse<Child>(json)
    println(child)
}


private var id = 100
private fun nextId(): Int {
    return id++
}

open class Super(@Json(name = "id", ignored = false) val id: Int = nextId())
data class Child(val name: String) : Super()



