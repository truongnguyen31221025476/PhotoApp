package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserData {

    private static final String TAG = "UserData";

    public static UserList data;

    private final Context context;
    private final GridView gridview;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    public UserData(Context context, GridView gridview) {
        this.context = context;
        this.gridview = gridview;
    }

    public static User getUserFromId(int id) {

        if (data == null || data.getUsers() == null) {
            return null;
        }

        for (User user : data.getUsers()) {

            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public void loadData(String url, Activity activity) {

        executor.execute(() -> {

            Log.d(TAG, "Start loading users");

            File file =
                    Downloader.downloadFile(
                            url,
                            context.getCacheDir()
                    );

            if (file == null) {
                Log.e(TAG, "Download failed - file is null");
                return;
            }

            UserList parsedData = null;

            try {

                String json = readText(file);

                Log.d(
                        TAG,
                        "JSON length: " + json.length()
                );

                parsedData =
                        new Gson().fromJson(
                                json,
                                UserList.class
                        );

            } catch (IOException | JsonSyntaxException e) {

                Log.e(
                        TAG,
                        "Error reading/parsing users.json",
                        e
                );

            } finally {

                file.delete();
            }

            if (parsedData == null ||
                    parsedData.getUsers() == null) {

                Log.e(TAG, "User data is null");
                return;
            }

            final UserList result = parsedData;

            Log.d(
                    TAG,
                    "Users loaded: "
                            + result.getUsers().size()
            );

            activity.runOnUiThread(() -> {

                data = result;

                UserAdapter adapter =
                        new UserAdapter(
                                data.getUsers(),
                                context
                        );

                gridview.setAdapter(adapter);

                Log.d(
                        TAG,
                        "User adapter attached"
                );
            });
        });
    }

    public String readText(File file)
            throws IOException {

        StringBuilder result =
                new StringBuilder();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(file),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                result
                        .append(line)
                        .append('\n');
            }
        }

        return result.toString();
    }
}