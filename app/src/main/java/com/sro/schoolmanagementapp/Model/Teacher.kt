package com.sro.schoolmanagementapp.Model

import java.io.Serializable

data class Teacher(
    var name: String,
    var subject: String,
    var schoolCode: String,
    var gender: String,
    var mobile: String,
    var classs: String,
    var section: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", "")
}