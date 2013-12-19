/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.resources;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import patient04.math.Vector;
import patient04.utilities.Buffers;

/**
 *
 * @author kajdreef
 */
public class Sound {
    private static Sound soundManager;
    private static final int MAXBUFFERS = 10;
    private static final String defaultSoundLocation = "res/sounds/";
    
    private final IntBuffer buffer;
    private final IntBuffer source;
    private int currentloc;
    
    private Sound() {
        // set current buffer location to zero
        currentloc = 0;
        
        // create new openAL instance
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        // create buffers
        buffer = BufferUtils.createIntBuffer(MAXBUFFERS);
        source = BufferUtils.createIntBuffer(MAXBUFFERS);
        
        // generate openAL buffers
        AL10.alGenBuffers(buffer);
        AL10.alGenSources(source);
    }
    
    public static Sound getManager() {
        if(soundManager == null)
            soundManager = new Sound();
        
        return soundManager;
    }
    
    public void setListenerPosition(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    }
    
    public void setListenerOrientation( float rotationy){
        // convert the y-rotation to a vector in the direction you are looking at
        float x = (float) -Math.sin(Math.toRadians(rotationy));
        float z = (float) -Math.cos(Math.toRadians(rotationy));
        
        // create a floatbuffer
        FloatBuffer buf = Buffers.createFloatBuffer(x, 0, z, 0, 1, 0);
        
        // set Listeners orientation
        AL10.alListener(AL10.AL_ORIENTATION, buf);
   }
    
    public void destroy() {
       AL10.alDeleteSources(source);
       AL10.alDeleteBuffers(buffer);
       AL.destroy();
        
        soundManager = null;
    }
    
    public Short newShort(String name) {
        return new Short(name, 1.0f, 1.0f, AL10.AL_FALSE);
    }
    
    public final class Short {
        Vector position;
        int bufferpos;
        
        public Short(String soundName, float pitch, float gain, int looping) {        // looping = AL10.AL_TRUE/AL10.AL_FALSE       
            //String locationFile = defaultSoundLocation + soundName;

            WaveData sound = WaveData.create(soundName);
            
            bufferpos = currentloc++;

            AL10.alBufferData(buffer.get(bufferpos),sound.format, sound.data, sound.samplerate);

            // Dispose the WaveData
            sound.dispose();

            // sound properties
            AL10.alSourcei(source.get(bufferpos), AL10.AL_BUFFER, buffer.get(bufferpos));
            AL10.alSourcei(source.get(bufferpos), AL10.AL_LOOPING, looping);
            AL10.alSourcef(source.get(bufferpos), AL10.AL_PITCH, pitch);
            AL10.alSourcef(source.get(bufferpos), AL10.AL_GAIN, gain);

            // set initial source position
            setSourcePosition(0, 0, 0);
            setSourceVelocity(0.4f, 0.4f, 0.4f);
        }
        
        public void play() {
            AL10.alSourcePlay(source.get(bufferpos));
        }
        
        public void setSourcePosition(float x, float y, float z) {
            AL10.alSource3f(source.get(bufferpos), AL10.AL_POSITION, x, y, z);
        }
        
        public void setSourceVelocity(float x, float y, float z) {
            AL10.alSource3f(source.get(bufferpos), AL10.AL_VELOCITY, x, y, z);
        }
        
    }
}
