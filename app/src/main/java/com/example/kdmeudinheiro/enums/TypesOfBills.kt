package com.example.kdmeudinheiro.enums

import androidx.core.content.res.TypedArrayUtils.getString
import com.example.kdmeudinheiro.R

enum class TypesOfBills(val catName: String, val catId: Int) {
    //All Enum Strings must be at strings archive. or not

    FIX_BILLS("Fixa", 1),
    LEISURE_BILLS("Lazer", 2),
    MONTHLY_BILLS("Mensal", 3),
    EMERGENCY_BILL("Emergencial", 4)


}