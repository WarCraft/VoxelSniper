package com.thevoxelbox.voxelsniper.brush;

import com.google.common.base.Objects;
import com.thevoxelbox.voxelsniper.VoxelMessage;
import com.thevoxelbox.voxelsniper.snipe.SnipeData;
import com.thevoxelbox.voxelsniper.snipe.Undo;
import com.thevoxelbox.voxelsniper.util.UndoDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#The_Tree_Brush
 *
 * @author Mick
 */
public class TreeSnipeBrush extends Brush {

    private TreeType treeType = TreeType.TREE;

    /**
     *
     */
    public TreeSnipeBrush() {
        this.setName("Tree Snipe");
    }

    @SuppressWarnings("deprecation")
    private void single(final SnipeData v, Block targetBlock) {
        UndoDelegate undoDelegate = new UndoDelegate(targetBlock.getWorld());
        Block blockBelow = targetBlock.getRelative(BlockFace.DOWN);
        BlockState currentState = blockBelow.getState();
        undoDelegate.setBlock(blockBelow);
        blockBelow.setType(Material.GRASS);
        this.getWorld().generateTree(targetBlock.getLocation(), this.treeType, undoDelegate);
        Undo undo = undoDelegate.getUndo();
        blockBelow.setBlockData(currentState.getBlockData().getMaterial().createBlockData(), true);
        undo.put(blockBelow);
        v.owner().storeUndo(undo);
    }

    private int getYOffset() {
        for (int i = 1; i < (getTargetBlock().getWorld().getMaxHeight() - 1 - getTargetBlock().getY()); i++) {
            if (Objects.equal(getTargetBlock().getRelative(0, i + 1, 0).getType(), Material.AIR)) {
                return i;
            }
        }
        return 0;
    }

    private void printTreeType(final VoxelMessage vm) {
        String printout = "";

        boolean delimiterHelper = true;
        for (final TreeType treeType : TreeType.values()) {
            if (delimiterHelper) {
                delimiterHelper = false;
            } else {
                printout += ", ";
            }
            printout += ((treeType.equals(this.treeType)) ? ChatColor.GRAY + treeType.name().toLowerCase() : ChatColor.DARK_GRAY + treeType.name().toLowerCase()) + ChatColor.WHITE;
        }

        vm.custom(printout);
    }

    @Override
    protected final void arrow(final SnipeData v) {
        Block targetBlock = getTargetBlock().getRelative(0, getYOffset(), 0);
        this.single(v, targetBlock);
    }

    @Override
    protected final void powder(final SnipeData v) {
        this.single(v, getTargetBlock());
    }

    @Override
    public final void info(final VoxelMessage vm) {
        vm.brushName(this.getName());
        this.printTreeType(vm);
    }

    @Override
    public final void parseParameters(final String triggerHandle, final String[] params, final SnipeData v) {
        if (params[1].equalsIgnoreCase("info")) {
            v.sendMessage(ChatColor.GOLD + "Tree Snipe Brush Parameters:");
            v.sendMessage(ChatColor.AQUA + "/b " + triggerHandle + " [treeType]  -- Change tree type");
            return;
        }

        try {
            this.treeType = TreeType.valueOf(params[0].toUpperCase());
            this.printTreeType(v.getVoxelMessage());
        } catch (Exception e) {
            v.getVoxelMessage().brushMessage(ChatColor.RED + "That tree type does not exist. Use " + ChatColor.LIGHT_PURPLE + " /b " + triggerHandle + " info " + ChatColor.GOLD + " to see brush parameters.");
        }
    }

    @Override
    public void registerSubcommandArguments(HashMap<Integer, List<String>> subcommandArguments) {
        List<String> arguments = new ArrayList<>();

        for (TreeType t : TreeType.values()) {
            arguments.add(t.name().toLowerCase());
        }

        subcommandArguments.put(1, arguments);

        super.registerSubcommandArguments(subcommandArguments); // super must always execute last!
    }

    @Override
    public String getPermissionNode() {
        return "voxelsniper.brush.treesnipe";
    }
}
