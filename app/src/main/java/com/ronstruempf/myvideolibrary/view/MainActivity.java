package com.ronstruempf.myvideolibrary.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ronstruempf.myvideolibrary.R;
import com.ronstruempf.myvideolibrary.controller.VideoLibraryController;
import com.ronstruempf.myvideolibrary.dal.DBHandler;
import com.ronstruempf.myvideolibrary.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Videos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VideoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainActivity extends AppCompatActivity {
    private String LOG_TAG = MainActivity.class.getSimpleName() + "_LOGTAG";
    private static VideoLibraryController _controller;
    private SimpleItemRecyclerViewAdapter _viewAdapter;

    /*
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean wideScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getTitle());
        }

        // The detail container view will be present only in the large-screen (single page) layouts
        // If this view is present, then the activity should be in single page/two-pane mode
        wideScreen = (findViewById(R.id.video_detail_container) != null);

        RecyclerView _recyclerView = (RecyclerView)findViewById(R.id.video_list);
        assert _recyclerView != null;
        _viewAdapter = new SimpleItemRecyclerViewAdapter(new ArrayList<Video>());
        _recyclerView.setAdapter(_viewAdapter);

        //
        // Launch background thread to query service for weather data
        //
        Log.d(LOG_TAG, "Launching background thread to query video list");
        new InitializationTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.add_movie:
                addNewMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Display an edit form for adding a new video
     */
    private void addNewMovie() {
        displayVideoDetails(0, true);
    }

    /**
     * Display the details view for a video
     *
     * @param id Video to display
     */
    private void displayVideoDetails(int id, boolean edit) {
        /**
         * Here's where pop-up vs. widescreen happens.
         *  - If wide screen, video_list has a FrameLayout called video_detail_container that gets
         *    replaced below with VideoDetailFragment, which inflates a video_detail as its root
         *    view (RelativeLayout)
         *  - For single pane with pop-up detail, a VideoDetailActivity object is started, which
         *    loads an activity_video_detail layout, which is simply a scrollable view container
         *    called video_detail_container, into which is loaded a VideoDetailFragment just like
         *    occurs below for the wide screen mode
         */
        if (wideScreen) {
            Bundle arguments = new Bundle();
            arguments.putString(VideoDetailFragment.ARG_VIDEO_ID, String.valueOf(id));
            arguments.putString(VideoDetailFragment.ARG_VIDEO_EDIT, String.valueOf(edit));
            VideoDetailFragment fragment = new VideoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_detail_container, fragment)
                    .commit();
        } else {
            Context context = this; // this.getApplicationContext();
            Intent intent = new Intent(context, VideoDetailActivity.class);
            intent.putExtra(VideoDetailFragment.ARG_VIDEO_ID, String.valueOf(id));
            intent.putExtra(VideoDetailFragment.ARG_VIDEO_EDIT, String.valueOf(edit));
            context.startActivity(intent);
        }
    }


    public static VideoLibraryController getController() {
        return _controller;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Video> mValues;

        public SimpleItemRecyclerViewAdapter(List<Video> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setVideo(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayVideoDetails(holder.getVideo().getId(), false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * Set a new list of videos
         *
         * @param videos List of videos to set
         */
        public void setList(ArrayList<Video> videos) {
            mValues.clear();
            mValues.addAll(videos);
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mVideoName;
            private Video mVideo;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mVideoName = (TextView) view.findViewById(R.id.list_content);
            }

            public void setVideo(Video video) {
                mVideo = video;
                mVideoName.setText(mVideo.getNameWithYear());
            }

            public Video getVideo() {
                return mVideo;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mVideoName.getText() + "'";
            }
        }
    }

    /**
     * Startup Task - initialize DB and load data on background thread
     */
    private class InitializationTask extends AsyncTask<Void, Void, Void> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        @Override
        protected Void doInBackground(Void... params) {
            // Allow database initialization and other controller setup to occur on a background thread
            DBHandler dbmgr = new DBHandler(MainActivity.this);
            _controller = new VideoLibraryController(dbmgr, dbmgr);
            _controller.getVideoManager().cacheAll();
            return null;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        @Override
        protected void onPostExecute(Void result) {
            _viewAdapter.setList(_controller.getVideoManager().getCache());
        }

    }


}
