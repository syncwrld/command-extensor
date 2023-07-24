
# CommandExtensor
Uma framework de comando para Bukkit MUITO, mas MUITO fácil de usar.

## Adicionando ao projeto

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
</dependency>
```

## Documentação da API

#### Criando um comando

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "comando1", aliases = {})
  public class MeuComando extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage("Você é um jogador");
        } else {
            commandSender.sendMessage("Você não é um jogador.");
        }
    }

  }
```
No caso acima, o comando `/comando1` é criado, e quando acionado retorna se o CommandSender é um jogador ou um console.

#### Criando um comando com aliases

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "comando2", aliases = {"cmd2", "cmd3"})
  public class MeuComando extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender.isOp())) {
            commandSender.sendMessage("Apenas OP's podem executar este comando.");
        } else {
            final String name = (commandSender instanceof Player) ? ((Player) commandSender).getName()) : "Console";
            commandSender.sendMessage(String.format("Salve, %s!", name));
        }
    }

  }
```
No caso acima, o comando `/comando2` é criado, e também pode ser executado utilizando `/cmd2` ou `/cmd3`

#### Criando um comando apenas para jogadores

```java
  import me.syncwrld.cmdextensor.framework.SimpleCommand;
  import me.syncwrld.cmdextensor.framework.CommandBuilder;
  
  @CommandBuilder(command = "expulsar", aliases = {"kick"}, onlyPlayers = true)
  public class MeuComando extends SimpleCommand {

      @Override
      public void runCommand(CommandSender commandSender, String[] args) {
          Player player = (Player) commandSender;

          if (args.length == 0) {
              player.sendMessage(new String[] {
                  "",
                  "§4§lLothusPunish",
                  " §c/expulsar [jogador]",
                  "",
              });
          } else {
              final String targetName = args[0];
              final Player target = Bukkit.getPlayerExact(targetName);

              if (target == null || (!(target.isOnline()))) {
                  player.sendMessage(String.format("§cO jogador '%s' é inválido ou está offline.", targetName));
                  return;
              }

              player.kickPlayer("§4§lLOTHUSPUNISH! §cVocê foi expulso pelo staff " + player.getName() + "!");
          }
      }

  }
```
No caso acima, o comando `/expulsar` é criado, e também pode ser executado utilizando `/kick` e pode ser executado apenas por jogadores, não por console.

#### Registrando um comando

```java
import me.syncwrld.cmdextensor.framework.CommandFramework;

public class MeuPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandFramework commandFramework = new CommandFramework(this, MeuPlugin.class);
        commandFramework.registerAll();
    }

}
```
Após ter usado o método `registerAll`, todos os comandos que forem criados corretamente serão registrados automaticamente.
