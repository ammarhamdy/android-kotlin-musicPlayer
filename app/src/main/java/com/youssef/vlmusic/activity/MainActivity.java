package com.youssef.vlmusic.activity;


//...imports...\\
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youssef.vlmusic.R;
import com.youssef.vlmusic.fragment.CanCalledBack;
import com.youssef.vlmusic.fragment.PlaylistFragment;
import com.youssef.vlmusic.fragment.SelfTrackFragment;
import com.youssef.vlmusic.fragment.TrackFragment;


//...main activity class...\\
public class MainActivity extends AppCompatActivity {


    //...main activity properties...\\
    // the context:
    private Context context;
    // bottom navigation bar:
    private BottomNavigationView bottomNavigationBar;
    // views in this activity:
    private LinearLayout alertMessageLayout;
    private Button permissionGivenButton;
    // fragments.
    private TrackFragment trackFragment;
    private SelfTrackFragment selfTrackFragment;
    private PlaylistFragment playlistFragment;
    // controller activity launcher.
    private ActivityResultLauncher<Intent> launcher;
    public CanCalledBack canCalledBack;
    // pages pointer.
//    static public byte pageNumber;
    public static byte pageNumber;


    //...main activity methods...\\
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVars();
        setActions();
        takePermission();
    }


    private void initVars(){
        // the context of activity.
        context = getApplicationContext();
        // views.
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        alertMessageLayout = findViewById(R.id.alert_message_layout);
        permissionGivenButton = findViewById(R.id.permission_given_button);
        // fragments.
        trackFragment = new TrackFragment();
        selfTrackFragment = new SelfTrackFragment();
        playlistFragment = new PlaylistFragment();
        // pages pointer
        pageNumber = -1;
        // launcher
         launcher = registerForActivityResult(
                 new ActivityResultContracts.StartActivityForResult(),
                 (result)-> canCalledBack.onCalledBack(result)
         );
    }

    private void takePermission(){
        // check if my app already takes have permission.
        if (context.checkCallingOrSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                ==
                PackageManager.PERMISSION_GRANTED)
        {  // if my app already take the permission.
            // show bottom navigation bar.
            showBottomNavigationBar();
        }else{  // if my app does not take the permission yet.
            // request the permission from user.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0)  // if the user give us the permission:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                // show bottom navigation bar.
                showBottomNavigationBar();
            else
                showAlertMessage();
    }


    private void showAlertMessage(){
        alertMessageLayout.setVisibility(View.VISIBLE);
    }

    private void setActions(){
        setBottomNavigationBarActions();
        setOnPermissionGivenButtonClicked();
    }

    private void setBottomNavigationBarActions(){
        bottomNavigationBar.setOnItemSelectedListener(
                item -> {
                    if (item.getItemId() == R.id.tracks_icon)
                        showTrackFragment();
                    else if (item.getItemId() == R.id.playlist_icon)
                        showPlaylistFragment();
                    else if (item.getItemId() == R.id.saved_icon)
                        showSelfTrackFragment();
                    return true;
                }
        );
    }

    private void setOnPermissionGivenButtonClicked(){
        permissionGivenButton.setOnClickListener((view)->takePermission());
    }

    private void showBottomNavigationBar(){
        if (alertMessageLayout.isShown())
            alertMessageLayout.setVisibility(View.GONE);
        bottomNavigationBar.setVisibility(View.VISIBLE);
        bottomNavigationBar.setSelectedItemId(R.id.tracks_icon);
    }

    private void showTrackFragment(){
        if (pageNumber == 0) // do nothing if that fragment still in show.
            return;
        if (pageNumber == 21)
            getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, trackFragment
        ).commit();
        pageNumber = 0;
    }

    private void showSelfTrackFragment(){
        if (pageNumber == 1) // do nothing if that fragment still in show.
            return;
        if (pageNumber == 21)
            getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, selfTrackFragment
        ).commit();
        pageNumber = 1;
    }

    private void showPlaylistFragment(){
        if (pageNumber == 2) // do nothing if that fragment still in show.
            return;
        if (pageNumber == 21)
            getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, playlistFragment, "PF"
        ).commit();
        pageNumber = 2;
    }

    public void launchControllerActivity(){
        launcher.launch(new Intent(this, ControllerActivity.class));
    }

}
