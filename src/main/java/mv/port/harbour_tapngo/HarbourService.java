package mv.port.harbour_tapngo;

import retrofit2.Call;
import retrofit2.http.*;


public interface HarbourService{
//    @GET("users/{user}/repos")
//    Call<List<Repo>> listRepos(@Path("user") String user);


    @Headers({
            "Accept: application/json",
            "Authorization: Bearer 3|GyOGWxqrqwYRe4comuaiFJfssliDxzug80W7ozOC9503b071", // TEST
            "App-Location-Id: 2"
    })
    @POST("api/pos/create-sticker")
//    CompletableFuture<Sticker> createSticker(@Body Sticker sticker);
    Call<StickerResponse> createSticker(@Body Sticker sticker);
//    Call<Sticker> createSticker(@Body RequestBody params);

}