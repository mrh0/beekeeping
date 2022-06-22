package github.mrh0.beekeeping.blocks.beehive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveBlock extends Block {
    public BeehiveBlock(Properties props) {
        super(props);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return this.mayPlaceOn(level.getBlockState(blockpos), level, blockpos) || mayPlaceNextTo(level, pos);
        return true;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.isFaceSturdy(getter, pos, Direction.UP);
    }

    protected boolean mayPlaceNextTo(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.north()).is(BlockTags.LOGS)
                || level.getBlockState(pos.east()).is(BlockTags.LOGS)
                || level.getBlockState(pos.south()).is(BlockTags.LOGS)
                || level.getBlockState(pos.west()).is(BlockTags.LOGS);
    }
}
