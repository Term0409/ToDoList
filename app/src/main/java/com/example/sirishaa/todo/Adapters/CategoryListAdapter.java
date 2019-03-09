package com.example.sirishaa.todo.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.sirishaa.todo.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter {

    ArrayList<String> catList;
    Context mContext;
    Typeface heavy_tf;
    StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

    public CategoryListAdapter(Context c) {
        this.mContext = c;
        this.catList = new ArrayList<>();
        heavy_tf = Typeface.createFromAsset(c.getAssets(), "fonts/avenir_heavy.ttf");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.position = position;
        String task = catList.get(position);
        ((ItemViewHolder) holder).title.setTypeface(heavy_tf);
        ((ItemViewHolder) holder).title.setText(task);

        ((ItemViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    //((SimpleItemViewHolder) holder).title.setPaintFlags(((SimpleItemViewHolder) holder).title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    animateStrikeThrough1(((ItemViewHolder) holder).title);
                    ((ItemViewHolder) holder).title.setAlpha(0.5f);
                    ((ItemViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    reverseAnimateStrikeThrough1(((ItemViewHolder) holder).title);
                    ((ItemViewHolder) holder).title.setAlpha(1);
                    ((ItemViewHolder) holder).title.setTextColor(mContext.getResources().getColor(R.color.black));
                    //((SimpleItemViewHolder) holder).title.setPaintFlags(((SimpleItemViewHolder) holder).title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

    }

    public void forceNotify(ArrayList<String> catList) {
        this.catList.clear();
        this.catList.addAll(catList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    private void animateStrikeThrough1(final TextView tv) {
        final int ANIM_DURATION = 500;              //duration of animation in millis
        final int length = tv.getText().length();
        new CountDownTimer(ANIM_DURATION, ANIM_DURATION / length) {
            Spannable span = new SpannableString(tv.getText());


            @Override
            public void onTick(long millisUntilFinished) {
                //calculate end position of strikethrough in textview
                int endPosition = (int) (((millisUntilFinished - ANIM_DURATION) * -1) / (ANIM_DURATION / length));
                endPosition = endPosition > length ?
                        length : endPosition;
                span.setSpan(strikethroughSpan, 0, endPosition,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(span);
            }

            @Override
            public void onFinish() {


            }
        }.start();
    }

    private void reverseAnimateStrikeThrough1(final TextView tv) {
        final int ANIM_DURATION = 500;              //duration of animation in millis
        final int length = tv.getText().length();
        new CountDownTimer(ANIM_DURATION, ANIM_DURATION / length) {
            Spannable span = new SpannableString(tv.getText());

            @Override
            public void onTick(long millisUntilFinished) {
                //calculate end position of strikethrough in textview
                /*int endPosition = (int) (((millisUntilFinished-ANIM_DURATION)*-1)/(ANIM_DURATION/length));
                endPosition = endPosition > length ?
                        length : endPosition;
                span.setSpan(strikethroughSpan, 0, endPosition,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/
                span.removeSpan(strikethroughSpan);
                tv.setText(span);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public final class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int position;
        TextView title;
        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            checkBox = itemView.findViewById(R.id.done);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
