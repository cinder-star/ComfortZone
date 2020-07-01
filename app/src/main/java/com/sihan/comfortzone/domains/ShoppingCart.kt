package com.sihan.comfortzone.domains

import io.paperdb.Paper

class ShoppingCart {
    companion object {
        fun addItem(cartItem: CartItem) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {
                targetItem.quantity++
            }
            saveCart(cart)
        }

        fun removeItem(cartItem: CartItem) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem != null) {
                targetItem.quantity--
                if (targetItem.quantity == 0){
                    cart.remove(targetItem)
                }
            }
            saveCart(cart)
        }

        private fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())
        }
        fun getShoppingCartSize(): Int {
            var cartSize = 0
            getCart().forEach {
                cartSize += it.quantity
            }
            return cartSize
        }
    }
}