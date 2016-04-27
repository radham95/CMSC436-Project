package edu.umd.radham95.cmsc436_project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Raymond on 4/26/16.
 * For viewpager layout thing
 */
public class DateFragments {
    private static final String TAG = "Date Fragments";
    static ViewGroup rootView;

    static public class dayFragment extends Fragment {
        public dayFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Day Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.day_fragment, container, false);

            return rootView;
        }
    }

    static public class weekFragment extends Fragment {
        public weekFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Week Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.week_fragment, container, false);

            return rootView;
        }
    }

    static public class monthFragment extends Fragment {
        public monthFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Month Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.month_fragment, container, false);

            return rootView;
        }
    }

    static public class yearFragment extends Fragment {
        public yearFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v(TAG, "Year Fragment selected");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.year_fragment, container, false);

            return rootView;
        }
    }
}
