package services;


import java.util.List;

import model.Event;
import model.Profile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataClient {

    @FormUrlEncoded
    @POST("count_checkin.php")
    Call<String> CountCheckin(@Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("count_ticket.php")
    Call<String> CountTicket(@Field("event_id") String event_id);


    @GET("load_list_mod.php")
    Call<List<Profile>> LoadAllMod(@Query("event_id") String event_id);

    @FormUrlEncoded
    @POST("add_mod.php")
    Call<String> AddMod(@Field("event_id") String event_id
                        , @Field("email") String email);

    @FormUrlEncoded
    @POST("update_checkin.php")
    Call<String> Update_Checkin(@Field("ticket_code") String ticket_code
                                 , @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("update_hand.php")
    Call<String> Update_Hand(@Field("email") String email
            , @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("update_card_ver3.php")
    Call<String> Update_Card(@Field("email") String email
            , @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST("get_profile_checkin.php")
    Call<List<Profile>> Get_Profile_By_Code(@Field("ticket_code") String ticket_code);
//
//    @GET("delete_my_product.php")
//    Call<String> DeleteMyProduct(@Query("product_id") String product_id, @Query("image_path") String image_path);
//
    @GET("load_list_event.php")
    Call<List<Event>> LoadEvent(@Query("email") String email);

    @GET("login.php")
    Call<String> Login(@Query("email") String email,
                       @Query("password") String password);
//
//    @FormUrlEncoded
//    @POST("add_deal.php")
//    Call<String> InsertDeal(@Field("id_account_a") String id_account_a
//            , @Field("id_account_b") String id_account_b
//            , @Field("product_id") String product_id
//            , @Field("wish_b") String wish_b
//            , @Field("note") String note);
//
//
//    @FormUrlEncoded
//    @POST("load_my_deal.php")
//    Call<List<Deal>> LoadMyDeal(@Field("account_id") String account_id);
//
//    @FormUrlEncoded
//    @POST("load_my_product_detail.php")
//    Call<List<Deal>> LoadMyProductDetail(@Field("product_id") String product_id);
//
//    @FormUrlEncoded
//    @POST("load_product_byid.php")
//    Call<List<Product>> LoadProductById(@Field("id") String id);
//
//    @FormUrlEncoded
//    @POST("load_profile_byid.php")
//    Call<List<Account>> LoadProfileById(@Field("id_account_b") String id_account_b);
//
//    @FormUrlEncoded
//    @POST("update_deal.php")
//    Call<String> UpdateDeal(@Field("product_id") String product_id
//            , @Field("id_account_b") String id_account_b
//            , @Field("status") String status);
//
//    @FormUrlEncoded
//    @POST("load_all_deal.php")
//    Call<List<Feed>> LoadAllFeed(@Field("account_id") String account_id);
//
//    @FormUrlEncoded
//    @POST("insert_report.php")
//    Call<String> InsertReport(@Field("account_id") String account_id
//            , @Field("id_post") String id_post
//            , @Field("reason") String reason);
//
//    @FormUrlEncoded
//    @POST("update_rating.php")
//    Call<String> UpdateRating(@Field("account_id") String account_id
//            , @Field("rating") String rating
//            , @Field("transfer_count") String transfer_count);
//
//    @FormUrlEncoded
//    @POST("update_profile.php")
//    Call<String> UpdateProfile(@Field("id") String id
//            , @Field("phone") String phone
//            , @Field("email") String email
//            , @Field("address") String address
//            , @Field("facebook") String facebook);
//
//    @FormUrlEncoded
//    @POST("push_notification.php")
//    Call<String> PushNoti(@Field("account_id") String account_id
//            , @Field("message") String message);
//
//    @FormUrlEncoded
//    @POST("insert_noti.php")
//    Call<String> InsertNoti(@Field("account_id") String account_id
//            , @Field("product_name") String product_name
//            , @Field("content") String content
//            , @Field("image") String image
//    );
//
//    @FormUrlEncoded
//    @POST("load_noti.php")
//    Call<List<Noti>> LoadAllNoti(@Field("account_id") String account_id);
//
//    @FormUrlEncoded
//    @POST("load_history.php")
//    Call<List<Feed>> LoadMyHistory(@Field("account_id") String account_id);

}
