import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Model.FileObj
import com.sro.schoolmanagementapp.Model.ResponseObject
import com.sro.schoolmanagementapp.Model.School
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Model.Teacher
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiService {

    @POST("/api/v1/school/addSchool")
    fun postSchool(@Body school: School): Call<ResponseObject>

    @POST("/api/v1/student/addStudent")
    fun postStudent(@Body student: Student): Call<ResponseObject>

    @POST("/api/v1/teacher/addTeacher")
    fun postTeacher(@Body teacher: Teacher): Call<ResponseObject>

    @POST("/api/v1/student/loginStudent/{mobile}")
    fun loginStudent(@Path("mobile") mobile: String): Call<Student>

    @POST("/api/v1/teacher/loginTeacher/{mobile}")
    fun loginTeacher(@Path("mobile") mobile: String): Call<Teacher>

    @POST("/api/v1/attendance/add")
    fun markAttendance(@Body attendance: Attendance): Call<ResponseObject>

    @Streaming
    @GET("/api/v1/attendance/download/{mobile}")
    fun getAttendancePdf(@Path("mobile") mobile: String): Call<ResponseBody>

    @GET("/api/v1/attendance/attendanceleaderboard")
    fun getAttendanceLeaderboardList(
        @Query("schoolcode") schoolCode: String,
        @Query("studentclass") studentClass: String
    ): Call<List<Attendance>>

    @GET("/api/v1/teacher/classAttendance")
    suspend fun getClassAttendance(
        @Query("schoolCode") schoolCode: String,
        @Query("studentClass") studentClass: String
    ): Response<List<Attendance>>

    @Multipart
    @POST("/api/files/uploadFileToServer")
    suspend fun uploadFileToServer(
        @Query("type") fileType: String,
        @Query("fileName") fileName: String,
        @Query("schoolName") schoolName: String,
        @Query("className") className: String,
        @Query("sectionName") sectionName: String,
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>

    @POST("/api/files/getAllFiles")
    suspend fun getFiles(
        @Query("schoolName") schoolName: String,
        @Query("className") className: String,
        @Query("sectionName") sectionName: String,
        @Query("fileType") fileType: String
    ): Response<List<FileObj>>

    @Streaming
    @POST("/api/files/getFile")
    fun downloadFile(
        @Query("schoolName") schoolName: String,
        @Query("className") className: String,
        @Query("sectionName") sectionName: String,
        @Query("fileName") fileName: String,
        @Query("fileType") fileType: String
    ): Call<ResponseBody>

}