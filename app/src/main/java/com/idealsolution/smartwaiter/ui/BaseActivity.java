package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.idealsolution.smartwaiter.R;
/**
 * Created by Usuario on 12/05/2015.
 */
public class BaseActivity extends Activity {
    // Not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SINCRONIZAR = 0;
    protected static final int NAVDRAWER_ITEM_SELEC_MESA = 1;
    protected static final int NAVDRAWER_ITEM_TOMAR_PEDIDO = 2;


    // Navigation mDrawerListView:
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView = null;
    private LinearLayout mDrawerMenuLayout;

    private boolean shouldGoInvisible = false;


    protected void onCreate(Bundle savedInstanceState, int resLayoutID) {

        setContentView(resLayoutID);
        super.onCreate(savedInstanceState);
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        mDrawerListView = (ListView) mDrawerMenuLayout.findViewById(R.id.drawer);
        mDrawerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String[] rows = getResources().getStringArray(R.array.drawer_rows);

        //Our application uses Holo.Light, which defaults to
        // light text; ListView also has a dark background.
        // Create a custom context so the views inflated by
        // ListAdapter use Holo.Light, to display them with dark text
        ContextThemeWrapper wrapper =
                new ContextThemeWrapper(this,
                        android.R.style.Theme_Holo_Light);

        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                wrapper, R.layout.drawer_row, rows));
        mDrawerToggle =
                new ActionBarDrawerToggle(
                        this,  //Host Activity
                        mDrawerLayout, //Container to use
                        R.string.drawer_open, //Content description strings
                        R.string.drawer_close) {
                    float mPreviousOffset = 0f;

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        shouldGoInvisible = true;
                        //Update the options menu
                        invalidateOptionsMenu();
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
//                        super.onDrawerStateChanged(newState);
//                        //Update the options menu
//                        invalidateOptionsMenu();
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        //http://stackoverflow.com/questions/18135214/hide-actionbar-menuitems-when-navigation-drawer-slides-for-any-amount
                        //http://stackoverflow.com/questions/18044277/android-navigation-drawer-bug-using-the-sample
                        super.onDrawerSlide(drawerView, slideOffset);
                        if (slideOffset > mPreviousOffset && !shouldGoInvisible) {
                            shouldGoInvisible = true;
                            invalidateOptionsMenu();
                        } else if (mPreviousOffset > slideOffset && slideOffset < 0.5f && shouldGoInvisible) {
                            shouldGoInvisible = false;
                            invalidateOptionsMenu();
                        }
                        mPreviousOffset = slideOffset;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        shouldGoInvisible = false;
                        //Update the options menu
                        invalidateOptionsMenu();
                    }
                };
        //Set the toggle as the drawer's event listener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View row,
                                    int position, long id) {
                goToNavDrawerItem(position);
                mDrawerLayout.closeDrawers();
            }

        });
        if (getSelfNavDrawerItem() != NAVDRAWER_ITEM_INVALID) {
            mDrawerListView.setItemChecked(getSelfNavDrawerItem(), true);
        }
        //Enable home button actions in the ActionBar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = shouldGoInvisible;
        hideMenuItems(menu, !drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void hideMenuItems(Menu menu, boolean visible) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Let the drawer have a crack at the event first
        // to handle home button events
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            //If this was a drawer toggle, we need to update the
            // options menu, but we have to wait until the next
            // loop iteration for the drawer state to change.
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    //Update the options menu
                    invalidateOptionsMenu();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_SINCRONIZAR:
                intent = new Intent(this, MainActivity.class);
                Log.d("Menu", "Sincronizar");
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_SELEC_MESA:
                intent = new Intent(this, MesasActivity.class);
                Log.d("Menu", "Seleccionar Mesa");
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_TOMAR_PEDIDO:
                intent = new Intent(this, TakeOrderActivity.class);
                Log.d("Menu", "Tomar Pedido");
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }
//    private Runnable onNavChange = new Runnable() {
//        @Override
//        public void run() {
//            if (lorem != null && lorem.isVisible()) {
//                mDrawerListView.setItemChecked(0, true);
//            } else if (content != null && content.isVisible()) {
//                mDrawerListView.setItemChecked(1, true);
//            } else {
//                int toClear = mDrawerListView.getCheckedItemPosition();
//                if (toClear >= 0) {
//                    mDrawerListView.setItemChecked(toClear, false);
//                }
//            }
//
//        }
//    };

}
