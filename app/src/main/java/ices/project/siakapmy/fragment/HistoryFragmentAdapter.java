package ices.project.siakapmy.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HistoryFragmentAdapter extends FragmentStateAdapter {

    public HistoryFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){
            return new DataLogFragment();
        }
        return new EventLogFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}