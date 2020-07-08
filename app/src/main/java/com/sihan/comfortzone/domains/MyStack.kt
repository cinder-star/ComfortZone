package com.sihan.comfortzone.domains

import java.io.Serializable

class MyStack<T>: Serializable {
    private val elements: ArrayList<T> = arrayListOf()
    private fun isEmpty() = elements.isEmpty()
    fun size() = elements.size
    fun push(item: T) {
        elements.add(item)
    }
    fun pop() {
        if (!isEmpty()){
            elements.removeAt(elements.size - 1)
        }
    }
    fun peek(): T? = elements.lastOrNull()
    fun clear() {
        elements.clear()
    }
    override fun toString(): String = elements.toString()
}