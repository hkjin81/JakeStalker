package kr.hkjin.jakestalker;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.hkjin.jakestalker.restapi.GithubService;
import kr.hkjin.jakestalker.restapi.ServiceGenerator;
import kr.hkjin.jakestalker.restapi.model.Repository;
import kr.hkjin.jakestalker.restapi.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RepositoryListAdapter.Delegate {
    private static final String TAG = "MainActivity";

    public static final String USERNAME = "jakewharton";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private User user;
    private RecyclerView listview;
    private RepositoryListAdapter adapter;
    private List<RepositoryItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        listview = (RecyclerView) findViewById(R.id.list);

        adapter = new RepositoryListAdapter(this);
        adapter.setDelegate(this);
        listview.setAdapter(adapter);
        listview.setLayoutManager(new LinearLayoutManager(this));

        requestUser();
        requestRepositories();
    }

    private void requestUser() {
        GithubService gitHubService = ServiceGenerator.createService(GithubService.class);
        Call<User> call = gitHubService.getUser(USERNAME);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    setProfile(user);
                }
                else {
                    Log.d(TAG, String.format("HKCP error: %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, String.format("HKCP requestUser failed: %s", t.toString()));
            }
        });
    }

    private void requestRepositories() {
        GithubService gitHubService = ServiceGenerator.createService(GithubService.class);
        Call<List<Repository>> call = gitHubService.getRepository(USERNAME);
        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.isSuccessful()) {
                    convertToStarSortedList(response.body(), items);
                    adapter.setItems(items);
                }
                else {
                    Log.d(TAG, String.format("HKCP error: %d", response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                Log.d(TAG, String.format("HKCP requestRepo failed: %s", t.toString()));
            }
        });
    }

    private void convertToStarSortedList(List<Repository> repoList, List<RepositoryItem> itemList) {
        itemList.clear();
        Collections.sort(repoList, new RepositoryComparator());
        for (Repository repository : repoList) {
            RepositoryItem item = new RepositoryItem();
            item.title = repository.getName();
            item.imageUrl = repository.getOwner().getAvatarUrl();
            item.description = repository.getDescription();
            item.setStarCount(repository.getStargazersCount());
            item.homepageUrl = repository.getHomepage();
            items.add(item);
        }
    }

    private void setProfile(User user) {
        TextView name = (TextView)findViewById(R.id.profileName);
        name.setText(user.getName());

        TextView email = (TextView)findViewById(R.id.email);
        email.setText(user.getEmail());

        TextView company = (TextView)findViewById(R.id.company);
        company.setText(user.getCompany());

        TextView location = (TextView)findViewById(R.id.location);
        location.setText(user.getLocation());

        TextView blog = (TextView)findViewById(R.id.blog);
        blog.setText(user.getBlog());

        TextView followers = (TextView)findViewById(R.id.followers_count);
        followers.setText(user.getFollowers().toString());

        TextView following = (TextView)findViewById(R.id.following_count);
        following.setText(user.getFollowing().toString());

        TextView repositoriesCount = (TextView)findViewById(R.id.repositories_count);
        repositoriesCount.setText(user.getPublicRepos().toString());

        setProfileImage(user.getAvatarUrl());
    }

    private void setProfileImage(String url) {
        ImageView profileImageView = (ImageView)findViewById(R.id.profileImage);
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_gallery)
                .resizeDimen(R.dimen.profile_image_size, R.dimen.profile_image_size)
                .centerCrop()
                .transform(new CircleTransform())
                .into(profileImageView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClicked(RepositoryItem item, int position) {
        Log.d(TAG, String.format("HKCP homepage url: %s", item.homepageUrl));
    }
}
