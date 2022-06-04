package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.dao.BusDao;
import neusoft.pensioncommunity.model.Bus;

public class BusService implements Service<Bus>{
    @Getter
    private static final BusService instance = new BusService();

    private BusService(){}
    private static final BusDao dao = BusDao.getInstance();

    @Override
    public void add(Bus item) {
        dao.add(item);
    }

    @Override
    public boolean remove(int id) {
        if (dao.search(id).getCountPassenger()!=0)
            return false;
        else
            dao.remove(id);
        return true;
    }

    @Override
    public void modify(int id, Bus item) {
        dao.modify(id,item);
    }

    @Override
    public ObservableList<Bus> getAll() {
        return FXCollections.observableArrayList(dao.getAll());
    }

    @Override
    public void save() {
        dao.save();
    }

    @Override
    public int size() {
        return dao.size();
    }

    @Override
    public int getNewId() {
        return dao.getNewId();
    }
}

