package net.atired.silkysong.entities;

import net.atired.silkysong.networking.payloads.CubePayload;
import net.atired.silkysong.networking.payloads.VelSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TrashCubeEntity extends Entity {

    public List<Entity> entities = new ArrayList<>();
    public List<Vec3d> positions = new ArrayList<>();
    public Vec3d collidedDir = Vec3d.ZERO;

    private static final TrackedData<Float> PITCH= DataTracker.registerData(TrashCubeEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> YAW = DataTracker.registerData(TrashCubeEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public TrashCubeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(PITCH,0.0f);
        builder.add(YAW,282.6f);
    }
    public void setCustomPitch(float pitch) {
        dataTracker.set(PITCH,pitch);
    }
    public void setCustomYaw(float yaw) {
        dataTracker.set(YAW,yaw);
    }
    public float getCustomPitch() {
        return dataTracker.get(PITCH);
    }
    public float getCustomYaw() {
        return dataTracker.get(YAW);
    }

    @Override
    public void tick() {
        float customPitch = getCustomPitch();
        float customYaw = getCustomYaw();
        Vec3d pardir = new Vec3d(0,0,-0.7).rotateX(-customPitch/180*3.14f).rotateY(customYaw/90.0f).multiply(0.6);
        for (int i = 0; i < 2; i++) {
            Vec3d parpos = new Vec3d((random.nextFloat()-0.5f)*1.7f,(random.nextFloat()-0.5f)*1.7f,-0.85).rotateX(-customPitch/180*3.14f).rotateY(customYaw/90.0f).add(getPos()).add(0,1,0);
           getWorld().addParticleClient(i==0?ParticleTypes.WHITE_SMOKE:ParticleTypes.SMALL_GUST,parpos.x,parpos.y,parpos.z,pardir.x,pardir.y,pardir.z);

        }
        float yawCos = (float) Math.cos(customYaw/90.0f);
        float yawSin = (float) Math.sin(customYaw/90.0f);
        float pitchCos = (float) Math.cos(customPitch/180*3.14f);
        float pitchSin = (float) Math.sin(customPitch/180*3.14f);
        for(Entity other : getWorld().getEntitiesByClass(Entity.class,getBoundingBox().expand(3),Entity::isAlive)){
            if(other==this){
                continue;
            }
            double translatedX = other.getX() - (this.getX()+pardir.x*8);
            double translatedY = other.getY()+other.getHeight()/2 - (this.getY()+1+pardir.y*8);
            double translatedZ = other.getZ() - (this.getZ()+pardir.z*8);
            double x1 = translatedX * yawCos - translatedZ * yawSin;
            double z1 = translatedX * yawSin + translatedZ * yawCos;
            double y1 = translatedY;
            double y2 = y1 * pitchCos - z1 * pitchSin;
            double z2 = y1 * pitchSin + z1 * pitchCos;
            double x2 = x1;
            if((Math.abs(x2) <= 0.8) && (Math.abs(y2) <= 0.8) && (Math.abs(z2) <= 4)){
                other.addVelocity(pardir.multiply(Math.max(0,4-distanceTo(other))*0.7));
            }
        }
        if(getWorld() instanceof ServerWorld serverWorld){
            setCustomYaw(getCustomYaw()+(float)getVelocity().multiply(1,0.0,1).length()*30.0f);
            if(!isOnGround()){
                setCustomYaw(getCustomYaw()+(float)getVelocity().multiply(0,1.0,0).length()*30.0f);
                setCustomPitch(getCustomPitch()+(float)getVelocity().multiply(1,0.0,1).multiply(1.0+getVelocity().multiply(0,1,0).length()).length()*4.2f);
            }
        }

        if(collidedDir!=Vec3d.ZERO&&collidedDir.length()>0.2&&getWorld() instanceof ServerWorld serverWorld){
            setVelocity(collidedDir);
            collidedDir= Vec3d.ZERO;
        }
        if(getWorld().isClient())
        {
            for(int i = 0; i < entities.size(); i++){
                Entity player = entities.get(i);
                Vec3d move = positions.get(i);

                player.move(MovementType.SELF,move);
            }
        }
        for(TrashCubeEntity other : getWorld().getEntitiesByClass(TrashCubeEntity.class,getBoundingBox().expand(-0.01),(cube)->{return cube!=this;})){
            Vec3d diraway = other.getPos().subtract(getPos()).normalize().multiply(0.3);
            other.addVelocity(diraway);
        }
        for(LivingEntity other : getWorld().getEntitiesByClass(LivingEntity.class,getBoundingBox().expand(0.1).offset(0,-0.15,0).offset(getVelocity().multiply(0.4,0,0.4)),LivingEntity::canTakeDamage)){
            if(true){
                if(getVelocity().length()>0.3&&getWorld() instanceof ServerWorld serverWorld){
                    if(other.damage(serverWorld,getDamageSources().flyIntoWall(),5.0f)){
                        Vec3d norm= getVelocity().multiply(-1,0,-1).normalize();
                        other.takeKnockback(2,norm.x,norm.z);
                        this.setVelocity(this.getVelocity().multiply(-0.7,1,-0.7));
                        break;
                    }
                }
            }
        }
        if(!entities.isEmpty()){
            entities.clear();
            positions.clear();
        }
        super.tick();
        if(isOnGround()&&getWorld() instanceof ServerWorld serverWorld){
            setCustomPitch(Math.round(getCustomPitch()/90)*90f);
        }
        move(MovementType.SELF,getVelocity());
        setVelocity(getVelocity().multiply(0.97,0.94,0.97));
        addVelocity(0,-0.12f,0);
    }

    @Override
    public void setVelocity(Vec3d velocity) {
        if(getWorld() instanceof ServerWorld serverWorld){
            for(ServerPlayerEntity serverPlayerEntity:PlayerLookup.tracking(this)){
                ServerPlayNetworking.send(serverPlayerEntity,new VelSyncPayload(getId(),velocity.x,velocity.y,velocity.z));
            }
            super.setVelocity(velocity);
        }
    }
    public void setClientVelocity(Vec3d velocity){
        super.setVelocity(velocity);
    }
    @Override
    public void setVelocity(double x, double y, double z) {
        super.setVelocity(x, y, z);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if(source.getAttacker()!=null&&amount>0.8){
            Entity attacker = source.getAttacker();
            Vec3d dir = attacker.getPos().subtract(getPos()).multiply(1,0.1,1).normalize().multiply(-0.35*Math.pow(amount,0.5)).add(0,1.0f,0);
            if(attacker.getY()-1.7>getY()&&distanceTo(attacker)<3){
                dir=attacker.getRotationVec(0).multiply(1,0,1).normalize().multiply(0.8);
            }
            setVelocity(dir);
        }
        return true;
    }

    @Override
    public boolean collides(Vec3d oldPos, Vec3d newPos, List<Box> boxes) {
        return super.collides(oldPos, newPos, boxes);
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        movement = this.adjustMovementForSneaking(movement, type);
        Vec3d vec3d =adjustMovementForCollisions(this,movement,getBoundingBox(),getWorld(),List.of());
        if(vec3d.multiply(1,1,1).length()<0.04){
            return;
        }
        for(Entity entity : findRidingEntities()){
            if(getWorld() instanceof ServerWorld serverWorld){
                if(entity instanceof ServerPlayerEntity player){
                    for(ServerPlayerEntity player1 : PlayerLookup.tracking(this)){
                        ServerPlayNetworking.send(player1,new CubePayload(this.getId(),player.getId(),vec3d.getX(),vec3d.getY(),vec3d.getZ()));
                    }
                }
                entity.move(MovementType.SHULKER_BOX,vec3d);
            }
        }
    }

    protected List<Entity> findRidingEntities() {

        List<Entity> onTopOfEntities2 = new ArrayList<>();
        List<Entity> onTopOfEntities = getWorld().getOtherEntities(this, getBoundingBox().contract(0, getHeight() - 0.3, 0).offset(new Vec3d(0, getHeight() - 0.5, 0)).expand(0.2, 0.7, 0.2).offset(getVelocity().multiply(-0.5,0,-0.5)).expand(Math.abs(getVelocity().x),0,Math.abs(getVelocity().z)));
        for (Entity entity : onTopOfEntities) {
            if (entity != null && entity.getY() >= this.getY() + 0.2 )
                onTopOfEntities2.add(entity);
        }
        return onTopOfEntities2;
    }
    @Override
    public boolean collidesWith(Entity other) {
        boolean collide = super.collidesWith(other);
        if(other instanceof TrashCubeEntity cubeEntity&&collide&&Math.abs(cubeEntity.getY()-getY())<0.9&&cubeEntity.getPos().subtract(getPos()).normalize().dotProduct(getVelocity().normalize())>0){
            Vec3d vec3d = getVelocity();
            Vec3d flip = getVelocity();
            if(Math.abs(cubeEntity.getX()-getX())<1.95){
                flip = flip.multiply(1,1,-1);
            }
            if(Math.abs(cubeEntity.getZ()-getZ())<1.95){
                flip = flip.multiply(-1,1,1);
            }
            setVelocity(flip.multiply(0.5,1,0.5));
            cubeEntity.setVelocity(vec3d.multiply(0.9,1,0.9));
            collidedDir = flip.multiply(0.5,1,0.5);
        }
        return collide;
    }

    @Override
    public boolean isCollidable(@Nullable Entity entity) {
        return true;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.setCustomYaw(view.getFloat("customYaw",282.6f));
        this.setCustomPitch(view.getFloat("customPitch",0.0f));
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putFloat("customYaw",getCustomYaw());
        view.putFloat("customPitch",getCustomPitch());
    }
}
