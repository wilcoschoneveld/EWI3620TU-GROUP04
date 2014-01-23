
package patient04.resources;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;
import patient04.math.Vector;
import patient04.utilities.Buffers;
import patient04.utilities.Logger;

/**
 *
 * @author Wilco
 */
public class Sound {
    private static final String defaultSoundLocation = "resources/sounds/";
    private static final HashMap<String, Integer> buffers = new HashMap<>();
    private static final HashSet<Source> sources = new HashSet<>();
    
    public static void setListenerPosition(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    }
    
    public static void setListenerVelocity(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
    }
    
    public static void setListenerOrientation(float x, float y, float z) {
        // Create a new vector pointing in the negative z direction
        Vector tmp = new Vector(0, 0, -1);
        
        // Rotate the vector according to orientation
        tmp.rotate(y, 0, 1, 0).rotate(x, 1, 0, 0).rotate(z, 0, 0, 1);
        
        // Upload the vector to openAL
        AL10.alListener(AL10.AL_ORIENTATION,
            Buffers.createFloatBuffer(tmp.x, tmp.y, tmp.z, 0, 1, 0));
    }    
  
    public static Source getResource(String wavFile) {
        // Return null if no file is given
        if (wavFile == null)
            return null;
        
        // Check if file is already loaded
        Integer buffer = buffers.get(wavFile);
        
        // Load file if needed
        if (buffer == null) {
            buffer = createBuffer(wavFile);
            buffers.put(wavFile, buffer);
        }
        
        // Create a new source from buffer
        Source source = new Source(buffer);

        // Check if source loaded succesfully
        if (source.source == -1)
            return null;
        
        // Add source to list for easy dispose
        sources.add(source);
        
        return source;
    }
    
    public static void disposeResources() {
        for (Source source : sources)
            AL10.alDeleteSources(source.source);
        
        for (Integer buffer : buffers.values())
            AL10.alDeleteBuffers(buffer);
        
        sources.clear();
        buffers.clear();
    }

    private static Integer createBuffer(String wavFile) {
        // Define file location
        String file = defaultSoundLocation + wavFile;

        // Create a new buffer
        int buffer = AL10.alGenBuffers();

        // Try to load the wav file
        try (InputStream in 
                        = new BufferedInputStream(new FileInputStream(file))) {
            
            switch(file.substring(file.length() - 4).toLowerCase()) {
                case ".wav":
                    // Load the wav data
                    WaveData wav = WaveData.create(in);

                    // Upload the data into the buffer
                    AL10.alBufferData(buffer, wav.format, wav.data, wav.samplerate);

                    // Dispose the wav data
                    wav.dispose(); break;
                case ".ogg":
                    // Load the ogg data
                    OggData ogg = new OggDecoder().getData(in);
                    
                    // Upload the data into the buffer
                    AL10.alBufferData(buffer, ogg.channels > 1 ?
                            AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                                                            ogg.data, ogg.rate);
                    
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Check if the buffer is properly created
        if (AL10.alGetError() != AL10.AL_NO_ERROR) {
            Logger.error("Could not load " + wavFile);
            return null;
        }

        return buffer;
    }
    
    public static final class Source {
        private int source;
        
        private Source(int buffer) {
            // Create a new source
            source = AL10.alGenSources();
            
            // Setup source settings
            AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
            
            // Set default source values
            setPitch(1);
            setGain(1);
            setLooping(false);
            setPosition(0, 0, 0);
            setVelocity(0, 0, 0);
            
            // Check if the source is properly created
            if (AL10.alGetError() != AL10.AL_NO_ERROR) {
                Logger.error("Could not create source from buffer!");
                source = -1;
            }
            
            // End constructor
        }
        
        public Source play() {
            AL10.alSourcePlay(source);
            return this;
        }
        
        public Source pause() {
            AL10.alSourcePause(source);
            return this;
        }
        
        public Source stop() {
            AL10.alSourceStop(source);
            return this;
        }
        
        public Source setPosition(float x, float y, float z) {
            AL10.alSource3f(source, AL10.AL_POSITION, x, y, z);
            return this;
        }
        
        public Source setVelocity(float x, float y, float z) {
            AL10.alSource3f(source, AL10.AL_VELOCITY, x, y, z);
            return this;
        }
        
        public Source setPitch(float pitch) {
            AL10.alSourcef(source, AL10.AL_PITCH, pitch);
            return this;
        }
        
        public Source setGain(float gain) {
            AL10.alSourcef(source, AL10.AL_GAIN, gain);
            return this;
        }
        
        public Source setLooping(boolean loop) {
            AL10.alSourcei(source, AL10.AL_LOOPING, loop ? 1 : 0);
            return this;
        }
    }
}
