package com.sro.schoolmanagementapp.Model

import java.io.Serializable

data class Student(
    var name: String,
    var classs: String,
    var section: String,
    var mobile: String,
    var schoolID: String,
    var roll: String
) : Serializable {
    constructor() : this("", "", "", "", "", "")
}