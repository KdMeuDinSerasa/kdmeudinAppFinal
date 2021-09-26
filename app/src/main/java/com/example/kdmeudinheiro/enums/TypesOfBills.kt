package com.example.kdmeudinheiro.enums

enum class TypesOfBills(val catName: String, val catId: Int) {
    FIX_BILLS("Fixa", 1),
    LEISURE_BILLS("Lazer", 2),
    MONTHLY_BILLS("Mensal", 3),
    EMERGENCY_BILL("Emergencial", 4)
}

enum class StatusBills(val status: Int) {
    NOTPAID(0),
    PAID(1)
}