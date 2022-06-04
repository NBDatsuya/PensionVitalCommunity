package neusoft.pensioncommunity.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import neusoft.pensioncommunity.GlobalConfig;
import neusoft.pensioncommunity.dao.ReserveDao;
import neusoft.pensioncommunity.model.Bus;
import neusoft.pensioncommunity.model.Reserve;
import neusoft.pensioncommunity.model.Senior;

import java.time.LocalDateTime;
import java.util.List;

public class ReserveService implements Service<Reserve> {
    @Getter
    private static final ReserveService instance = new ReserveService();

    private ReserveService(){}
    private static final ReserveDao dao = ReserveDao.getInstance();


    @Override
    public void add(Reserve item) {
        dao.add(item);
    }

    @Override
    public boolean remove(int id) {
        dao.remove(id);
        return true;
    }

    @Override
    public void modify(int id, Reserve item) {
        dao.modify(id,item);
    }

    @Override
    public ObservableList<Reserve> getAll() {
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

    public ObservableList<Senior> searchForRestSeniors(int busId) {
        ObservableList<Reserve> byBusAndStatus = searchByStatus(0,searchByBus(busId,dao.getAll()));
        ObservableList<Senior> seniorList = FXCollections.observableArrayList();
            for(Senior senior: GlobalConfig.seniorService.getAll())
                if(searchByPassenger(senior.getId(),byBusAndStatus).isEmpty())
                    seniorList.add(senior);

        return FXCollections.observableArrayList(seniorList);
    }

    public ObservableList<Senior> searchForUnchecked(int busId) {
        ObservableList<Reserve> byBusAndStatus = searchByStatus(0,searchByBus(busId,dao.getAll()));
        ObservableList<Senior> seniorList = FXCollections.observableArrayList();
        for(Senior senior: GlobalConfig.seniorService.getAll())
            for(Reserve reserve: byBusAndStatus)
                if(reserve.getSeniorId()==senior.getId())
                    seniorList.add(senior);

        return FXCollections.observableArrayList(seniorList);
    }

    public ObservableList<Reserve> searchByBus (int busId,List<Reserve> subList){
        ObservableList<Reserve> list = FXCollections.observableArrayList();
        for(Reserve item : subList)
            if(item.getBusId()==busId)
                list.add(item);

        return list;
    }
    public ObservableList<Reserve> searchByStatus(int status,List<Reserve> subList){
        ObservableList<Reserve> list = FXCollections.observableArrayList();
        for(Reserve item : subList)
            if(item.getStatus()==status)
                list.add(item);

        return list;
    }
    public ObservableList<Reserve> searchByLogistic(int logisticId){
        ObservableList<Reserve> list = FXCollections.observableArrayList();
        for(Reserve item : dao.getAll())
            if(item.getLogisticId()==logisticId)
                list.add(item);

        return list;
    }
    public ObservableList<Reserve> searchByLogistic(int logisticId,List<Reserve> subList){
        ObservableList<Reserve> list = FXCollections.observableArrayList();
        for(Reserve item : subList)
            if(item.getLogisticId()==logisticId)
                list.add(item);

        return list;
    }
    public ObservableList<Reserve> searchByPassenger(int seniorId,List<Reserve> subList){
        ObservableList<Reserve> list = FXCollections.observableArrayList();
        for(Reserve item : subList)
            if(item.getSeniorId()==seniorId)
                list.add(item);

        return list;
    }

    public void submitReserve(int busId, List<Senior> passengers){
        for(Senior senior:passengers){
            Reserve reserve = new Reserve(
                    busId,
                    senior.getId(),
                    GlobalConfig.currentUser.getId(),
                    0, LocalDateTime.now()
            );
            reserve.setId(getNewId());
            dao.add(reserve);
        }
    }

    public LocalDateTime getUnCheckedReserveTime(int busId, int seniorId){
        List<Reserve> list = searchByPassenger(seniorId,searchByStatus(0,searchByBus(busId,dao.getAll())));
        return (list.isEmpty()?null:list.get(list.size()-1).getReserveTime());
    }
    public void confirmReserve(int busId, ObservableList<Senior> passengers) {
        for(Senior senior : passengers)
            for(Reserve reserve : searchByPassenger(senior.getId(),searchByStatus(0,searchByBus(busId,dao.getAll())))){
                reserve.setStatus(1);
                dao.modify(reserve.getId(),reserve);
            }
    }
    public void cancelReserve(int busId, ObservableList<Senior> passengers) {
        for(Senior senior : passengers)
            for(Reserve reserve : searchByPassenger(senior.getId(),searchByStatus(0,searchByBus(busId,dao.getAll())))){
                reserve.setStatus(2);
                dao.modify(reserve.getId(),reserve);
            }
    }
}
