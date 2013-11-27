
package patient04.Sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import patient04.Manager.GameStates;
import patient04.Manager.StateManager;

/**
 * @author kajdreef
 */
public class Sound2 {
    private int NUM_BUFFERS = 3;
    long lastStep = 0;
    long stepTime = 75;
    // Buffers hold sound data
    private IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
    private IntBuffer source = BufferUtils.createIntBuffer(NUM_BUFFERS);
    
    private FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.0f , 0.0f, 0.0f});
    private FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.0f, 0.0f, 0.5f});
    
    private FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});

    private WaveData mainTheme;
    private WaveData walking;
    private WaveData hitGroundSound;
    
   public Sound2(){
       listenerPos.flip();
       listenerVel.flip();
       listenerOri.flip();
       execute();
   }
   
   public int loadALData(){
       AL10.alGenBuffers(buffer);
       
       if(AL10.alGetError() != AL10.AL_NO_ERROR){
            return AL10.AL_FALSE;
       }
       
       mainTheme = WaveData.create("test.wav");
       // Put wav file in buffer
       AL10.alBufferData(buffer.get(0), mainTheme.format, mainTheme.data, mainTheme.samplerate);
       // dispose the waveFile after the wav data was put in the buffer
       mainTheme.dispose();

       walking = WaveData.create("footsteps_slow3.wav");
       AL10.alBufferData(buffer.get(1), walking.format, walking.data, walking.samplerate);
       mainTheme.dispose();
       
       hitGroundSound = WaveData.create("footsteps_slow3.wav");
       AL10.alBufferData(buffer.get(2), walking.format, walking.data, walking.samplerate);
       mainTheme.dispose();
       
       AL10.alGenSources(source);
               
       if (AL10.alGetError() != AL10.AL_NO_ERROR){
           return AL10.AL_FALSE;
       }
       // main theme
       AL10.alSourcei(source.get(0), AL10.AL_BUFFER,    buffer.get(0));
       AL10.alSourcei(source.get(0), AL10.AL_LOOPING,   AL10.AL_TRUE);
       AL10.alSourcef(source.get(0), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(0), AL10.AL_GAIN,      1.0f);
       AL10.alSource (source.get(0), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(0));
       AL10.alSource (source.get(0), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(0));
       
       // walking sound
       AL10.alSourcei(source.get(1), AL10.AL_BUFFER,    buffer.get(1));
       AL10.alSourcei(source.get(1), AL10.AL_LOOPING,   AL10.AL_FALSE);
       AL10.alSourcef(source.get(1), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(1), AL10.AL_GAIN,      1.0f);
       AL10.alSource (source.get(1), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(3));
       AL10.alSource (source.get(1), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(3));
       
       // hitGround sound
       AL10.alSourcei(source.get(2), AL10.AL_BUFFER,    buffer.get(2));
       AL10.alSourcei(source.get(2), AL10.AL_LOOPING,   AL10.AL_FALSE);
       AL10.alSourcef(source.get(2), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(2), AL10.AL_GAIN,      1.0f);
       AL10.alSource (source.get(2), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(6));
       AL10.alSource (source.get(2), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(6));
       
       
       // Do another error check and return.
       if(AL10.alGetError() == AL10.AL_NO_ERROR)
            return AL10.AL_TRUE;

       return AL10.AL_FALSE;   

   }
   
   public void setListenerValues(){
       AL10.alListener(AL10.AL_POSITION, listenerPos);
       AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
       AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
   }
   
   public void setListenerPos(float x, float y, float z){
       AL10.alListener3f(AL10.AL_POSITION, x, y, z);
   }
   
   public void setSourcePos(int s ,float x, float y, float z){
       AL10.alSource3f(source.get(s), AL10.AL_POSITION, x, y, z);
   }
   
   public void killALData(){
       AL10.alDeleteSources(source);
       AL10.alDeleteBuffers(buffer);
   }
   
   public void execute(){
       // initialize OpenAl and clear the error bit.
       try{
          // AL.create(null, 15, 22050, true);
           AL.create();
       } catch (LWJGLException e){
           e.printStackTrace();
           return;
       }
       AL10.alGetError();
    
       setListenerValues();
       loadALData();
   }
   
    public void playTune(){
       loadALData();
       AL10.alSourcePlay(source.get(0));
   }
   
   public void playWalking(){
       loadALData();
//       // check what the state of the source is. If it stopped playing play it again.
//        if (AL10.alGetSourcei(source.get(1), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING)//AL10.AL_PLAYING)
//            System.out.println("check");
//        else
//            AL10.alSourcePlay(source.get(1));
//        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && System.currentTimeMillis() - lastStep < stepTime/2){
//                    AL10.alSourcePlay(source.get(1));
//        }
//        else
        if(System.currentTimeMillis() - lastStep < stepTime){
            System.out.println("check");
        }         
        else
            System.out.println("step");
            lastStep = System.currentTimeMillis();
            AL10.alSourcePlay(source.get(1));
        }
                
                
   
   public void playHitGround(){
     //  loadALData();
       AL10.alSourcePlay(source.get(2));
   }
   
    public void update(){
       long time = Sys.getTime();
       long elapse = 0;
           
           elapse += Sys.getTime() - time;
           time += elapse;
           if(elapse > 5000){
                elapse = 0;
                sourcePos.put(0, sourcePos.get(0) + sourceVel.get(0));
                sourcePos.put(1, sourcePos.get(1) + sourceVel.get(1));
                sourcePos.put(2, sourcePos.get(2) + sourceVel.get(2));

                AL10.alSource(source.get(0), AL10.AL_POSITION, sourcePos);
           
        }
    }
}