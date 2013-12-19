
package patient04.Sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import patient04.utilities.Buffers;

/**
 * @author kajdreef
 */
public class Sound {
    private static Sound soundManager;
    private final int NUM_BUFFERS = 10;
    private static final String defaultSoundLocation = "res/sounds/";
    
    // Buffers hold sound data
    private IntBuffer buffer;
    private IntBuffer source;

    private WaveData sound;
    
    private int currentloc;
    
   public Sound(){
       
       currentloc = 0;

              // initialize OpenAl and clear the error bit.
       try{
          // AL.create(null, 15, 22050, true);
           AL.create();
       } catch (LWJGLException e){
           e.printStackTrace();
           return;
       }
       AL10.alGetError();

       buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
       source = BufferUtils.createIntBuffer(NUM_BUFFERS);
       
       AL10.alGenBuffers(buffer);
       AL10.alGenSources(source);
   }
   
   public int addSound(String soundName, float pitch, float gain , int looping){        // looping = AL10.AL_TRUE/AL10.AL_FALSE       
       WaveData sound = null;
       
       try {
           sound = WaveData.create(
                    new BufferedInputStream(new FileInputStream(defaultSoundLocation + soundName)));
       } catch(Exception e) {}
       
       //System.out.println(index + "index");
       AL10.alBufferData(buffer.get(currentloc), sound.format, sound.data, sound.samplerate);
       
       // Dispose the WaveData
       sound.dispose();
       
       // sound properties
       AL10.alSourcei(source.get(currentloc), AL10.AL_BUFFER,    buffer.get(currentloc));
       AL10.alSourcei(source.get(currentloc), AL10.AL_LOOPING,   looping);
       AL10.alSourcef(source.get(currentloc), AL10.AL_PITCH,     pitch);
       AL10.alSourcef(source.get(currentloc), AL10.AL_GAIN,      gain);
       AL10.alSource3f(source.get(currentloc), AL10.AL_POSITION,  0, 0, 0);
       AL10.alSource3f(source.get(currentloc), AL10.AL_VELOCITY,  0, 0, 0);
       
       currentloc++;      
       
       // error check
       if(AL10.alGetError() == AL10.AL_NO_ERROR)
            return AL10.AL_TRUE;
       
       return AL10.AL_FALSE;
       
   }
   
   public void setListenerPos(float posX, float posY, float posZ){
       AL10.alListener3f(AL10.AL_POSITION, posX, posY, posZ);
   }
   
   public void setListenerOri( float rotationy){
       // convert the y-rotation to a vector in the direction you are looking at
        float x = (float) -Math.sin(Math.toRadians(rotationy));
        float z = (float) -Math.cos(Math.toRadians(rotationy));
        
        // create a floatbuffer
        FloatBuffer buf = Buffers.createFloatBuffer(x, 0, z, 0, 1, 0);
        
        // set Listeners orientation
        AL10.alListener(AL10.AL_ORIENTATION, buf);
   }
   
   public void setSourcePos(int s ,float posX, float posY, float posZ){
       AL10.alSource3f(source.get(s), AL10.AL_POSITION, posX, posY, posZ);
   }
   
   public void destroy() {
       AL10.alDeleteSources(source);
       AL10.alDeleteBuffers(buffer);
       AL.destroy();
        
       soundManager = null;
    }
   
    public void playSound(int geluid ){
       AL10.alSourcePlay(source.get(geluid));
   }
}