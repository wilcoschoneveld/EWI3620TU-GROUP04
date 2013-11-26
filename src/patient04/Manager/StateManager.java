package patient04.Manager;

import patient04.Sound.Sound2;
import patient04.States.MainMenu;
import patient04.States.MazeRunner;


/************************
 * @author kajdreef     *
 ************************/

public class StateManager {

    public MainMenu main = new MainMenu();
    public MazeRunner runner = new  MazeRunner();
    public static Sound2 sound1 = new Sound2();
    public static GameStates State;
    private static GameStates pState;

    
    public StateManager(GameStates state){
        StateManager.State = state;
    }
    
    // updates state to new state. So you are able to switch from menu to game for example
    public void StateUpdate(GameStates state) {
        if(pState != null) {
            switch(pState) {
                case IN_GAME:
                    runner.destroy();
                    break;
            }
        }

        switch(state){
            case MAIN_MENU:
                loadMain();
                break;
            case PAUSE:
                break;
            case SETTINGS:
                break;
            case IN_GAME: 
                loadGame();
                break;
            case QUIT:
                break;
        }
        pState = state;
    }
    // get previous state
    public GameStates getpState(){
        return pState;
    }
    // get state
    public GameStates getState(){
        return State;
    }
    
    public void setState(GameStates state){
        StateManager.State = state;
    }
    
    public void loadMain(){
        main.initialize();
        sound1.killALData();
        sound1.playTune();
    }
    
    public void loadGame(){ 
       runner.initialize();
       sound1.killALData();
       sound1.playTune();
    }
}