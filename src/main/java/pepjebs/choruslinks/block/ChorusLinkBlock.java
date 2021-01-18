package pepjebs.choruslinks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pepjebs.choruslinks.block.entity.ChorusLinkBlockEntity;

public class ChorusLinkBlock extends Block implements BlockEntityProvider {

    public ChorusLinkBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ChorusLinkBlockEntity();
    }
}
