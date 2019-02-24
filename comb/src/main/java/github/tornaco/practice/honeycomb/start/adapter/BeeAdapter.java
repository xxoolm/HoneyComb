package github.tornaco.practice.honeycomb.start.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.databinding.BeeItemBinding;
import github.tornaco.practice.honeycomb.start.OnBeeItemClickListener;

public class BeeAdapter extends RecyclerView.Adapter<BeeAdapter.BeeViewHolder> {

    private final List<Bee> bees = new ArrayList<>();

    public void updateBee(List<Bee> bees) {
        this.bees.clear();
        this.bees.addAll(bees);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeeViewHolder(BeeItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeeViewHolder holder, int position) {
        holder.beeItemBinding.setBee(bees.get(position));
        holder.beeItemBinding.setListener(new OnBeeItemClickListener() {
            @Override
            public void onBeeItemClick(Bee bee) {
                holder.itemView.getContext().startActivity(bee.getStarter());
            }
        });
        holder.beeItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return bees.size();
    }

    static class BeeViewHolder extends RecyclerView.ViewHolder {
        private BeeItemBinding beeItemBinding;

        public BeeViewHolder(@NonNull BeeItemBinding beeItemBinding) {
            super(beeItemBinding.getRoot());
            this.beeItemBinding = beeItemBinding;
        }

        public BeeItemBinding getBeeItemBinding() {
            return beeItemBinding;
        }
    }
}
