package com.example.firebasehw

class UserData (
    var name: String,
    var phone: String) {

    constructor(): this("","")

    override fun toString(): String {
        return "Имя: $name, Номер телефона: $phone"
    }
}