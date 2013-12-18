
package patient04.Sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

/**
 * @author kajdreef
 */
public class Sound {
    private final int NUM_BUFFERS;
    
    // Buffers hold sound data
    private IntBuffer buffer;
    private IntBuffer source;
    
    private FloatBuffer sourcePos;
    private FloatBuffer sourceVel;
    
    private FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f});
    private FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});
    
    private String defaultSoundLocation = "res/sounds/";
    private WaveData sound;
    
    private int index;
    
   public Sound(int num){
       
       NUM_BUFFERS = num;
       index = 0;

              // initialize OpenAl and clear the error bit.
       try{
          // AL.create(null, 15, 22050, true);
           AL.create();
       } catch (LWJGLException e){
           e.printStackTrace();
           return;
       }
       AL10.alGetError();
       
       listenerPos.flip();
       listenerVel.flip();
       listenerOri.flip();
       
       setListenerValues();

       buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
       source = BufferUtils.createIntBuffer(NUM_BUFFERS);
       
       sourcePos = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.0f , 0.0f, 0.0f});
       sourceVel = BufferUtils.createFloatBuffer(3*NUM_BUFFERS).put(new float[] {0.4f, 0.4f, 0.4f});
       
       AL10.alGenBuffers(buffer);
       AL10.alGenSources(source);
   }
   
   public int addSound(String soundName, float pitch, float gain , int looping){        // looping = AL10.AL_TRUE/AL10.AL_FALSE       
       String locationFile = defaultSoundLocation + soundName;
       System.out.println(locationFile + "    " + index);
       sound = WaveData.create(locationFile);
       
       //System.out.println(index + "index");
       AL10.alBufferData(buffer.get(index), sound.format, sound.data, sound.samplerate);
       
       // Dispose the WaveData
       sound.dispose();
       
       // sound properties
       AL10.alSourcei(source.get(index), AL10.AL_BUFFER,    buffer.get(index));
       AL10.alSourcei(source.get(index), AL10.AL_LOOPING,   looping);
       AL10.alSourcef(source.get(index), AL10.AL_PITCH,     pitch);
       AL10.alSourcef(source.get(index), AL10.AL_GAIN,      gain);
       AL10.alSource (source.get(index), AL10.AL_POSITION,  (FloatBuffer) sourcePos.position(index));
       AL10.alSource (source.get(index), AL10.AL_VELOCITY,  (FloatBuffer) sourceVel.position(index));
       
       index++;      
       
       // error check
       if(AL10.alGetError() == AL10.AL_NO_ERROR)
            return AL10.AL_TRUE;
       
       return AL10.AL_FALSE;
       
   }

   public void setListenerValues(){
       AL10.alListener(AL10.AL_POSITION, listenerPos);
       AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
       AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
   }
   
   public void setListenerPos(float posX, float posY, float posZ){
       AL10.alListener3f(AL10.AL_POSITION, posX, posY, posZ);
   }
   
   public void setListenerOri( float rotationy){
       // convert the y-rotation to a vector in the direction you are looking at
        float x = (float) -Math.sin(Math.toRadians(rotationy));
        float z = (float) -Math.cos(Math.toRadians(rotationy));
        
        // Use that vector for the orientation
        listenerOri.put(0,x);
        listenerOri.put(1,0);
        listenerOri.put(2,z);
        
        // set Listeners orientation
        AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
   }
   
   public void setSourcePos(int s ,float posX, float posY, float posZ){
       AL10.alSource3f(source.get(s), AL10.AL_POSITION, posX, posY, posZ);
   }
   
   public void killALData(){
       AL10.alDeleteSources(source);
       AL10.alDeleteBuffers(buffer);
   }
   
   public void destroy(){
       killALData();
       AL.destroy();
   }
   
    public void playSound(int geluid ){
       AL10.alSourcePlay(source.get(geluid));
   }
}