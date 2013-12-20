package patient04.level;

import patient04.level.elements.Usable;
import patient04.level.elements.Solid;
import patient04.level.elements.Prop;
import patient04.level.elements.Elevator;
import java.io.*;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import patient04.level.elements.Enemy;

import patient04.resources.Model;
import patient04.level.elements.Waypoint;
import patient04.rendering.Renderer;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;

public class Level {   
    // Gravity vectors
    public static final Vector GRAVITY = new Vector(0, -1, 0);
    
    // Friction vectors
    public static final Vector FRICTION_FLY = new Vector(0.6f, 0.6f, 0.6f);
    public static final Vector FRICTION_GROUND = new Vector(0.6f, 1, 0.6f);
    public static final Vector FRICTION_AIR = new Vector(0.98f, 0.98f, 0.98f);
    
    // Wall height
    public static final float WALL_HEIGHT = 3f;
    
    private final ArrayList<Solid> solids;
    private final ArrayList<Light> lights;
    private final ArrayList<Entity> entities;
    private final ArrayList<Usable> usables;
    
    public final ArrayList<Waypoint> waypoints;
    public final Vector startPoint = new Vector();
    
    public Level() {
        this.solids = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.waypoints = new ArrayList<>();
        this.usables = new ArrayList<>();
    }
    
    public void addSolid(Solid solid) {
        solids.add(solid);
    }
    
    public void addUsable(Usable usable) {
        usables.add(usable);
    }
    
    public void removeUsable(Usable usable) {
        usables.remove(usable);
    }
    
    public ArrayList<Usable> getUsables() {
        return usables;
    }
    
    public Light addNewLight() {
        Light light = new Light();
        lights.add(light);
        return light;
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public Waypoint addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        
        return waypoint;
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for (Solid obj : solids) 
            if(broadphase.intersects(obj.aabb))
                aabbs.add(obj.aabb);

        return aabbs;
    }
    
    public void drawGeometry(Renderer renderer) {
        for (Solid solid : solids)
            solid.draw(renderer);
        
        for (Entity entity : entities)
            entity.draw(renderer);
        
        for (Usable usable : usables)
            usable.draw(renderer);
    }
    
    public void drawLights(Renderer renderer) {
        for (Light light : lights)
            light.draw(renderer);
        
        for (Entity entity : entities)
            entity.drawLight(renderer);
        
        for (Usable usable : usables)
            usable.drawLight(renderer);
    }
    
    public void drawNavPoints(Renderer renderer) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        renderer.glUpdateModelMatrix(null);
        
