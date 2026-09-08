package com.jasonvosmn.parasiteshud.items;

import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;


public class ItemBase extends Item {
    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS); // Предмет появится во вкладке "Материалы"
        setMaxStackSize(1);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            net.minecraft.entity.player.EntityPlayerMP serverPlayer = (net.minecraft.entity.player.EntityPlayerMP) playerIn;

            int phaseItem = ConfigLoader.getPhase(serverPlayer);
            int pointsItem = ConfigLoader.getTotalPoints(serverPlayer);
            int cooldownItem = ConfigLoader.getCooldown(serverPlayer);
            int pointsNextPhaseItem = ConfigLoader.getPointsNextPhase((byte)phaseItem);

            String message = "§e[SRP INFO] §fPhase: §c" + phaseItem +
                    " §f| Points: §a" + pointsItem +
                    " §f| Cooldown: §c" + cooldownItem +
                    " §f| Next phase: §c" + pointsNextPhaseItem;

            playerIn.sendMessage(new TextComponentString(message));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        // Простое описание
        tooltip.add(TextFormatting.GRAY + "Древний артефакт, реагирующий на паразитов.");

        // Пустая строка для разделения
        tooltip.add("");

        // Цветное описание с подсказкой клавиш
        tooltip.add(TextFormatting.YELLOW + "Нажмите " + TextFormatting.GOLD + "ПКМ" + TextFormatting.YELLOW + ", чтобы узнать стадию эволюции.");

        // Динамическое описание (например, если нажат Shift)
        if (net.minecraft.client.gui.GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.DARK_RED + "Отображение Стадии, Очков, Времени Перезарядки, Очков до следующей стадии)");
        } else {
            tooltip.add(TextFormatting.DARK_GRAY + "Удерживайте " + TextFormatting.WHITE + "SHIFT" + TextFormatting.DARK_GRAY + " для подробностей.");
        }
    }
}