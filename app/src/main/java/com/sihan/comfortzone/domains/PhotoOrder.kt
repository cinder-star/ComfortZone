package com.sihan.comfortzone.domains

data class PhotoOrder (
    var filepath: String? = null,
    var datetime: String? = null,
    var ownerId: String? = null,
    var name: String? = null,
    var address: String? = null,
    var mobileNo: String? = null
)