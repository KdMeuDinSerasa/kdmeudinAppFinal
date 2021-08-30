package com.example.kdmeudinheiro.enums

import com.example.kdmeudinheiro.R

enum class TypesOfBills(CatName: String, catId: Int) {
    //All Enum Strings must be at strings archive.

    FIX_BILLS(R.string.Fix_Bill_Label.toString(), 1),
    LEISURE_BILLS(R.string.Leisure_Bill_Label.toString(), 2),

}