package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewArticleActivity extends AppCompatActivity {
  ImageView iv_detail, iv_author_avatar;
  TextView tv_detail_title, tv_detail_description;
  TextView tv_author_name, tv_author_bio, tv_author_count;
  View ll_author;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_article);
    getSupportActionBar().hide();

    iv_detail = findViewById(R.id.iv_detail);
    tv_detail_title = findViewById(R.id.tv_detail_title);
    tv_detail_description = findViewById(R.id.tv_detail_description);
    ll_author = findViewById(R.id.ll_author);
    iv_author_avatar = findViewById(R.id.iv_author_avatar);
    tv_author_name = findViewById(R.id.tv_author_name);
    tv_author_bio = findViewById(R.id.tv_author_bio);
    tv_author_count = findViewById(R.id.tv_author_count);

    int id = (int) getIntent().getLongExtra("id", 0);
    Article article = ArticleData.getPhotoFromId(id);
    if (article == null) { // dữ liệu chưa có (ví dụ app bị hệ thống thu hồi bộ nhớ)
      finish();
      return;
    }

    Picasso.get().load(article.getArticle_image()).resize(400, 500).centerCrop().into(iv_detail);
    tv_detail_title.setText(article.getArticle_title());
    tv_detail_description.setText(article.getArticle_description());

    User user = article.getUser();
    if (user == null) {
      ll_author.setVisibility(View.GONE);
      return;
    }
    tv_author_name.setText(user.getUname());
    tv_author_bio.setText(user.getShort_bio());
    tv_author_count.setText(ArticleData.getArticlesByUserId(user.getId()).size() + " bài viết");

    String avatar = user.getUrl_profile();
    if (avatar != null && !avatar.isEmpty()) {
      Picasso.get().load(avatar).resize(200, 200).centerCrop().into(iv_author_avatar);
    }
  }
}