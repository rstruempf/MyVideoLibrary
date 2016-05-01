package com.ronstruempf.myvideolibrary.view;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ronstruempf.myvideolibrary.R;
import com.ronstruempf.myvideolibrary.model.Video;

/**
 * A fragment representing a single Video detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link VideoDetailActivity}
 * on handsets.
 */
public class VideoDetailFragment extends Fragment {
    /**
     * The argument is the video id of the video to show details for
     */
    public static final String ARG_VIDEO_ID = "video_id";
    public static final String ARG_VIDEO_EDIT = "video_edit";

    /**
     * Video bound to this fragment
     */
    private boolean _edit;
    private Video _video;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(ARG_VIDEO_ID)) {
            return;
        }
        // Load the details for the video specified by the fragment argument
        int videoId;
        try {
            videoId = Integer.parseInt(getArguments().getString(ARG_VIDEO_ID));
            _edit = Boolean.parseBoolean(getArguments().getString(ARG_VIDEO_EDIT));
        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this.getContext(), "Invalid video id given", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        _video = MainActivity.getController().getVideoManager().getVideo(videoId);
        if (_video == null) {
            Toast toast = Toast.makeText(this.getContext(), "Video not found (" + videoId + ")", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(_video.getNameWithYear());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(_edit?R.layout.video_edit:R.layout.video_detail, container, false);
        Spinner location;

        // setup spinner if edit mode
        if (_edit) {
            location = (Spinner)rootView.findViewById(R.id.video_location);
            location.setAdapter(
                    new ArrayAdapter<>(
                            getActivity(),
                            R.layout.list_item, // layout that defines individual list items
                            R.id.list_item,     // TextView id within layout
                            MainActivity.getController().getLocationManager().getAll()
                    )
            );
            if (_video != null) {
                location.setSelection(_video.getLocation());
            }
        }
        if (_video == null) {
            return rootView;
        }
        // setup fields from video
        setField(R.id.video_title, _video.getTitle(), rootView);
        setField(R.id.video_year, String.valueOf(_video.getYear()), rootView);
        if (!_edit) {
            String loc = MainActivity.getController().getLocationManager().getName(_video.getLocation());
            setField(R.id.video_location, loc, rootView);
        }
        setField(R.id.video_rating, String.valueOf(_video.getRating()), rootView);
        setField(R.id.video_description, _video.getDescription(), rootView);
        setField(R.id.video_imdb_url, _video.getIMDbUrl(), rootView);
        return rootView;
    }

    /**
     * Set a field - method depends on edit mode
     *
     * @param fieldId Field to set
     * @param value Value to set
     */
    private void setField(int fieldId, String value, View view) {
        if (_edit) {
            ((EditText)view.findViewById(fieldId)).setText(value);
        }
        else {
            ((TextView)view.findViewById(fieldId)).setText(value);
        }
    }
}
