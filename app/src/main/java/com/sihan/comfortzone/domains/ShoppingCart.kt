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
                cartItem.quantity++
                targetItem.quantity++
            }
            saveCart(cart)
        }

        fun bulkAdd(product: Product, quantity: Int) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == product.id }
            if (targetItem == null) {
                cart.add(CartItem(product, quantity))
            } else {
                targetItem.quantity += quantity
            }
            saveCart(cart)
        }

        fun removeItem(cartItem: CartItem) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem != null) {
                cartItem.quantity--
                targetItem.quantity--
                if (targetItem.quantity == 0) {
                    cart.remove(targetItem)
                }
            }
            saveCart(cart)
        }

        fun completelyRemoveItem(cartItem: CartItem) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem != null) {
                cart.remove(targetItem)
            }
            saveCart(cart)
        }

        fun updateItem(product: Product) {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.product.id == product.id }
            if (targetItem != null) {
                targetItem.product = product
            }
            saveCart(cart)
        }

        fun bulkUpdate(products: MutableList<Product>) {
            val cart = getCart()
            products.forEach { product ->
                val targetItem = cart.singleOrNull {
                    it.product.id == product.id
                }
                if (targetItem != null) {
                    targetItem.product = product
                }
            }
            cart.forEach { cartItem ->
                val targetItem = products.singleOrNull {
                    it.id == cartItem.product.id
                }
                if (targetItem == null) {
                    cart.remove(cartItem)
                }
            }
            saveCart(cart)
        }

        fun clearCart() {
            val newCart: MutableList<CartItem> = mutableListOf()
            Paper.book().write("cart", newCart)
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