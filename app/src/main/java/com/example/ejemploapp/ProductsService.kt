package com.example.ejemploapp



import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ProductsService {
    @POST("auth/local")

    fun GetToken (@Body()credentials: Credentials): Call<TokenInfo>

//    @GET("products")
//    fun getProducts(@Query("_limit") limit:Int =20,@Header("Authorization")token :String):Call<List<Product>>

    @GET("products")
    fun getProducts(@Query("_limit") limit:Int =10):Call<List<Product>>
//
//    @GET("products")
//    fun getProductsById(@Query("id") id:Int ):Call<List<Product>>

    @FormUrlEncoded
    @POST("products")
    fun addProduct(
        @Field("name") name:String,
        @Field("model") model:String,
        @Field("price") price:Float,
        @Field("description") description:String
    ):Call<Product>

    @PUT("products/{id}")
    fun updateProduct(
        @Path("id") id:Int,@Body producto:Product):Call<Product>

    @DELETE("products/{id}")
    fun delete(
        @Path ("id") id:Int
    ):Call<Unit>

}
data class Credentials (var identifier: String, var password :String)

public class TokenInfo{
    lateinit var jwt : String
}

public class RetrofitHelper {
    companion object{
        fun getInstance(baseUrl:String): ProductsService{
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
            return   retrofit.create(ProductsService::class.java)

        }
        fun getAunthenticatedInstance(baseUrl:String,token: String): ProductsService{
           val okHttpClient = OkHttpClient.Builder()
               .addInterceptor { chain ->
                   val newRequest = chain.request().newBuilder()
                       .addHeader("Authorization","Bearer "+ token)
                       .build()
                   chain.proceed(newRequest)
               }
               .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return   retrofit.create(ProductsService::class.java)

        }
        interface IResult<T> {
            fun onSuccess(items: T?)
            fun onError(exception: Exception)
        }


    }
}


@Parcelize
data class Product( val id:Int,
val name:String,
val model:String,
val price:Float,
val description:String): Parcelable

