package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycity.R;

public class CategoryAdapter extends BaseAdapter {
   private  int []imageList;

   private int[]categorylist;
    private  Context context;
    private LayoutInflater inflater;
    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
public CategoryAdapter(Context context,int []imageList,int []categorylist)
{
    this.context=context;
    this.categorylist=categorylist;
    this.imageList=imageList;
}
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
         inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
            convertView=inflater.inflate(R.layout.categories_layout,null);
        ImageView imageView=convertView.findViewById(R.id.img_category);
        TextView textView=convertView.findViewById(R.id.tv_category_name);
        imageView.setImageResource(imageList[position]);
        textView.setText(categorylist[position]);
        return convertView;
    }
}
