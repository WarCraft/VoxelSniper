/*
 * This file is part of VoxelSniper, licensed under the MIT License (MIT).
 *
 * Copyright (c) The VoxelBox <http://thevoxelbox.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.brush.Brush;
import com.thevoxelbox.voxelsniper.brush.BrushManager;
import com.thevoxelbox.voxelsniper.player.PlayerData;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BrushCommand implements CommandCallable {

    @Override
    public CommandResult process(CommandSource src, String args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextColors.RED, "Player only."));
            return CommandResult.success();
        }
        if (args == null || args.isEmpty()) {
            PlayerData data = PlayerData.get(((Player) src).getUniqueId());
            src.sendMessage(Text.of("Current brush: " + data.getCurrentBrush().getName()));
            return CommandResult.success();
        }
        String brush = args;
        String brushArgs = "";
        if (args.indexOf(' ') != -1) {
            brush = args.substring(0, args.indexOf(' '));
            brushArgs = args.substring(args.indexOf(' ') + 1);
        }
        Brush<?> brushInstance = BrushManager.get().getBrush(brush);
        if (brushInstance == null) {
            src.sendMessage(Text.of(TextColors.RED, "Brush not found"));
            return CommandResult.success();
        }
        brushInstance = brushInstance.create(brushArgs);
        PlayerData data = PlayerData.get(((Player) src).getUniqueId());
        data.setCurrentBrush(brushInstance);
        src.sendMessage(Text.of("Your brush has been set to " + brushInstance.getName()));
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        // TODO tab completion
        return Collections.emptyList();
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("voxelsniper.command.brush");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of("Set your brush"));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of("Set your brush"));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("/brush [brush [args...]]");
    }

}