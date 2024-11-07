package extention

import java.text.NumberFormat
import java.util.*


fun Int.toFormattedPrice(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Format untuk IDR
    return formatter.format(this)
}

fun Double.toFormattedPrice(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Format untuk IDR
    return formatter.format(this)
}