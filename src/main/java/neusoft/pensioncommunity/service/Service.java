package neusoft.pensioncommunity.service;

import neusoft.pensioncommunity.model.Model;

import java.util.List;

public interface Service<T extends Model>{
    public void add(T item);
    public void remove(int id);
    public T search(int id);
    public void modify(int id,T item);
    public List<T> getAll();
    public void load();
    public void save();
    public int size();
    public int getNewId();
}
