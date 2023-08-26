package com.implementing.feedfive.util

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.implementing.feedfive.R
import com.implementing.feedfive.getString
import com.implementing.feedfive.ui.theme.Avenir
import com.implementing.feedfive.ui.theme.Green
import com.implementing.feedfive.ui.theme.Orange
import com.implementing.feedfive.ui.theme.Red
import com.implementing.feedfive.ui.theme.Jost
import com.implementing.feedfive.ui.theme.Rubik


enum class ThemeSettings(val value: Int) {
    LIGHT(0),
    DARK(1),
    AUTO(2)
}

enum class StartUpScreenSettings(val value: Int) {
    DASHBOARD(0),
    SPACES(1)
}
sealed class OrderType(val orderTitle: String) {
    class ASC(val title: String = getString(R.string.ascending)) : OrderType(title)
    class DESC(val title: String = getString(R.string.descending)) : OrderType(title)
}
sealed class Order(val orderType: OrderType, val orderTitle: String){
    abstract fun copy(orderType: OrderType): Order
    data class Alphabetical(val type: OrderType = OrderType.ASC(), val title: String = getString(R.string.alphabetical)) : Order(type, title) {
        override fun copy(orderType: OrderType): Order {
            return this.copy(type = orderType)
        }
    }

    data class DateCreated(val type: OrderType = OrderType.ASC(), val title: String = getString(R.string.date_created)) : Order(type, title) {
        override fun copy(orderType: OrderType): Order {
            return this.copy(type = orderType)
        }
    }

    data class DateModified(val type: OrderType = OrderType.ASC(), val title: String = getString(R.string.date_modified)) : Order(type, title) {
        override fun copy(orderType: OrderType): Order {
            return this.copy(type = orderType)
        }
    }

    data class Priority(val type: OrderType = OrderType.ASC(), val title: String = getString(R.string.priority)) : Order(type, title) {
        override fun copy(orderType: OrderType): Order {
            return this.copy(type = orderType)
        }
    }
}

enum class Priority( @StringRes val title: Int, val color: Color) {
    LOW(R.string.low, Green),
    MEDIUM(R.string.medium, Orange),
    HIGH(R.string.high, Red)
}

enum class ItemView(@StringRes val title: Int, val value: Int) {
    LIST(R.string.list, 0),
    GRID(R.string.grid, 1)
}

fun Int.toNotesView(): ItemView {
    return ItemView.values().first { it.value == this }
}


fun Int.toPriority(): Priority {
    return when (this) {
        0 -> Priority.LOW
        1 -> Priority.MEDIUM
        2 -> Priority.HIGH
        else -> Priority.LOW
    }
}

fun Priority.toInt(): Int {
    return when (this) {
        Priority.LOW -> 0
        Priority.MEDIUM -> 1
        Priority.HIGH -> 2
    }
}

fun Int.toOrder(): Order {
    return when(this){
        0 -> Order.Alphabetical(OrderType.ASC())
        1 -> Order.DateCreated(OrderType.ASC())
        2 -> Order.DateModified(OrderType.ASC())
        3 -> Order.Priority(OrderType.ASC())
        4 -> Order.Alphabetical(OrderType.DESC())
        5 -> Order.DateCreated(OrderType.DESC())
        6 -> Order.DateModified(OrderType.DESC())
        7 -> Order.Priority(OrderType.DESC())
        else -> Order.Alphabetical(OrderType.ASC())
    }
}
fun Order.toInt(): Int {
    return when (this.orderType) {
        is OrderType.ASC -> {
            when (this) {
                is Order.Alphabetical -> 0
                is Order.DateCreated -> 1
                is Order.DateModified -> 2
                is Order.Priority -> 3
            }
        }
        is OrderType.DESC -> {
            when (this) {
                is Order.Alphabetical -> 4
                is Order.DateCreated -> 5
                is Order.DateModified -> 6
                is Order.Priority -> 7
            }
        }
    }
}

fun Int.toFontFamily(): FontFamily {
    return when (this) {
        0 -> FontFamily.Default
        1 -> Avenir
        2 -> Rubik
        3 -> Jost
        else -> FontFamily.Default
    }
}

fun FontFamily.toInt(): Int {
    return when (this) {
        FontFamily.Default -> 0
        Avenir -> 1
        Rubik -> 2
        Jost -> 3
        else -> 0
    }
}

fun FontFamily.getName(): String {
    return when (this) {
        FontFamily.Default ->  getString(R.string.font_system_default)
        Avenir -> "Avenir"
        Rubik -> "Rubik"
        Jost -> "Jost"
        else -> getString(R.string.font_system_default)
    }
}

fun Set<String>.toIntList() = this.toList().map { it.toInt() }
fun MutableList<Int>.addAndToStringSet(id: Int) = apply { add(id) }.map { it.toString() }.toSet()
fun MutableList<Int>.removeAndToStringSet(id: Int) = apply { remove(id) }.map { it.toString() }.toSet()