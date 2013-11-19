package patient04.utilities;


import org.lwjgl.Sys;

public class Timer {
	private final long maxtime = 1000 / 20; //20 fps
	private long prev = 0;
	
	public static long getTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public int deltaTime() {
		return (int) Math.min(-prev + (prev=getTime()), maxtime);
	}
}