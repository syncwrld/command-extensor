### [Versão em portugûes da Wiki aqui](https://github.com/syncwrld/CommandExtensor) ###
# CommandExtensor
A command framework for Bukkit that is VERY, and I mean VERY, easy to use.

![CommandExtensor](https://socialify.git.ci/syncwrld/CommandExtensor/image?description=1&descriptionEditable=easy-to-use%20bukkit%20command%20framework%20&font=KoHo&forks=1&issues=1&language=1&name=1&owner=1&pattern=Solid&stargazers=1&theme=Dark)

## Adding into your projeto

#### Gradle
```gradle
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation 'com.github.syncwrld:CommandExtensor:1.0-snapshot'
}
```


#### Maven
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.syncwrld</groupId>
	<artifactId>CommandExtensor</artifactId>
        <version>1.0-snapshot</version>
</dependency>
```

## API Documentation

#### Creating a command

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "command1", aliases = {})
  public class MyCommand extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage("You are a player.");
        } else {
            commandSender.sendMessage("You are not a player.");
        }
    }

  }
```
In above example, the `/command1` command is created, and when executed, it checks if the CommandSender is a player or the console.

#### Creating a command with aliases

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "command2", aliases = {"cmd2", "cmd3"})
  public class MyCommand extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender.isOp())) {
            commandSender.sendMessage("Only OP's can execute this command.");
        } else {
            final String name = (commandSender instanceof Player) ? ((Player) commandSender).getName()) : "Console";
            commandSender.sendMessage(String.format("Hello, %s!", name));
        }
    }

  }
```
In the above example, the `/command2` command is created, and it can also be executed using `/cmd2` or `/cmd3`.

#### Creating command that can be executed only by players

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "kickplayer", aliases = {"kick"}, onlyPlayers = true)
  public class MyCommand extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
          Player player = (Player) commandSender;

          if (args.length == 0) {
              player.sendMessage(new String[] {
                  "",
                  "§4§lLothusPunish",
                  " §c/kickplayer [player]",
                  "",
              });
          } else {
              final String targetName = args[0];
              final Player target = Bukkit.getPlayerExact(targetName);

              if (target == null || (!(target.isOnline()))) {
                  player.sendMessage(String.format("§cThe player '%s' is invalid or offline.", targetName));
                  return;
              }

              player.kickPlayer("§4§lLOTHUSPUNISH! §cYou have been kicked by staff " + player.getName() + "!");
          }
      }

  }
```
In the above example, the `/kickplayer` command is created, and it can also be executed using `/kick`, but only by players, not by the console.

#### Registering commands

```java
import me.syncwrld.cmdextensor.framework.CommandFramework;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandFramework commandFramework = new CommandFramework(this, MeuPlugin.class);
        commandFramework.registerAll();
    }

}
```
After using the `registerAll` method, all correctly created commands will be automatically registered.
