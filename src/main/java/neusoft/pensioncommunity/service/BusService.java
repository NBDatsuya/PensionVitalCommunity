package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.dao.BusDao;
import neusoft.pensioncommunity.model.Bus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

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

    public Bus searchById(int id){
        return dao.search(id);
    }

    public ObservableList<Bus> searchByName(String name){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getName().contains(name))
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByCode(String code){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getCode().contains(code))
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByDirection(int direction){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getDirection()==direction)
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByAnnual(int annual){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getAnnual()==annual)
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByHours(int hours){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getHours()==hours)
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByBeginTime(String strTime){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getTimeBegin().toString().contains(strTime))
                list.add(item);

        return list;
    }

    public ObservableList<Bus> searchByDeadline(String strTime){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll()){
            if(item.isDDLSet() && item.getTimeDeadline().toString().contains(strTime))
                list.add(item);
            else if (!item.isDDLSet() && strTime.isEmpty())
                list.add(item);

        }
        return list;
    }

    public ObservableList<Bus> searchByMemo(String memo){
        ObservableList<Bus> list = FXCollections.observableArrayList();
        for(Bus item : dao.getAll())
            if(item.getMemo().contains(memo))
                list.add(item);

        return list;
    }
}

