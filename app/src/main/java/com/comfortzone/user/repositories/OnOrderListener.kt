package com.comfortzone.user.repositories

import com.comfortzone.user.domains.Order

interface OnOrderListener {
    fun onOrderClick(order: Order)
}