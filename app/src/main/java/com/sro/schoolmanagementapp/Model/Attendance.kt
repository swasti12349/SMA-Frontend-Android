package com.sro.schoolmanagementapp.Model

import java.io.Serializable

data class Attendance(
    var schoolCode: String,
    var studentClass: String,
    var studentMobile: String,
    var studentName: String,
    var studentDates: String,
    var studentRoll: String

) : Serializable {
    constructor() : this("", "", "", "", "", "")
}