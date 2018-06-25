package example

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon

fun main(args: Array<String>) {
    parse()
    render()
}

fun parse() {
    val jsonString = """{"id" : "111", "side" : "buy", "symbol" : "ftusdt"}"""
    val order = klaxon.parse<Order>(jsonString)
    println(order)
}

fun render() {
    val order = Order("111", Symbol("ft", "usdt"), OrderSide.Buy)
    val json = klaxon.toJsonString(order)
    println(json)
}

data class Order(
        val id: String,
        val symbol: Symbol,
        val side: OrderSide
)

enum class OrderSide(val value: String) {
    Buy("buy"), Sell("sell")
}

// IMPORTANT!!!!
fun wrapQuotes(text: String) = "\"" + text + "\""

val orderSideConverter = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == OrderSide::class.java
    }

    override fun fromJson(jv: JsonValue): OrderSide {
        return OrderSide.values().find { it.value == jv.inside } ?: throw IllegalArgumentException("invalid json value: ${jv.inside}")
    }

    override fun toJson(value: Any): String {
        return wrapQuotes((value as OrderSide).value)
    }
}

val symbolConverter = object : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == Symbol::class.java
    }

    override fun fromJson(jv: JsonValue): Any {
        val value = jv.inside as String
        val quote = "usdt"
        if (value.endsWith(quote)) {
            return Symbol(value.removeSuffix("quote"), quote)
        } else {
            throw IllegalArgumentException("Invalid symbol: $value")
        }
    }

    override fun toJson(value: Any): String {
        return wrapQuotes((value as Symbol).toString())
    }

}

data class Symbol(val base: String, val quote: String) {
    override fun toString(): String {
        return base + quote
    }
}

val klaxon = Klaxon().converter(orderSideConverter).converter(symbolConverter)

