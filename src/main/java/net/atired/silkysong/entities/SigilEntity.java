package net.atired.silkysong.entities;

import net.atired.silkysong.particles.types.ParryParticleEffect;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SigilEntity extends Entity {

    private Vec3d[] trailPositions = new Vec3d[64];
    private int trailPointer = -1;
    private float lerping = 0.0f;
    private LivingEntity target;
    private static final TrackedData<Integer> SIGIL_AGE = DataTracker.registerData(SigilEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Float> SIGIL_DISAPPEAR = DataTracker.registerData(SigilEntity.class,TrackedDataHandlerRegistry.FLOAT);
    public SigilEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SIGIL_AGE, 0);
        builder.add(SIGIL_DISAPPEAR, 0.0f);
    }

    public float getSigilDisappear() {
        return this.dataTracker.get(SIGIL_DISAPPEAR);
    }
    public void setSigilDisappear(float aged) {
        this.dataTracker.set(SIGIL_DISAPPEAR,aged);
    }
    public int getSigilAge() {
        return this.dataTracker.get(SIGIL_AGE);
    }
    public void setSigilAge(int aged) {
        this.dataTracker.set(SIGIL_AGE,aged);
    }

    public Vec3d getTrailPosition(int pointer, float partialTick) {
        if (this.isRemoved()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3d d0 = this.trailPositions[j];
        Vec3d d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.multiply(partialTick));
    }
    public boolean hasTrail() {
        return trailPointer != -1;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getSigilDisappear()>0.0f){
            if(getWorld() instanceof ServerWorld serverWorld){
                float sigil =this.getSigilDisappear();
                this.setSigilDisappear(sigil+0.15f);
                if(sigil>=1f-0.15f){
                    this.discard();
                }
            }
            return;
        }
        else if(this.age>200&&getWorld() instanceof ServerWorld serverWorld){
            this.setSigilDisappear(0.01f);
        }
        double velmult = 1.0;
        if(this.age<72){
            this.age+=6;
            velmult*=this.age/72.0;
        }

        else if(getWorld() instanceof ServerWorld serverWorld&&target==null){
            float distance = 100.0f;
            for (LivingEntity a : serverWorld.getEntitiesByClass(LivingEntity.class,getBoundingBox().expand(10),(living -> {return !(living instanceof BloodEntity)&&living.canTakeDamage();}))){
                if(distanceTo(a)<distance){
                    distance = distanceTo(a);
                    this.target = a;
                }
            }

        }
        int age = getSigilAge();
        if(getWorld() instanceof ServerWorld serverWorld){
            if(this.target!=null&&this.lerping<1.0f){
                this.lerping+=0.01f;
            }
            Vec3d rotating = (new Vec3d(0.68*velmult,0,0).rotateY((float)age/36.0f*3.14f)).add(0,Math.sin(age/6.4f)/4.0f*velmult,0);

            if(this.target!=null&&this.lerping>0.0f){
                Vec3d dir = this.getPos().subtract(target.getPos().add(0,target.getHeight()/2.0f,0)).normalize();
                if(this.lerping>0.2f&&getSigilAge()%4==0){
                    serverWorld.spawnParticles(new ParryParticleEffect(getVelocity().normalize().multiply(-1).toVector3f(),1),getX(),getY()+0.1,getZ(),1,0.02,0.02,0.02,0);
                }
                rotating = rotating.multiply((1.0f-this.lerping));
                rotating = rotating.add(dir.multiply(-0.9).multiply(this.lerping));
            }
            if(this.target!=null&&this.distanceTo(target)<1.3){
                target.damage(serverWorld,getDamageSources().indirectMagic(this,this),6.0f);
                target.hurtTime = 0;


                target.addVelocity(getVelocity().add(0,0.5,0).normalize().multiply(0.2));
                setSigilDisappear(0.05f);
            }
            this.setVelocity(rotating);

            setSigilAge(age+1);

        }
        this.setPosition(this.getPos().add(this.getVelocity()));
        Vec3d trailAt = this.getPos().add(0, this.getHeight() / 2F, 0);
        if (trailPointer == -1) {
            Vec3d backAt = trailAt;
            for (int i = 0; i < trailPositions.length; i++) {
                trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomData(ReadView view) {
        int aged = view.getInt("age",(int)Math.round(Math.random()*360));
        this.setSigilAge(aged);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {

        return super.createSpawnPacket(entityTrackerEntry);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putInt("age",this.getSigilAge());
    }
}
