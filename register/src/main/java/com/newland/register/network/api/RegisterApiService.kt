package com.newland.register.network.api

import com.newland.register.bean.result.RegisterResult
import com.newland.core.bean.BaseResultData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
interface RegisterApiService {
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Response<BaseResultData<RegisterResult>>
}