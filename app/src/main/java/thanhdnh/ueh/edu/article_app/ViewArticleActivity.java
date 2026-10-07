package thanhdnh.ueh.edu.article_app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewArticleActivity
        extends AppCompatActivity {

  ShapeableImageView ivUserProfile;

  TextView tvUserName;
  TextView tvUserBio;
  TextView tvRelatedCount;

  LinearLayout llRelatedArticles;

  @Override
  protected void onCreate(
          Bundle savedInstanceState
  ) {

    super.onCreate(savedInstanceState);

    setContentView(
            R.layout.activity_view_article
    );

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    ivUserProfile =
            findViewById(
                    R.id.iv_user_profile
            );

    tvUserName =
            findViewById(
                    R.id.tv_user_name
            );

    tvUserBio =
            findViewById(
                    R.id.tv_user_bio
            );

    tvRelatedCount =
            findViewById(
                    R.id.tv_related_count
            );

    llRelatedArticles =
            findViewById(
                    R.id.ll_related_articles
            );

    int userId =
            (int) getIntent()
                    .getLongExtra(
                            "user_id",
                            0
                    );

    User user =
            UserData.getUserFromId(
                    userId
            );

    if (user == null) {
      finish();
      return;
    }

    displayUser(user);
  }

  private void displayUser(User user) {

    tvUserName.setText(
            user.getUname()
    );

    tvUserBio.setText(
            user.getShort_bio()
    );

    String avatar =
            user.getUrl_profile();

    if (avatar != null &&
            !avatar.isEmpty()) {

      Picasso.get()
              .load(avatar)
              .resize(300, 300)
              .centerCrop()
              .into(ivUserProfile);
    }

    ArrayList<Article> articles =
            user.getArticles();

    if (articles == null) {

      tvRelatedCount.setText(
              "Bài viết liên quan (0)"
      );

      return;
    }

    tvRelatedCount.setText(
            "Bài viết liên quan (" +
                    articles.size() +
                    ")"
    );

    for (Article article : articles) {

      addArticleView(article);
    }
  }

  private void addArticleView(
          Article article
  ) {

    TextView articleView =
            new TextView(this);

    String content =
            article.getArticle_title()
                    + "\n"
                    + article.getArticle_description();

    articleView.setText(content);

    articleView.setTextColor(
            Color.WHITE
    );

    articleView.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            15
    );

    articleView.setBackgroundColor(
            Color.parseColor("#1E1E1E")
    );

    int padding =
            dpToPx(12);

    articleView.setPadding(
            padding,
            padding,
            padding,
            padding
    );

    LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

    params.setMargins(
            0,
            0,
            0,
            dpToPx(10)
    );

    articleView.setLayoutParams(
            params
    );

    llRelatedArticles.addView(
            articleView
    );
  }

  private int dpToPx(int dp) {

    return (int)
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    getResources()
                            .getDisplayMetrics()
            );
  }
}