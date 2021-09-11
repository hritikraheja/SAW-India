package com.example.saw_india;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.saw_india.modalClasses.RecyclerViewAdaptorForFeeds;
import com.example.saw_india.modalClasses.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class FeedsFragment extends Fragment {
    RecyclerView recyclerView;
    LinkedList<Video> allVideos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feeds_fragment, container, false);
        allVideos = new LinkedList<>();
        recyclerView = view.findViewById(R.id.feedsRecyclerView);
        new GetVideosFromDatabase().execute();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
    class GetVideosFromDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... string) {
            String result = null;
            try {
                URL url = new URL("https://hritikraheja1.github.io/videos.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();
                    String temp;
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp);
                        sb.append("\n");
                    }
                    result = sb.toString();
                } else {
                    result = "ERROR: HTTP NOT OK";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String videoLink;
            String thumbnailLink;
            String iconLink;
            String title;
            String organizationName;
            String description;
            try {
                JSONObject allData = new JSONObject(s);
                JSONArray array = allData.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    videoLink= obj.getString("video_link");
                    thumbnailLink = obj.getString("video_thumbnail_link");
                    iconLink = obj.getString("icon_link");
                    title = obj.getString("title");
                    organizationName = obj.getString("organization_name");
                    description = obj.getString("description");
                    Video video = new Video(videoLink,thumbnailLink,iconLink, title, organizationName, description);
                    allVideos.add(video);
                }
                recyclerView.setAdapter(new RecyclerViewAdaptorForFeeds(allVideos));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}