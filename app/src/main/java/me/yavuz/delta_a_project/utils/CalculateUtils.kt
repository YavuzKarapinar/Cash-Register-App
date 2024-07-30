package me.yavuz.delta_a_project.utils

import me.yavuz.delta_a_project.model.Product
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CalculateUtils {

    fun calculateNetPrice(grossPrice: Double, taxValue: Double): Double {
        return grossPrice / ((taxValue / 100) + 1)
    }

    fun calculateTotalPrice(items: MutableList<Pair<Product, Int>>): Double {
        return items.sumOf { it.first.price * it.second }
    }

    fun formatDouble(value: Double): String {
        val df = DecimalFormat("#0.0", DecimalFormatSymbols(Locale.US))
        return df.format(value)
    }
}