package com.sro.schoolmanagementapp.Model

import java.io.Serializable


data class School(
    var adminName: String,
    var mobileNumber: Int?,
    var schoolName: String,
    var schoolType: String,
    var schoolCode: String
):Serializable {

    constructor() : this("", null, "", "", "")
}
