
package patient04.Sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import patient04.Manager.StateManager;
import patient04.physics.Vector;

/**
 * @author kajdreef
 */
public class Sound2 {
    private int NUM_BUFFERS = 4;
    private long lastStep = 0;
    private long stepTime = 75;
    
    // Buffers hold sound data
    private IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
    private IntBuffer source = BufferUtils.createIntBuffer(NUM_BUFFERS);
    
    private FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.0f , 0.0f, 0.0f});
    private FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.4f, 0.4f, 0.4f});
    
    private FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});

    private WaveData mainTheme;
    private WaveData walking;
    private WaveData hitGroundSound;
    private WaveData sound3D;
    
    private final int MAIN = 0;
    private final int WALK = 1;
    private final int GROUND = 2;
    private final int SOUND3D = 3;
    
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
       
       mainTheme = WaveData.create("test.wav");             // Stereo
       // Put wav file in buffer
       AL10.alBufferData(buffer.get(0), mainTheme.format, mainTheme.data, mainTheme.samplerate);
       // dispose the waveFile after the wav data was put in the buffer
       mainTheme.dispose();

       walking = WaveData.create("footsteps_slow3.wav");    // mono
       AL10.alBufferData(buffer.get(1), walking.format, walking.data, walking.samplerate);
       walking.dispose();
       
       hitGroundSound = WaveData.create("footsteps_slow5.wav"); //mono
       AL10.alBufferData(buffer.get(2), hitGroundSound.format, hitGroundSound.data, hitGroundSound.samplerate);
       hitGroundSound.dispose();
       
       sound3D = WaveData.create("footsteps_slow5.wav"); //mono
       AL10.alBufferData(buffer.get(3), sound3D.format, sound3D.data, sound3D.samplerate);
       sound3D.dispose();
       
       AL10.alGenSources(source);
               
       if (AL10.alGetError() != AL10.AL_NO_ERROR){
           return AL10.AL_FALSE;
       }
       // main theme
       AL10.alSourcei(source.get(MAIN), AL10.AL_BUFFER,    buffer.get(MAIN));
       AL10.alSourcei(source.get(MAIN), AL10.AL_LOOPING,   AL10.AL_TRUE);
       AL10.alSourcef(source.get(MAIN), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(MAIN), AL10.AL_GAIN,      0.2f);
       AL10.alSource (source.get(MAIN), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(MAIN));
       AL10.alSource (source.get(MAIN), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(MAIN));
       
       // walking sound
       AL10.alSourcei(source.get(WALK), AL10.AL_BUFFER,    buffer.get(WALK));
       AL10.alSourcei(source.get(WALK), AL10.AL_LOOPING,   AL10.AL_FALSE);
       AL10.alSourcef(source.get(WALK), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(WALK), AL10.AL_GAIN,      0.2f);
       AL10.alSource (source.get(WALK), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(3*WALK));
       AL10.alSource (source.get(WALK), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(3*WALK));
       
       // hitGround sound
       AL10.alSourcei(source.get(GROUND), AL10.AL_BUFFER,    buffer.get(GROUND));
       AL10.alSourcei(source.get(GROUND), AL10.AL_LOOPING,   AL10.AL_FALSE);
       AL10.alSourcef(source.get(GROUND), AL10.AL_PITCH,     1.0f);
       AL10.alSourcef(source.get(GROUND), AL10.AL_GAIN,      0.3f);
       AL10.alSource (source.get(GROUND), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(3*GROUND));
       AL10.alSource (source.get(GROUND), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(3*GROUND));
       
       // Test 3D sound
       AL10.alSourcei(source.get(SOUND3D), AL10.AL_BUFFER,    buffer.get(SOUND3D));
       AL10.alSourcei(source.get(SOUND3D), AL10.AL_LOOPING,   AL10.AL_TRUE);
       AL10.alSourcef(source.get(SOUND3D), AL10.AL_PITCH,     0.7f);
       AL10.alSourcef(source.get(SOUND3D), AL10.AL_GAIN,      1.0f);
       AL10.alSource (source.get(SOUND3D), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(3*SOUND3D));
       AL10.alSource (source.get(SOUND3D), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(3*SOUND3D));
       
        // Do another error check and return.
       if(AL10.alGetError() == AL10.AL_NO_ERROR)
            return AL10.AL_TRUE;

       return AL10.AL_FALSE;   

   }
   public void setListenerOri( float ry){
       // convert the y-rotation to a vector in the direction you are looking at
        float x = (float) -Math.sin(Math.toRadians(ry));
        float z = (float) -Math.cos(Math.toRadians(ry));
        
        // Use that vector for the orientation
        listenerOri.put(0,x);
        listenerOri.put(1,0);
        listenerOri.put(2,z);
        AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
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
    
    public void play3dSound(){
        loadALData();
        setSourcePos(SOUND3D, 10, 1, 10);
        AL10.alSourcePlay(source.get(SOUND3D));

    }
   
   public void playWalking(){
       loadALData();
        if(System.currentTimeMillis() - lastStep < stepTime){
        }         
        else{
            lastStep = System.currentTimeMillis();
            AL10.alSourcePlay(source.get(1));
        }
   }        
           
   public void playHitGround(){
       loadALData();
       AL10.alSourcePlay(source.get(2));
   }
}