        GL11.glBegin(GL11.GL_LINES);
        for(Waypoint wp : waypoints) {
            for(Waypoint np : wp.neighbors) {
                GL11.glColor4f(0, 1, 0, 0.3f);
                GL11.glVertex3f(wp.position.x, wp.position.y, wp.position.z);
                GL11.glVertex3f(np.position.x, np.position.y, np.position.z);
            }
            GL11.glColor4f(1, 0, 0, 0.9f);
            GL11.glVertex3f(wp.position.x, wp.position.y, wp.position.z);
            GL11.glVertex3f(wp.position.x, wp.position.y + 1, wp.position.z);
        }
        GL11.glEnd();
    }
    
    public void update(float dt) {
        for (Entity entity : entities)
            entity.update(dt);
        
        for (Entity entity : entities)
            entity.integrate();
    }
    
    public void cleanup() {
        // TODO?
    }
    
    public void generateFloor(String textureFile) {
        Float xmin = null, zmin = null,
                xmax = null, zmax = null;
        
        for (Solid obj : solids) {
            if(xmin == null || obj.aabb.pos.x + obj.aabb.min.x <  xmin)
                xmin = obj.aabb.pos.x + obj.aabb.min.x;
            if(zmin == null || obj.aabb.pos.z + obj.aabb.min.z <  zmin)
                zmin = obj.aabb.pos.z + obj.aabb.min.z;
            if(xmax == null || obj.aabb.pos.x + obj.aabb.max.x >  xmax)
                xmax = obj.aabb.pos.x + obj.aabb.max.x;
            if(zmax == null || obj.aabb.pos.z + obj.aabb.max.z >  zmax)
                zmax = obj.aabb.pos.z + obj.aabb.max.z;
        }
        
        if (xmin == null || zmin == null || xmax == null || zmax == null) {
            Logger.error("Could not generate floor!");
            return;
        }
        
        Vector min = new Vector(xmin, -1f, zmin);
        Vector max = new Vector(xmax, 0, zmax);
        
        Solid floor = new Solid();
        
        floor.model = Model.buildFloor(min, max, textureFile);
        floor.model.compileBuffers();
        floor.model.releaseRawData();
        
        floor.aabb = new AABB(floor.position, min, max);
        floor.culling = false;
        
        solids.add(floor);
    }
    
    public static Level fromFile(String file) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Level level = new Level();
            
            String line; String[] tokens;
            
            while ((line = reader.readLine()) != null) {
                // Split line
                tokens = line.split("\\s+"); 
                
                if(line.length() == 0 || tokens.length < 1)
                    continue;
                
                switch (tokens[0].toLowerCase()) {
                    case "camera": break;
                    case "wall":
                        float w = Float.parseFloat(tokens[3]);
                        float h = Float.parseFloat(tokens[4]);
                        
                        Vector min = new Vector(-w/2, 0, -h/2);
                        Vector max = new Vector(w/2, WALL_HEIGHT, h/2);
                        
                        Model model =
                                Model.buildWall(min, max, "wall_hospital.png");
                        
                        model.compileBuffers();
                        model.releaseRawData();
                        
                        Solid wall = new Solid();
                        
                        wall.aabb = new AABB(wall.position, min, max);
                        wall.model = model;
                        wall.position.set(
                                Float.parseFloat(tokens[1]), 0,
                                Float.parseFloat(tokens[2]));
                        wall.culling = false;
                        
                        level.addSolid(wall);
                        
                        break;
                    case "light":
                        
                        level.addNewLight()
                                .setPosition(Float.parseFloat(tokens[1]),
                                             WALL_HEIGHT * 2 / 3,
                                             Float.parseFloat(tokens[2]))
                                .setColor(Float.parseFloat(tokens[3]),
                                          Float.parseFloat(tokens[4]))
                                .setIntensity(Float.parseFloat(tokens[5]) * 5)
                                .setEnvironmentLight();
                        
                        break;
                    case "enemy":
                        Enemy enemy = new Enemy(level);
                        
                        enemy.setPosition(Float.parseFloat(tokens[1]), 0,
                                           Float.parseFloat(tokens[2]));
                        
                        enemy.setRotation(0, Float.parseFloat(tokens[3]), 0);
                        enemy.selectNearestWaypoint();
                        
                        level.addEntity(enemy);
                        
                        break;
                    case "waypoint":
                        Waypoint waypoint = new Waypoint(new Vector(
                                Float.parseFloat(tokens[1]), 0,
                                Float.parseFloat(tokens[2])));
                        
                        level.addWaypoint(waypoint);
                        
                        break;
                    case "start":
                        level.startPoint.set(Float.parseFloat(tokens[1]),
                                             Float.parseFloat(tokens[3]),
                                             Float.parseFloat(tokens[2]));
                        
                        break;
                    case "exit":
                        Elevator elevator = new Elevator(
                                                Integer.parseInt(tokens[3]));
                        
                        elevator.position.set(Float.parseFloat(tokens[1]), 0,
                                              Float.parseFloat(tokens[2]));
                        
                        level.addSolid(elevator);
                        
                        break;
                    case "prop":
                        Prop prop = new Prop(tokens[1],
                                      (Integer.parseInt(tokens[5]) + 1) % 4);
                        
                        prop.position.set(Float.parseFloat(tokens[2]),
                                          Float.parseFloat(tokens[3]),
                                          Float.parseFloat(tokens[4]));
                        
                        level.addSolid(prop);
                        
                        break;
                    case "link":
                        Waypoint.link(
                            level.waypoints.get(Integer.parseInt(tokens[1])),
                            level.waypoints.get(Integer.parseInt(tokens[2])));
                        
                        break;
                    default: // Incompatible line                        
                        Logger.error("Could not read LVL file " + file);
                        Logger.error("Invalid line > " + line);
                        return null;
                }
            }
            
            // Generate floor
            level.generateFloor("floor_hospital.png");
            
            return level;
        } catch(IOException e) { e.printStackTrace(); return null; }
    }
}
