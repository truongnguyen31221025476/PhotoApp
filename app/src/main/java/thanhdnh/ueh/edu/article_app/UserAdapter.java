package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private final ArrayList<User> userList;
    private final Context context;

    public UserAdapter(ArrayList<User> userList,
                       Context context) {

        this.userList = userList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList
                .get(position)
                .getId();
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater =
                    (LayoutInflater)
                            context.getSystemService(
                                    Context.LAYOUT_INFLATER_SERVICE
                            );

            convertView =
                    inflater.inflate(
                            R.layout.article_disp_tpl,
                            parent,
                            false
                    );

            holder = new ViewHolder();

            holder.ivProfile =
                    convertView.findViewById(
                            R.id.iv_profile
                    );

            holder.tvUsername =
                    convertView.findViewById(
                            R.id.tv_username
                    );

            holder.tvBio =
                    convertView.findViewById(
                            R.id.tv_bio
                    );

            holder.tvArticleCount =
                    convertView.findViewById(
                            R.id.tv_article_count
                    );

            convertView.setTag(holder);

        } else {

            holder =
                    (ViewHolder)
                            convertView.getTag();
        }

        User user =
                userList.get(position);

        holder.tvUsername.setText(
                user.getUname()
        );

        holder.tvBio.setText(
                user.getShort_bio()
        );

        int articleCount = 0;

        if (user.getArticles() != null) {
            articleCount =
                    user.getArticles().size();
        }

        holder.tvArticleCount.setText(
                articleCount + " bài viết"
        );

        String avatar =
                user.getUrl_profile();

        if (avatar != null &&
                !avatar.isEmpty()) {

            Picasso.get()
                    .load(avatar)
                    .resize(250, 250)
                    .centerCrop()
                    .into(holder.ivProfile);
        }

        return convertView;
    }

    private static class ViewHolder {

        ShapeableImageView ivProfile;
        TextView tvUsername;
        TextView tvBio;
        TextView tvArticleCount;
    }
}