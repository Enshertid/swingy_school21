package swingy;

import swingy.controller.HeroGenerationController;
import swingy.controller.LevelMapController;
import swingy.model.DAO.DAOFactory;
import swingy.model.DAO.mapper.CharacterMapper;
import swingy.model.character.hero.Hero;
import swingy.utils.GameConfig;
import swingy.utils.ViewMode;
import swingy.utils.exceptions.BreakGameFromKeyboardException;
import swingy.utils.exceptions.LooseGameException;

import java.io.IOException;
import java.util.Properties;

public class Game {
    public static GameConfig mainConfig = new GameConfig();

    public static void main(String[] args) {
        validateStartInput(args);
        
        readConfigFileIfItExists();

        Hero character = generateCharacter();
        LevelMapController levelMapController = new LevelMapController(character);

        launchGameAndHandleResult(character, levelMapController);
    }

    private static void readConfigFileIfItExists() {
        var properties = new Properties();
        mainConfig.setMaxLevel(3);
        try {
            var cl = Game.class.getClassLoader();
            var inputStream = cl.getResourceAsStream("application.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mainConfig.setMaxLevel(Integer.parseInt(properties.getProperty("hero.max.level")));
        } catch (Exception ex){
        }
    }

    private static void launchGameAndHandleResult(Hero character, LevelMapController levelMapController) {
            try {
                levelMapController.gameCycle(character);
            } catch (BreakGameFromKeyboardException exception) {
                System.out.println("game will be saved, goodbye!");
                if (character.getId() == null) {
                    DAOFactory.getHeroDAO().save(CharacterMapper.toEntity(character));
                } else {
                    DAOFactory.getHeroDAO().update(CharacterMapper.toEntity(character));
                }
                System.exit(0);
            } catch (LooseGameException exception) {
                System.out.println("you loose game, goodbye");
            } catch (Exception exception) {
                System.out.println("unexpected exception:");
                exception.printStackTrace();
                System.exit(1);
            }
        }

    private static Hero generateCharacter() {
        try {
           return new HeroGenerationController().heroGenerate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private static void validateStartInput(String[] args) {
        if (args.length == 1 && args[0].equals("console")) {
            mainConfig.setViewMode(ViewMode.CONSOLE);
        } else if (args.length == 1 && args[0].equals("gui")) {
            mainConfig.setViewMode(ViewMode.GUI);
        } else {
            System.out.println("usage:");
            System.out.println("[launch jar] gui");
            System.out.println("[launch jar] console");
            System.exit(1);
        }
    }
}
