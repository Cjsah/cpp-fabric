package net.cpp.api;

import javax.swing.text.html.parser.Entity;

public interface IDarkTransform {
    <T extends Entity> void run(T entity);
}
