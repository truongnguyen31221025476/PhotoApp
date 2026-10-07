package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleData {
  public static ArticleList data;
  private Context context;
  private GridView gridview;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  public ArticleData(Context context, GridView gridview) {
    this.context = context;
    this.gridview = gridview;
  }

  public static Article getPhotoFromId(int id) {
    if (data == null || data.getArticles() == null) return null;
    for (Article a : data.getArticles()) {
      if (a.getArticle_id() == id) return a;
    }
    return null;
  }

  /** Lấy tất cả bài viết của một user. */
  public static ArrayList<Article> getArticlesByUserId(int userId) {
    ArrayList<Article> result = new ArrayList<>();
    if (data == null || data.getArticles() == null) return result;
    for (Article a : data.getArticles()) {
      User u = a.getUser();
      if (u != null && u.getId() == userId) result.add(a);
    }
    return result;
  }

  public void loadData(String url, Activity activity) {
    executor.execute(() -> {
      // Toàn bộ việc tải, đọc file và parse JSON chạy trên luồng nền
      File file = Downloader.downloadFile(url, context.getCacheDir());
      if (file == null) return;

      ArticleList parsed = null;
      try {
        parsed = new Gson().fromJson(readText(file), ArticleList.class);
      } catch (IOException | JsonSyntaxException e) {
        e.printStackTrace();
      } finally {
        file.delete(); // dọn file tạm trong cache
      }
      if (parsed == null || parsed.getArticles() == null) return;

      final ArticleList result = parsed;
      // Chỉ cập nhật giao diện trên luồng chính
      activity.runOnUiThread(() -> {
        data = result;
        gridview.setAdapter(new ArticleAdapter(data.getArticles(), context));
      });
    });
  }

  public String readText(File file) throws IOException {
    StringBuilder sb = new StringBuilder();
    // try-with-resources: tự đóng file kể cả khi có lỗi
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    }
    return sb.toString();
  }
}