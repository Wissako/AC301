package com.luis.interfaces;

import java.util.List;

public interface ICrudDAO <T>{
List<T> getAll();
T findById(Integer id);
void actualizar(T obj);
void delete(Integer id);
void create(T obj);
}
