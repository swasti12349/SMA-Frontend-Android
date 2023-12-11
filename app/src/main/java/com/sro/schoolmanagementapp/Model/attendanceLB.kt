package com.sro.schoolmanagementapp.Model

import java.io.Serializable


data class attendanceLB(
    var studentName: String,
    var studentRoll: String

)  {
    constructor() : this("","")
}