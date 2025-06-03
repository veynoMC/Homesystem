package eu.veyno.homesystem.Warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private static WarpCommand instance = new WarpCommand();

    private WarpCommand(){

    }

    public static WarpCommand getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player player){
            if(command.getName().equalsIgnoreCase("warp")||command.getName().equalsIgnoreCase("warps")){
                WarpUI.getInstance(player).openMainPage();
            }
        }
        return true;
    }
}
