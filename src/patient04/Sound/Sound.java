//package patient04.Sound;
//
//import java.io.IOException;
//import org.newdawn.slick.openal.Audio;
//import org.newdawn.slick.openal.AudioLoader;
//import org.newdawn.slick.util.ResourceLoader;
//
///**
// * @author kajdreef
// */
//public class Sound {
//	public Audio wavMusic;
//        public Audio walkEffect;
//        
//        private float volumeMusic = (float) -100;     
//        
//	public Sound(){
//		init();	
//	}
//        
//	public void init() {
//            wavMusic = null;
//            System.out.println(volumeMusic);
//            try {
//                walkEffect = AudioLoader.getAudio( "WAV", ResourceLoader.getResourceAsStream("footsteps_slow.WAV"));
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//	}
//       
//	public void playMainMenuTune(){
//		try {
//                    wavMusic = AudioLoader.getAudio( "WAV", ResourceLoader.getResourceAsStream("test.WAV"));
//		} catch (IOException e){
//                    e.printStackTrace();
//                }
//                wavMusic.playAsMusic(1.0f, volumeMusic, true);
//	}
//        
//        public void playInGameTune(){
//		try {
//                    wavMusic = AudioLoader.getAudio( "WAV", ResourceLoader.getResourceAsStream("test.WAV"));
//		} catch (IOException e){
//                    e.printStackTrace();
//                }
//                wavMusic.playAsMusic(1.0f, volumeMusic, true);
//        }
//        
//        public void playWalkEffect(){
//            walkEffect.playAsSoundEffect(1.0f, 0.3f, false);
//        }
//        
//        public boolean isPlaying(Audio sound){
//            return sound.isPlaying();
//        }
//        
//        public void setVolumeMusic(float volume){
//            volumeMusic = volume;
//        }
//}