package me.yavuz.delta_a_project.utils

import me.yavuz.delta_a_project.model.Product
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CalculateUtils {

    /**
     * Calculates the price without taxes.
     *
     * @param grossPrice gross price of the product
     * @param taxValue tax value of the product
     *
     * @return double value of the net price
     */
    fun calculateNetPrice(grossPrice: Double, taxValue: Double): Double {
        return grossPrice / ((taxValue / 100) + 1)
    }

    /**
     * Calculates total prices of the products.
     * Product has its price and its quantity that added to cart.
     * This method provides calculated total price of the cart items.
     *
     * @param items cart item list
     *
     * @return double value of total price
     */
    fun calculateTotalPrice(items: MutableList<Pair<Product, Int>>): Double {
        return items.sumOf { it.first.price * it.second }
    }

    /**
     * Formatting double values with a [DecimalFormat].
     * Formatted values floating point side will be only one character.
     *
     * Here is an example
     * ```
     * val formattedValue = formatDouble(12.7692)
     * println(formattedValue) // 12.7
     * ```
     *
     * @param value double value for formatting
     *
     * @return string version of formatted decimal value
     */
    fun formatDouble(value: Double): String {
        val df = DecimalFormat("#0.0", DecimalFormatSymbols(Locale.US))
        return df.format(value)
    }
}