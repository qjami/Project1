package cn.app.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import lib.base.EActivity;
import cn.yhub.app.R;
import cn.app.ui.fragment.GalleryFragment;
import cn.app.ui.fragment.DeviceScanFragment;
import cn.app.ui.fragment.MineFragment;

public class MainActivity extends EActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.app.FragmentManager fManager;
    private DeviceScanFragment deviceScanFragment;
    private GalleryFragment galleryFragment;
    private MineFragment mineFragment;
    @Override
    public void initData(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fManager = getFragmentManager();
        deviceScanFragment = DeviceScanFragment.newInstance();
        galleryFragment = GalleryFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fManager.beginTransaction().replace(R.id.ly_content, deviceScanFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();*/

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            if (deviceScanFragment != null)
                fManager.beginTransaction().replace(R.id.ly_content, deviceScanFragment).commit();
        } else if (id == R.id.nav_slideshow) {
            if (mineFragment != null)
                fManager.beginTransaction().replace(R.id.ly_content, mineFragment).commit();
        } else if (id == R.id.nav_gallery) {
            if (galleryFragment != null)
                fManager.beginTransaction().replace(R.id.ly_content, galleryFragment).commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Object newP() {
        return null;
    }
}
