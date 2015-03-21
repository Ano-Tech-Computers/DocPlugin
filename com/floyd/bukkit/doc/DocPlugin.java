package com.floyd.bukkit.doc;

import java.io.*;

import java.util.logging.Logger;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Server;
//import org.bukkit.event.Event.Priority;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


//import com.nijikokun.bukkit.Permissions.Permissions;
//import ru.tehkode.permissions.PermissionManager;
// Simply use: if (player.hasPermission("node.subnode"))

import java.util.Scanner;

/**
* Doc plugin for Bukkit
*
* @author FloydATC
*/
public class DocPlugin extends JavaPlugin implements Listener {
//    private final DocPlayerListener playerListener = new DocPlayerListener(this);

//    public static Permissions Permissions = null;
	public static final Logger logger = Logger.getLogger("Minecraft.DocPlugin");

//    public DocPlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
//        super(pluginLoader, instance, desc, folder, plugin, cLoader);
//        // TODO: Place any custom initialization code here
//
//        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
//    }

    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled
    	
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
    	PluginDescriptionFile pdfFile = this.getDescription();
    	logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events
    	
//    	setupPermissions();
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
  //      pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvents(this, this);
                
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }

    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        showFile(player, "plugins/DocPlugin/motd.txt");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args ) {
    	String cmdname = cmd.getName().toLowerCase();
        Player player = null;
        if (sender instanceof Player) {
        	player = (Player)sender;
        }
        
    	if (cmdname.equalsIgnoreCase("motd")) {
    			showFile(player, "plugins/DocPlugin/motd.txt");
    		log(player, "plugins/DocPlugin/motd.txt");
    			return true;
    	}
    	if (cmdname.equalsIgnoreCase("rules")) {
    			showFile(player, "plugins/DocPlugin/rules.txt");
    		log(player, "plugins/DocPlugin/rules.txt");
    			return true;
    	}
    	if (cmdname.equalsIgnoreCase("help")) {
    		if (args.length == 0) {
     			showFile(player, "plugins/DocPlugin/help.txt");
    			log(player, "plugins/DocPlugin/help.txt");
     			return true;
    		} else {
    			String fname = "plugins/DocPlugin/help";
    			for (String word : args) {
    				if (word.matches("^\\w+$")) {	// Prevent special chars and path traversal
    					fname = fname.concat("/" + word.toLowerCase());
    				}
    			}
    			fname = fname.concat(".txt");
    			if (fileExists(fname)) {
    				showFile(player, fname);
    		    	if (player != null) {
    		    		logger.info("[Doc] "+player.getName()+": "+fname);
    		    	}
    			} else {
    				respond(player, "[Doc] No such help entry, sorry");
    		    	if (player != null) {
    		    		logger.warning("[Doc] "+player.getName()+" tried to view "+fname);
    		    	}
    			}
       			return true;
    		}
    	}
    	return false;
    }

    private void showFile(Player player, String fname) {
    	Server server = getServer();
    	ConsoleCommandSender console = server.getConsoleSender();
    	for (String line : loadFile(fname)) {
			line = line.replaceAll("(\\r|\\n)", "");
    		if (player == null) {
    			console.sendMessage("§7[§6Doc§7]§r "+line);
    		} else {
    			player.sendMessage("§7[§6Doc§7]§r "+line);
    		}
    	}
    }

    private ArrayList<String> loadFile(String fname) {
    	ArrayList<String> lines = new ArrayList<String>();
    	try {
    		Scanner scanner = new Scanner(new FileInputStream(fname), "ISO8859_1");
//    		Scanner scanner = new Scanner(new FileInputStream(fname));
    		String line = null;
    		String newline = System.getProperty("line.separator");
    		while (scanner.hasNextLine()){
    			line = scanner.nextLine() + newline;
    			if (!line.matches("^#.*") && !line.matches("")) {
    				lines.add(line);
    			}
    		}		
    	}
    	catch (FileNotFoundException e) {
    		logger.warning("[Doc] File not found: " + fname);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return lines;
    }
    
    private void log(Player player, String filename) {
    	if (player != null) {
    		System.out.println("[Doc] "+player.getName()+": "+filename);
    	}
    }

    private void respond(Player player, String message) {
    	if (player == null) {
    		System.out.println(message);
    	} else {
    		player.sendMessage(message);
    	}
    }

    
    private Boolean fileExists(String filename) {
    	File f = new File(filename);
    	return f.exists();
    }
    

}
