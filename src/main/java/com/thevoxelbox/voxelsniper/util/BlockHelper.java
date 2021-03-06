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
package com.thevoxelbox.voxelsniper.util;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.data.property.block.SolidCubeProperty;

import java.util.Optional;

public class BlockHelper {

    public static boolean isLiquid(BlockState state) {
        Optional<MatterProperty> matter = state.getProperty(MatterProperty.class);
        if (matter.isPresent()) {
            Matter m = matter.get().getValue();
            if (m == Matter.LIQUID) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLiquidOrGas(BlockState state) {
        Optional<MatterProperty> matter = state.getProperty(MatterProperty.class);
        if (matter.isPresent()) {
            Matter m = matter.get().getValue();
            if (m != Matter.SOLID) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSolid(BlockState state) {
        Optional<SolidCubeProperty> matter = state.getProperty(SolidCubeProperty.class);
        if (matter.isPresent()) {
            return matter.get().getValue();
        }
        return false;
    }

}
