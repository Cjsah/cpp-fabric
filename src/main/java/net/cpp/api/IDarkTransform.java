package net.cpp.api;


import net.minecraft.entity.Entity;

public interface IDarkTransform {
    <T extends Entity> void run(T entity);
}
