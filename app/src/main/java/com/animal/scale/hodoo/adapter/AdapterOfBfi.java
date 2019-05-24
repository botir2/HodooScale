package com.animal.scale.hodoo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.animal.scale.hodoo.R;
import com.animal.scale.hodoo.databinding.LayoutBfiBinding;
import com.animal.scale.hodoo.domain.BfiModel;

import java.util.List;

import static com.animal.scale.hodoo.activity.setting.pet.accounts.PetAccountsActivity.TAG;

/**
 * Created by SongSeokwoo on 2019-05-21.
 */
public class AdapterOfBfi extends BaseAdapter {
    public interface OnCheckedListener {
        void onItemChecked( int position, View v );
    }
    private Context context;
    private List<BfiModel> items;
    private LayoutBfiBinding binding;
    private LayoutInflater inflater;
    private OnCheckedListener callback;
    private int[] selected;

    public AdapterOfBfi (Context context, List<BfiModel> items, int[] selected, OnCheckedListener callback) {
        this.context = context;
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callback = callback;
        this.selected = selected;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            binding = DataBindingUtil.inflate(inflater, R.layout.layout_bfi, viewGroup, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (LayoutBfiBinding) view.getTag();
        }
//        binding.bfiImg.setImageResource(R.drawable.d01d);
//        binding.bfiImg.setBackgroundResource(R.drawable.d01d);
        binding.bfiImg.setButtonDrawable(R.drawable.d01d);
        binding.bfiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( callback != null )
                    callback.onItemChecked(i, view);
            }
        });
        if ( selected[i] != 0 )
            binding.bfiImg.setChecked(true);
        binding.bfiImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if ( !compoundButton.isChecked() ) {
                    if (compoundButton.isPressed())
                        compoundButton.setChecked( !compoundButton.isChecked() );
                } else {
                    if (compoundButton.isPressed())
                        compoundButton.setChecked(false);
                }
            }
        });
        return view;
    }
}