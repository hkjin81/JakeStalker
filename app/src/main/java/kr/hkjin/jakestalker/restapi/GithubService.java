package kr.hkjin.jakestalker.restapi;

import kr.hkjin.jakestalker.restapi.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hkjin81 on 2017. 4. 24..
 */

public interface GithubService {
    String BASE_URL = "https://api.github.com/";

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
}
