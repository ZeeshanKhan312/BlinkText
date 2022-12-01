package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem profile,chat,status;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager=findViewById(R.id.fragment_container);
        tabLayout=findViewById(R.id.tabLayout);
        profile=findViewById(R.id.profile);
        chat=findViewById(R.id.chats);
        status=findViewById(R.id.status);

        PageAdapter pageAdapter= new PageAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1||tab.getPosition()==2)
                    pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    //to add search button in this page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}