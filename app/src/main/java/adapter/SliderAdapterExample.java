package adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycity.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.SliderItem;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context,List<SliderItem>list) {
            this.context = context;
            mSliderItems=list;
        }

        public void renewItems(List<SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        public void deleteItem(int position) {
            this.mSliderItems.remove(position);
            notifyDataSetChanged();
        }

        public void addItem(SliderItem sliderItem) {
            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SliderItem sliderItem = mSliderItems.get(position);
            viewHolder.tvImageDes.setText(sliderItem.getImageDes());
            Picasso.get().load(sliderItem.getImageUrl()).into(viewHolder.imageAdv);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getCount() {
            //slider view count could be dynamic size
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageAdv;

            TextView tvImageDes;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                 imageAdv=itemView.findViewById(R.id.img_adv);
                 tvImageDes=itemView.findViewById(R.id.tv_image_des);
                this.itemView = itemView;
            }
        }

    }

