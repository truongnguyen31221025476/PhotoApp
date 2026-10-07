package thanhdnh.ueh.edu.article_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  public GridView gridview;

  private final AdapterView.OnItemClickListener
          onItemClickListener =
          new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {

              Intent intent =
                      new Intent(
                              MainActivity.this,
                              ViewArticleActivity.class
                      );

              long userId =
                      gridview
                              .getAdapter()
                              .getItemId(position);

              intent.putExtra(
                      "user_id",
                      userId
              );

              startActivity(intent);
            }
          };

  @Override
  protected void onCreate(
          Bundle savedInstanceState
  ) {

    super.onCreate(savedInstanceState);

    setContentView(
            R.layout.activity_main
    );

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    gridview =
            findViewById(
                    R.id.gridview
            );

    String url =
            "https://raw.githubusercontent.com/" +
                    "truongnguyen31221025476/" +
                    "PhotoApp/master/users.json";

    new UserData(
            getBaseContext(),
            gridview
    ).loadData(
            url,
            this
    );

    gridview.setOnItemClickListener(
            onItemClickListener
    );
  }
}