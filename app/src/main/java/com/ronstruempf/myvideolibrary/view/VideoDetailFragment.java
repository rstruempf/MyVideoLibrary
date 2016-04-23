package com.ronstruempf.myvideolibrary.view;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    /**
     * Video bound to this fragment
     */
    private Video video;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Pass in Video or id and provide content provider
        if (getArguments().containsKey(ARG_VIDEO_ID)) {
            // Load the details for the video specified by the fragment
            // argument.
            // TODO: get video
            //video = some method such as controller or content provider - getArguments().getString(ARG_VIDEO_ID));
            // TODO: Test
            //video = new Video.Builder(1, "H.E.A.T.", 1995).location(1).build();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                // TODO: Test
                //appBarLayout.setTitle(video.getNameWithYear());
                appBarLayout.setTitle("H.E.A.T. (1995)");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_detail, container, false);

        // Show the dummy content as text in a TextView.
        // TODO: Test
//        if (video != null) {
//            // TODO: Create detail view with id?
//            ((TextView) rootView.findViewById(R.id.video_detail)).setText(video.getNameWithYear());
//        }

        return rootView;
    }
}
