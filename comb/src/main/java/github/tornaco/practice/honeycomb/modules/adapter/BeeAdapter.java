package github.tornaco.practice.honeycomb.modules.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.tornaco.practice.honeycomb.data.Bee;
import github.tornaco.practice.honeycomb.databinding.BeeItemCheckableBinding;
import github.tornaco.practice.honeycomb.databingding.BeeWireable;
import github.tornaco.practice.honeycomb.modules.ModulesViewModel;
import lombok.Setter;

public class BeeAdapter extends RecyclerView.Adapter<BeeAdapter.BeeViewHolder>
        implements BeeWireable {

    @Setter
    private ModulesViewModel viewModel;
    private final List<Bee> bees = new ArrayList<>();

    @Override
    public void updateBee(List<Bee> bees) {
        this.bees.clear();
        this.bees.addAll(bees);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeeViewHolder(BeeItemCheckableBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeeViewHolder holder, int position) {
        holder.beeItemBinding.setBee(bees.get(position));
        holder.beeItemBinding.setListener(bee -> holder.beeItemBinding.moduleSwitch.performClick());
        holder.beeItemBinding.moduleSwitch.setOnClickListener(view -> {
            boolean checked = ((Checkable) view).isChecked();
            viewModel.setModuleActivated(bees.get(holder.getAdapterPosition()), checked);
        });
        holder.beeItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return bees.size();
    }

    static class BeeViewHolder extends RecyclerView.ViewHolder {
        private BeeItemCheckableBinding beeItemBinding;

        BeeViewHolder(@NonNull BeeItemCheckableBinding beeItemBinding) {
            super(beeItemBinding.getRoot());
            this.beeItemBinding = beeItemBinding;
        }

    }
}
